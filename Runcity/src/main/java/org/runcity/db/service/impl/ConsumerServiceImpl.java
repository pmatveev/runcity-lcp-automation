package org.runcity.db.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.Token;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.enumeration.SecureUserRole;
import org.runcity.db.repository.ConsumerRepository;
import org.runcity.db.repository.PersistedLoginsRepository;
import org.runcity.db.repository.TokenRepository;
import org.runcity.db.repository.VolunteerRepository;
import org.runcity.db.service.ConsumerService;
import org.runcity.exception.DBException;
import org.runcity.exception.EMailException;
import org.runcity.exception.UnexpectedArgumentException;
import org.runcity.secure.SecureUserDetails;
import org.runcity.util.CommonProperties;
import org.runcity.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@Transactional(rollbackFor = { DBException.class, UnexpectedArgumentException.class })
public class ConsumerServiceImpl implements ConsumerService {
	private static final Logger logger = Logger.getLogger(ConsumerServiceImpl.class);

	@Autowired
	private ConsumerRepository consumerRepository;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private PersistedLoginsRepository persistedLoginsRepository;

	@Autowired
	private VolunteerRepository volunteerRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SessionRegistry sessionRegistry;

	private void initialize(Consumer c, Consumer.SelectMode selectMode) {
		switch (selectMode) {
		case WITH_ROLES:
			Hibernate.initialize(c.getRoles());
			break;
		default:
			break;
		}		
	}
	
	@Override
	public List<Consumer> selectAll(Consumer.SelectMode selectMode) {
		List<Consumer> consumers = consumerRepository.findAll();
		for (Consumer c : consumers) {
			initialize(c, selectMode);
		}
		return consumers;
	}

	@Override
	public Consumer selectByUsername(String username, Consumer.SelectMode selectMode) {
		Consumer c = consumerRepository.findByUsername(username);
		initialize(c, selectMode);
		return c;
	}

	@Override
	public Consumer selectByEmail(String email, Consumer.SelectMode selectMode) {
		Consumer c = consumerRepository.findByEmail(email);
		initialize(c, selectMode);
		return c;
	}

	@Override
	public Consumer selectById(Long id, Consumer.SelectMode selectMode) {
		Consumer c = consumerRepository.findOne(id);
		initialize(c, selectMode);
		return c;
	}

	@Override
	public Consumer add(Consumer c) throws DBException {
		if (c.getId() != null) {
			throw new UnexpectedArgumentException("Cannot edit existing user with this service");
		}

		try {
			return consumerRepository.save(c);
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	private void updateSessionsNoRoles(String username, Consumer c) {
		updateSessions(username, c, false, null);
	}
	
	private void updateSessionsWithRoles(String username, Consumer c, List<SecureUserRole> prevRoles) {
		updateSessions(username, c, true, prevRoles);
	}
	
	private void updateSessions(String username, Consumer c, boolean roles, List<SecureUserRole> prevRoles) {
		List<SecureUserRole> newRoles = c.getRoleEnum();
		boolean forceLogout = Boolean.FALSE.equals(c.isActive());
		
		if (roles) {
			forceLogout = forceLogout || !prevRoles.containsAll(newRoles) || !newRoles.containsAll(prevRoles);
		}
		logger.debug("User info changed for " + username + ". Looping principals");

		for (Object p : sessionRegistry.getAllPrincipals()) {
			if (p instanceof SecureUserDetails) {
				SecureUserDetails d = (SecureUserDetails) p;
				if (ObjectUtils.nullSafeEquals(username, d.getUsername())) {
					logger.debug("\tPrincipal found");
					logger.debug("\t\tupdating");
					d.update(c.getId(), c.getUsername(), c.isActive(), c.getPassHash(), c.getCredentials(),
							c.getEmail(), c.getLocale(), roles ? c.getRoleEnum() : null);
					if (forceLogout) {
						for (SessionInformation si : sessionRegistry.getAllSessions(p, false)) {
							logger.debug("\t\tsession " + si.getSessionId() + " expired");
							si.expireNow();
						}
					}
				}
			}
		}
	}

	@Override
	public Consumer update(Consumer c) throws DBException {
		if (c.getId() == null) {
			throw new UnexpectedArgumentException("Cannot create new user with this service");
		}

		try {
			Consumer prev = selectById(c.getId(), Consumer.SelectMode.WITH_ROLES);

			String prevUsername = prev.getUsername();
			List<SecureUserRole> prevRoles = new LinkedList<SecureUserRole>(prev.getRoleEnum());

			if (!StringUtils.isEqual(c.getUsername(), prev.getUsername())) {
				persistedLoginsRepository.updateUsername(prev.getUsername(), c.getUsername());
			}

			prev.update(c);

			Consumer result = consumerRepository.save(prev);
			Hibernate.initialize(result.getRoles());

			updateSessionsWithRoles(prevUsername, result, prevRoles);

			return result;
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	private void delete(Long id) {
		consumerRepository.delete(id);
	}

	public void delete(List<Long> id) {
		for (Long i : id) {
			delete(i);
		}
	}

	@Override
	public boolean validatePassword(Consumer c, String password) {
		return new BCryptPasswordEncoder().matches(StringUtils.toNvlString(password), c.getPassHash());
	}

	private Consumer updateConsumerPassword(Consumer c, String newPassword) throws DBException {
		try {
			c.setPassHash(new BCryptPasswordEncoder(10).encode(newPassword));
			return update(c);
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	private SecureUserDetails getCurrentUser() {
		return SecureUserDetails.getCurrent();
	}

	private Long getCurrentUserId() {
		return getCurrentUser().getId();
	}

	@Override
	public Consumer getCurrent() {
		return selectById(getCurrentUserId(), Consumer.SelectMode.NONE);
	}

	@Override
	public Consumer updateCurrentData(String username, String credentials, String email, String locale) {
		Consumer c = getCurrent();
		
		if (c == null) {
			return null;
		}
		
		String prevUsername = c.getUsername();

		c.setUsername(username);
		c.setCredentials(credentials);
		c.setEmail(email);
		c.setLocale(locale);
		try {
			c = update(c);

			updateSessionsNoRoles(prevUsername, c);

			return c;
		} catch (DBException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Consumer updateCurrentPassword(String newPassword) throws DBException {
		return updateConsumerPassword(getCurrent(), newPassword);
	}

	@Override
	public Consumer register(String username, String password, String credentials, String email, String locale)
			throws DBException {
		Consumer c = new Consumer(null, username, true, password, credentials, email, locale, null);
		return add(c);
	}

	@Override
	public List<Consumer> updatePassword(List<Long> id, String newPassword) throws DBException {
		List<Consumer> result = new ArrayList<Consumer>(id.size());

		for (Long i : id) {
			Consumer c = selectById(i, Consumer.SelectMode.NONE);
			c = updateConsumerPassword(c, newPassword);
			result.add(c);
		}

		return result;
	}

	@Override
	public void recoverPassword(Consumer c, CommonProperties commonProperties, MessageSource messageSource,
			Locale locale) throws DBException, EMailException {
		Date dateFrom = new Date();
		Calendar dateToCal = Calendar.getInstance();
		dateToCal.add(Calendar.SECOND, commonProperties.getPasswordTokenLifetime());
		Date dateTo = dateToCal.getTime();

		invalidateRecoveryTokens(c, dateFrom);

		int counter = 3;
		Token token = null;
		while (true) {
			token = new Token(null, c, dateFrom, dateTo, UUID.randomUUID().toString().replace("-", ""));

			try {
				token = tokenRepository.save(token);
				break;
			} catch (Throwable t) {
				if (counter > 0) {
					counter--;
				} else {
					throw new DBException(t);
				}
			}
		}

		if (token == null) {
			throw new DBException("Could not create password recovery token");
		}

		Locale messageLocale = null;
		try {
			messageLocale = org.springframework.util.StringUtils
					.parseLocaleString(SecureUserDetails.getLocaleCurrent());
		} catch (Throwable t) {
		}

		messageLocale = messageLocale == null ? locale : messageLocale;

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, false, "utf-8");
			helper.setFrom(commonProperties.getEmailFrom(),
					messageSource.getMessage("passwordRecovery.emailSource", null, locale));
			helper.setTo(c.getEmail());
			helper.setSubject(messageSource.getMessage("passwordRecovery.emailSubject", null, locale));
			message.setContent(
					messageSource.getMessage(
							"passwordRecovery.emailText", new Object[] { c.getCredentials(), commonProperties.getUrl()
									+ "recoverPassword?token=" + token.getToken() + "&check=" + token.check() },
							messageLocale),
					"text/html");
			mailSender.send(message);
		} catch (Throwable t) {
			throw new EMailException(t);
		}
	}

	private void invalidateRecoveryTokens(Consumer c, Date d) {
		tokenRepository.invalidateToken(c, d);
	}

	public Token getPasswordResetToken(String token, String check) {
		Token t = tokenRepository.selectToken(token, new Date());

		return t == null ? null : StringUtils.isEqual(check, t.check()) ? t : null;
	}

	public void invalidateRecoveryTokens(Consumer c) throws DBException {
		try {
			invalidateRecoveryTokens(c, new Date());
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	@Override
	public Consumer resetPasswordByToken(Token token, String password) throws DBException {
		if (token != null) {
			Consumer c = updateConsumerPassword(selectById(token.getConsumer().getId(), Consumer.SelectMode.NONE), password);
			invalidateRecoveryTokens(c);
			return c;
		}
		return null;
	}

	@Override
	public List<Volunteer> selectVolunteers(Long consumer) {
		return selectVolunteers(consumerRepository.findOne(consumer));
	}

	@Override
	public List<Volunteer> selectVolunteers(Consumer consumer) {
		return volunteerRepository.findCPByConsumer(consumer);
	}

	@Override
	public List<Volunteer> selectCoordinators(Long consumer) {
		return selectVolunteers(consumerRepository.findOne(consumer));
	}

	@Override
	public List<Volunteer> selectCoordinators(Consumer consumer) {
		return volunteerRepository.findGameByConsumer(consumer);
	}
}

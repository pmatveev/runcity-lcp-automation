package org.runcity.db.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.mail.internet.MimeMessage;

import org.hibernate.Hibernate;
import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.Token;
import org.runcity.db.entity.Volunteer;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = { DBException.class, UnexpectedArgumentException.class })
public class ConsumerServiceImpl implements ConsumerService {
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

	@Override
	@Secured("ROLE_ADMIN")
	public List<Consumer> selectAll(boolean roles) {
		List<Consumer> consumers = consumerRepository.findAll();
		if (roles) {
			for (Consumer c : consumers) {
				Hibernate.initialize(c.getRoles());
			}
		}
		return consumers;
	}

	@Override
	public Consumer selectByUsername(String username, boolean roles) {
		Consumer c = consumerRepository.findByUsername(username);
		if (roles) {
			Hibernate.initialize(c.getRoles());
		}
		return c;
	}

	@Override
	public Consumer selectByEmail(String email, boolean roles) {
		Consumer c = consumerRepository.findByEmail(email);
		if (roles) {
			Hibernate.initialize(c.getRoles());
		}
		return c;
	}

	@Override
	public Consumer selectById(Long id, boolean roles) {
		Consumer c = consumerRepository.findOne(id);
		if (roles) {
			Hibernate.initialize(c.getRoles());
		}
		return c;
	}

	@Override
	@Secured("ROLE_ADMIN")
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

	@Override
	@Secured("ROLE_ADMIN")
	public Consumer update(Consumer c) throws DBException {
		if (c.getId() == null) {
			throw new UnexpectedArgumentException("Cannot create new user with this service");
		}

		try {
			Consumer prev = selectById(c.getId(), true);

			if (!StringUtils.isEqual(c.getUsername(), prev.getUsername())) {
				persistedLoginsRepository.updateUsername(prev.getUsername(), c.getUsername());
			}

			prev.update(c);

			return consumerRepository.save(prev);
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	private void delete(Long id) {
		consumerRepository.delete(id);
	}

	@Secured("ROLE_ADMIN")
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
		return selectById(getCurrentUserId(), false);
	}

	@Override
	public Consumer updateCurrentData(String username, String credentials, String email, String locale) {
		Consumer c = getCurrent();

		if (c == null) {
			return null;
		}

		c.setUsername(username);
		c.setCredentials(credentials);
		c.setEmail(email);
		c.setLocale(locale);
		try {
			c = update(c);

			SecureUserDetails user = getCurrentUser();
			user.setUsername(c.getUsername());
			user.setCredentials(c.getCredentials());
			user.setEmail(c.getEmail());
			user.setLocale(locale);

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
	@Secured("ROLE_ADMIN")
	public List<Consumer> updatePassword(List<Long> id, String newPassword) throws DBException {
		List<Consumer> result = new ArrayList<Consumer>(id.size());

		for (Long i : id) {
			Consumer c = selectById(i, false);
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
			helper.setFrom(commonProperties.getEmailFrom(), messageSource.getMessage("passwordRecovery.emailSource", null, locale));
			helper.setTo(c.getEmail());
			helper.setSubject(messageSource.getMessage("passwordRecovery.emailSubject", null, locale));
			message.setContent(messageSource.getMessage("passwordRecovery.emailText",
					new Object[] { c.getCredentials(),
							commonProperties.getUrl() + "recoverPassword?token=" + token.getToken() + "&check=" + token.check()},
					messageLocale), "text/html");
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
			Consumer c = updateConsumerPassword(selectById(token.getConsumer().getId(), false), password);
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
		return volunteerRepository.findByConsumer(consumer);
	}
}

package org.runcity.db.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.runcity.db.entity.Consumer;
import org.runcity.db.repository.ConsumerRepository;
import org.runcity.db.repository.PersistedLoginsRepository;
import org.runcity.db.service.ConsumerService;
import org.runcity.exception.DBException;
import org.runcity.exception.UnexpectedArgumentException;
import org.runcity.secure.SecureUserDetails;
import org.runcity.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = {DBException.class, UnexpectedArgumentException.class})
public class ConsumerServiceImpl implements ConsumerService {
	@Autowired
	private ConsumerRepository consumerRepository;

	@Autowired
	private PersistedLoginsRepository persistedLoginsRepository;

	@Override
	@Secured("ROLE_ADMIN")
	public List<Consumer> selectAll() {
		return consumerRepository.findAll();
	}

	@Override
	public Consumer selectByUsername(String username) {
		return consumerRepository.findByUsername(username);
	}

	@Override
	public Consumer selectByEmail(String email) {
		return consumerRepository.findByEmail(email);
	}

	@Override
	public Consumer selectById(Long id) {
		return consumerRepository.findOne(id);
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
			Consumer prev = selectById(c.getId());

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
		c.setPassHash(new BCryptPasswordEncoder(10).encode(newPassword));
		return update(c);
	}

	private SecureUserDetails getCurrentUser() {
		return SecureUserDetails.getCurrent();
	}

	private Long getCurrentUserId() {
		return getCurrentUser().getId();
	}

	@Override
	public Consumer getCurrent() {
		return selectById(getCurrentUserId());
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
	public Consumer register(String username, String password, String credentials, String email, String locale) throws DBException {
		Consumer c = new Consumer(null, username, true, password, credentials, email, locale, null);
		return add(c);
	}

	@Override
	@Secured("ROLE_ADMIN")
	public List<Consumer> updatePassword(List<Long> id, String newPassword) throws DBException {
		List<Consumer> result = new ArrayList<Consumer>(id.size());
		
		for (Long i : id) {
			Consumer c = selectById(i);
			c = updateConsumerPassword(c, newPassword);
			result.add(c);
		}
		
		return result;
	}
}

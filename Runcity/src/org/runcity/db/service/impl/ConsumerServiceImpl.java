package org.runcity.db.service.impl;

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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsumerServiceImpl implements ConsumerService {
	@Autowired
	private ConsumerRepository consumerRepository;

	@Autowired
	private PersistedLoginsRepository persistedLoginsRepository;

	@Override
	public List<Consumer> selectAll() {
		return consumerRepository.findAll(new Sort(new Order(Direction.ASC, "credentials")));
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
		return consumerRepository.findById(id);
	}

	private Consumer addNewConsumer(Consumer c) throws DBException {
		if (c.getId() != null) {
			throw new UnexpectedArgumentException("Cannot edit existing user with this service");
		}

		try {
			return consumerRepository.save(c);
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	@Transactional
	private Consumer editConsumer(Consumer c) throws DBException {
		if (c.getId() == null) {
			throw new UnexpectedArgumentException("Cannot create new user with this service");
		}

		try {
			Consumer prev = selectById(c.getId());

			if (!StringUtils.isEqual(c.getUsername(), prev.getUsername())) {
				persistedLoginsRepository.updateUsername(prev.getUsername(), c.getUsername());
			}

			return consumerRepository.save(c);
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	@Override
	public boolean validatePassword(Consumer c, String password) {
		return new BCryptPasswordEncoder().matches(StringUtils.toNvlString(password), c.getPassHash());
	}

	@Override
	@Secured("ROLE_ADMIN")
	public Consumer updateConsumerPassword(Consumer c, String newPassword) throws DBException {
		c.setPassHash(new BCryptPasswordEncoder(10).encode(newPassword));
		return editConsumer(c);
	}

	private SecureUserDetails getCurrentUser() {
		return (SecureUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	private Long getCurrentUserId() {
		return getCurrentUser().getId();
	}

	@Override
	public Consumer getCurrent() {
		return selectById(getCurrentUserId());
	}

	@Override
	public Consumer updateCurrentData(String username, String credentials, String email) {
		Consumer c = getCurrent();

		if (c == null) {
			return null;
		}

		c.setUsername(username);
		c.setCredentials(credentials);
		c.setEmail(email);
		try {
			c = editConsumer(c);

			SecureUserDetails user = getCurrentUser();
			user.setUsername(c.getUsername());
			user.setCredentials(c.getCredentials());
			user.setEmail(c.getEmail());

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
	public Consumer register(String username, String password, String credentials, String email) throws DBException {
		Consumer c = new Consumer(null, username, true, password, credentials, email, null);
		return addNewConsumer(c);
	}

}

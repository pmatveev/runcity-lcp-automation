package org.runcity.db.service.impl;

import java.util.List;

import org.runcity.db.entity.Consumer;
import org.runcity.db.repository.ConsumerRepository;
import org.runcity.db.service.ConsumerService;
import org.runcity.exception.DBException;
import org.runcity.mvc.web.formdata.ConsumerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImpl implements ConsumerService {
	@Autowired
	private ConsumerRepository consumerRepository;

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
	public Consumer addNewConsumer(ConsumerForm form) throws DBException {
		Consumer c = new Consumer();
		c.setCredentials(form.getCredentials());
		c.setEmail(form.getEmail());
		c.setIsActive(true);
		c.setUsername(form.getUsername());
		c.setPassHash(new BCryptPasswordEncoder(10).encode(form.getPassword()));

		try {
			return consumerRepository.saveAndFlush(c);
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

}

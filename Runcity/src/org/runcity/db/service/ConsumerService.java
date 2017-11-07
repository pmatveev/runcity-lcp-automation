package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Consumer;
import org.runcity.exception.DBException;

public interface ConsumerService {
	public List<Consumer> selectAll();

	public Consumer selectByUsername(String username);

	public Consumer selectByEmail(String email);
	
	public Consumer selectById(Long id);
	
	public Consumer addNewConsumer(Consumer c) throws DBException;
	
	public Consumer editConsumer(Consumer c) throws DBException;
	
	public boolean validatePassword(Consumer c, String password);
	
	public Consumer updateConsumer(Consumer c) throws DBException;
	
	public Consumer updateConsumerPassword(Consumer c, String newPassword) throws DBException;
}

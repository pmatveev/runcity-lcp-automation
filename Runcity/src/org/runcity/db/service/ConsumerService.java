package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Consumer;
import org.runcity.exception.DBException;
import org.springframework.security.access.annotation.Secured;

public interface ConsumerService {
	@Secured("ROLE_ADMIN")
	public List<Consumer> selectAll();

	public Consumer selectByUsername(String username);

	public Consumer selectByEmail(String email);
	
	public Consumer selectById(Long id);
	
	@Secured("ROLE_ADMIN")
	public Consumer addNewConsumer(Consumer c) throws DBException;
	
	@Secured("ROLE_ADMIN")
	public Consumer editConsumer(Consumer c) throws DBException;
		
	@Secured("ROLE_ADMIN")
	public void deleteConsumer(List<Long> id) throws DBException;
	
	public boolean validatePassword(Consumer c, String password);
	
	public Consumer register(String username, String password, String credentials, String email) throws DBException;
	
	public Consumer getCurrent();
	
	public Consumer updateCurrentData(String username, String credentials, String email);

	public Consumer updateCurrentPassword(String newPassword) throws DBException;
	
	@Secured("ROLE_ADMIN")
	public List<Consumer> updateConsumerPassword(List<Long> id, String newPassword) throws DBException;
}

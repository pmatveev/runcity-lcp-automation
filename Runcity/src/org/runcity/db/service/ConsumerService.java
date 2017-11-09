package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Consumer;
import org.runcity.exception.DBException;
import org.springframework.security.access.annotation.Secured;

public interface ConsumerService {
	public List<Consumer> selectAll();

	public Consumer selectByUsername(String username);

	public Consumer selectByEmail(String email);
	
	public Consumer selectById(Long id);
	
	public boolean validatePassword(Consumer c, String password);
	
	@Secured("ROLE_ADMIN")
	public Consumer updateConsumerPassword(Consumer c, String newPassword) throws DBException;
	
	public Consumer register(String username, String password, String credentials, String email) throws DBException;
	
	public Consumer getCurrent();
	
	public Consumer updateCurrentData(String username, String credentials, String email);

	public Consumer updateCurrentPassword(String newPassword) throws DBException;
}

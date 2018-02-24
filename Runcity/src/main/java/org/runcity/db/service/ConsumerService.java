package org.runcity.db.service;

import java.util.List;
import java.util.Locale;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.Token;
import org.runcity.db.entity.Volunteer;
import org.runcity.exception.DBException;
import org.runcity.exception.EMailException;
import org.runcity.util.CommonProperties;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;

public interface ConsumerService {
	@Secured("ROLE_ADMIN")
	public List<Consumer> selectAll(boolean roles);

	public Consumer selectByUsername(String username, boolean roles);

	public Consumer selectByEmail(String email, boolean roles);
	
	public Consumer selectById(Long id, boolean roles);
	
	@Secured("ROLE_ADMIN")
	public Consumer add(Consumer c) throws DBException;
	
	@Secured("ROLE_ADMIN")
	public Consumer update(Consumer c) throws DBException;
		
	@Secured("ROLE_ADMIN")
	public void delete(List<Long> id);
	
	public boolean validatePassword(Consumer c, String password);
	
	public Consumer register(String username, String password, String credentials, String email, String locale) throws DBException;
	
	public Consumer getCurrent();
	
	public Consumer updateCurrentData(String username, String credentials, String email, String locale);

	public Consumer updateCurrentPassword(String newPassword) throws DBException;
	
	@Secured("ROLE_ADMIN")
	public List<Consumer> updatePassword(List<Long> id, String newPassword) throws DBException;

	public void recoverPassword(Consumer c, CommonProperties commonProperties, MessageSource messageSource, Locale locale) throws DBException, EMailException;
	
	public Token getPasswordResetToken(String token, String check);
	
	public void invalidateRecoveryTokens(Consumer c) throws DBException;
	
	public Consumer resetPasswordByToken(Token token, String password) throws DBException;

	public List<Volunteer> selectVolunteers(Long consumer);

	public List<Volunteer> selectVolunteers(Consumer consumer);

	public List<Volunteer> selectCoordinators(Long consumer);

	public List<Volunteer> selectCoordinators(Consumer consumer);
}

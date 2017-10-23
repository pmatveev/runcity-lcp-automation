package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Consumer;
import org.runcity.exception.DBException;
import org.runcity.mvc.web.formdata.ConsumerForm;

public interface ConsumerService {
	public List<Consumer> selectAll();

	public Consumer selectByUsername(String username);
	
	public Consumer selectByEmail(String email);
	
	public Consumer addNewConsumer(ConsumerForm form) throws DBException;
}

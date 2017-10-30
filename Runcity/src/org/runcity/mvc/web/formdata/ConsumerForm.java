package org.runcity.mvc.web.formdata;

import org.apache.log4j.Logger;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.web.util.*;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class ConsumerForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(ConsumerForm.class);
	
	private FormStringColumn username;
	private FormStringColumn credentials;
	private FormStringColumn password;
	private FormStringColumn password2;
	private FormStringColumn email;

	public ConsumerForm() {
		this.formName = "consumerForm";
		this.username = new FormPlainStringColumn(null, new ColumnDefinition("username", "user.username"), formName,
				true, 4, 32);
		this.credentials = new FormPlainStringColumn(null, new ColumnDefinition("credentials", "user.credentials"),
				formName, true, 4, 32);

		FormPasswordColumn pwd = new FormPasswordColumn(null, new ColumnDefinition("password", "login.password"),
				formName, true);
		FormPasswordConfirmationColumn pwd2 = new FormPasswordConfirmationColumn(null,
				new ColumnDefinition("password2", "register.password2"), formName, true, pwd);
		pwd.setPasswordConfirmation(pwd2);
		this.password = pwd;
		this.password2 = pwd2;

		this.email = new FormEmailColumn(null, new ColumnDefinition("email", "user.email"), formName, true, 255);
	}

	public ConsumerForm(String username, String credentials, String password, String password2, String email) {
		this();
		this.username.setValue(username);
		this.credentials.setValue(credentials);
		this.password.setValue(password);
		this.password2.setValue(password2);
		this.email.setValue(email);
	}

	public FormStringColumn getPassword() {
		return password;
	}

	public void setPassword(FormPasswordColumn password) {
		this.password = password;
	}

	public FormStringColumn getPassword2() {
		return password2;
	}

	public void setPassword2(FormPasswordConfirmationColumn password2) {
		this.password2 = password2;
	}

	public FormStringColumn getUsername() {
		return username;
	}

	public void setUsername(FormPlainStringColumn username) {
		this.username = username;
	}

	public FormStringColumn getCredentials() {
		return credentials;
	}

	public void setCredentials(FormPlainStringColumn credentials) {
		this.credentials = credentials;
	}

	public FormStringColumn getEmail() {
		return email;
	}

	public void setEmail(FormEmailColumn email) {
		this.email = email;
	}
	
	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating ConsumerForm");
		username.validate(errors);
		credentials.validate(errors);
		password.validate(errors);
		password2.validate(errors);
		email.validate(errors);
		
		ConsumerService consumerService = context.getBean(ConsumerService.class);
		if (consumerService.selectByUsername(username.getValue()) != null) {
			logger.debug(username.getName() + " is not unique");
			errors.rejectValue(username.getName(), "mvc.userExists");
		}
		
		if (consumerService.selectByEmail(email.getValue()) != null) {
			logger.debug(email.getName() + " is not unique");
			errors.rejectValue(email.getName(), "mvc.emailExists");
		}
	}
}

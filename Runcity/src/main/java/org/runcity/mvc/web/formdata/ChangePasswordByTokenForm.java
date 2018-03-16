package org.runcity.mvc.web.formdata;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Token;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.*;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public class ChangePasswordByTokenForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(ChangePasswordByTokenForm.class);
	
	@JsonView(Views.Public.class)
	private FormStringColumn token;
	
	@JsonView(Views.Public.class)
	private FormStringColumn check;
	
	@JsonView(Views.Public.class)
	private FormStringColumn password;

	@JsonView(Views.Public.class)
	private FormStringColumn password2;

	private Token passwordToken;
	
	public ChangePasswordByTokenForm() {
		this(null);
	}
	
	public ChangePasswordByTokenForm(DynamicLocaleList localeList) {
		super("changePasswordByTokenForm", null, "/recoverPassword", localeList);
		logger.trace("Creating form " + getFormName());
		setTitle("changePassword.header");

		this.token = new FormPlainStringColumn(this, new ColumnDefinition("token", "token"));
		this.token.setHidden(true);
		this.check = new FormPlainStringColumn(this, new ColumnDefinition("check", "check"));
		this.check.setHidden(true);
		
		FormPasswordPair passwords = new FormPasswordPair(
				new FormPasswordColumn(this, new ColumnDefinition("password", "changePassword.password")),
				new FormPasswordConfirmationColumn(this, new ColumnDefinition("password2", "changePassword.password2")));

		this.password = passwords.getPassword();
		this.password2 = passwords.getPasswordConfirmation();
	}

	public String getToken() {
		return token.getValue();
	}
	
	public void setToken(String token) {
		this.token.setValue(token);
	}
	
	public String getCheck() {
		return check.getValue();
	}
	
	public void setCheck(String check) {
		this.check.setValue(check);
	}

	public String getPassword() {
		return password.getValue();
	}

	public void setPassword(String password) {
		this.password.setValue(password);
	}

	public String getPassword2() {
		return password2.getValue();
	}

	public void setPassword2(String password2) {
		this.password2.setValue(password2);
	}

	public FormStringColumn getTokenColumn() {
		return token;
	}
	
	public FormStringColumn getCheckColumn() {
		return check;
	}
	
	public FormStringColumn getPasswordColumn() {
		return password;
	}

	public FormStringColumn getPassword2Column() {
		return password2;
	}
	
	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		
		token.validate(context, errors);
		check.validate(context, errors);
		password.validate(context, errors);
		password2.validate(context, errors);
		
		ConsumerService consumerService = context.getBean(ConsumerService.class);
		passwordToken = consumerService.getPasswordResetToken(token.getValue(), check.getValue());
	}
	
	public Token getPasswordToken() {
		return passwordToken;
	}
}

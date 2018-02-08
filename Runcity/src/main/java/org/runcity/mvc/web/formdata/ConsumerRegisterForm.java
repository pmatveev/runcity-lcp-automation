package org.runcity.mvc.web.formdata;

import org.apache.log4j.Logger;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.*;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public class ConsumerRegisterForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(ConsumerRegisterForm.class);

	@JsonView(Views.Public.class)
	private FormStringColumn username;
	
	@JsonView(Views.Public.class)
	private FormStringColumn credentials;
	
	@JsonView(Views.Public.class)
	private FormStringColumn password;
	
	@JsonView(Views.Public.class)
	private FormStringColumn password2;
	
	@JsonView(Views.Public.class)
	private FormStringColumn email;
	
	@JsonView(Views.Public.class)
	private FormListboxLocaleColumn locale;

	public ConsumerRegisterForm() {
		this(null);
	}
	
	public ConsumerRegisterForm(DynamicLocaleList localeList) {
		super("consumerRegisterForm", null, null, "/common/api/v1/register", localeList);
		logger.trace("Creating form " + getFormName());
		setTitle("register.header");
		this.username = new FormPlainStringColumn(this, new ColumnDefinition("username", "user.username"));
		this.username.setRequired(true);
		this.username.setMinLength(4);
		this.username.setMaxLength(32);
		this.credentials = new FormPlainStringColumn(this, new ColumnDefinition("credentials", "user.credentials"));
		this.credentials.setRequired(true);
		this.credentials.setMinLength(4);
		this.credentials.setMaxLength(32);

		FormPasswordPair passwords = new FormPasswordPair(
				new FormPasswordColumn(this, new ColumnDefinition("password", "user.password")),
				new FormPasswordConfirmationColumn(this, new ColumnDefinition("password2", "user.password2")));

		this.password = passwords.getPassword();
		this.password2 = passwords.getPasswordConfirmation();

		this.email = new FormEmailColumn(this, new ColumnDefinition("email", "user.email"));
		this.email.setRequired(true);
		this.email.setMaxLength(255);
		this.locale = new FormListboxLocaleColumn(this, new ColumnDefinition("locale", "user.locale"), localeList);
	}

	public ConsumerRegisterForm(String username, String credentials, String password, String password2, String email, String locale, DynamicLocaleList localeList) {
		this(localeList);
		setUsername(username);
		setCredentials(credentials);
		setPassword(password);
		setPassword2(password2);
		setEmail(email);
		setLocale(locale);
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

	public String getUsername() {
		return username.getValue();
	}

	public void setUsername(String username) {
		this.username.setValue(username);
	}

	public String getCredentials() {
		return credentials.getValue();
	}

	public void setCredentials(String credentials) {
		this.credentials.setValue(credentials);
	}

	public String getEmail() {
		return email.getValue();
	}

	public void setEmail(String email) {
		this.email.setValue(email);
	}
	
	public String getLocale() {
		return locale.getValue();
	}
	
	public void setLocale(String locale) {
		this.locale.setValue(locale);
	}

	public FormStringColumn getUsernameColumn() {
		return username;
	}

	public FormStringColumn getPasswordColumn() {
		return password;
	}

	public FormStringColumn getPassword2Column() {
		return password2;
	}

	public FormStringColumn getCredentialsColumn() {
		return credentials;
	}

	public FormStringColumn getEmailColumn() {
		return email;
	}
	
	public FormListboxLocaleColumn getLocaleColumn() {
		return locale;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		username.validate(context, errors);
		credentials.validate(context, errors);
		password.validate(context, errors);
		password2.validate(context, errors);
		email.validate(context, errors);
		locale.validate(context, errors);

		ConsumerService consumerService = context.getBean(ConsumerService.class);
		if (consumerService.selectByUsername(username.getValue(), false) != null) {
			logger.debug(username.getName() + " is not unique");
			errors.rejectValue(username.getName(), "validation.userExists");
		}

		if (consumerService.selectByEmail(email.getValue(), false) != null) {
			logger.debug(email.getName() + " is not unique");
			errors.rejectValue(email.getName(), "validation.emailExists");
		}
	}
}

package org.runcity.mvc.web.formdata;

import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Consumer;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormEmailColumn;
import org.runcity.mvc.web.util.FormListboxActiveColumn;
import org.runcity.mvc.web.util.FormListboxLocaleColumn;
import org.runcity.mvc.web.util.FormListboxSecureUserRolesColumn;
import org.runcity.mvc.web.util.FormPasswordColumn;
import org.runcity.mvc.web.util.FormPasswordConfirmationColumn;
import org.runcity.mvc.web.util.FormPasswordPair;
import org.runcity.mvc.web.util.FormPlainStringColumn;
import org.runcity.mvc.web.util.FormStringColumn;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class ConsumerCreateForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(ConsumerCreateForm.class);

	private FormStringColumn username;
	private FormStringColumn credentials;
	private FormStringColumn password;
	private FormStringColumn password2;
	private FormStringColumn email;
	private FormListboxActiveColumn active;
	private FormListboxSecureUserRolesColumn roles;
	private FormListboxLocaleColumn locale;

	public ConsumerCreateForm() {
		this(null);
	}
	
	public ConsumerCreateForm(DynamicLocaleList localeList) {
		super("consumerCreateForm", null, null, "/api/v1/consumerCreate", localeList);
		logger.trace("Creating form " + getFormName());
		setTitle("common.create");
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
		this.active = new FormListboxActiveColumn(this, new ColumnDefinition("active", "user.active"));
		this.active.setRequired(true);
		this.roles = new FormListboxSecureUserRolesColumn(this, new ColumnDefinition("roles", "user.roles"));
		this.roles.setRequired(true);
		this.locale = new FormListboxLocaleColumn(this, new ColumnDefinition("locale", "user.locale"), localeList);
	}

	public ConsumerCreateForm(String username, String credentials, String email, boolean active, String locale, List<String> roles, DynamicLocaleList localeList) {
		this(localeList);
		setUsername(username);
		setCredentials(credentials);
		setEmail(email);
		setActive(active);
		setRoles(roles);
		setLocale(locale);
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

	public String getEmail() {
		return email.getValue();
	}

	public void setEmail(String email) {
		this.email.setValue(email);
	}

	public Boolean getActive() {
		return active.getValue();
	}

	public void setActive(Boolean active) {
		this.active.setValue(active);
	}
	
	public String getLocale() {
		return locale.getValue();
	}
	
	public void setLocale(String locale) {
		this.locale.setValue(locale);
	}

	public List<String> getRoles() {
		return roles.getValue();
	}

	public void setRoles(List<String> roles) {
		this.roles.setValue(roles);
	}

	public FormStringColumn getUsernameColumn() {
		return username;
	}

	public FormStringColumn getCredentialsColumn() {
		return credentials;
	}

	public FormStringColumn getPasswordColumn() {
		return password;
	}

	public FormStringColumn getPassword2Column() {
		return password2;
	}

	public FormStringColumn getEmailColumn() {
		return email;
	}

	public FormListboxActiveColumn getActiveColumn() {
		return active;
	}
	
	public FormListboxLocaleColumn getLocaleColumn() {
		return locale;
	}

	public FormListboxSecureUserRolesColumn getRolesColumn() {
		return roles;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		username.validate(context, errors);
		credentials.validate(context, errors);
		password.validate(context, errors);
		password2.validate(context, errors);
		email.validate(context, errors);
		active.validate(context, errors);
		roles.validate(context, errors);
		locale.validate(context, errors);

		ConsumerService consumerService = context.getBean(ConsumerService.class);

		// creating new record
		if (consumerService.selectByUsername(username.getValue(), false) != null) {
			logger.debug(username.getName() + " is not unique");
			errors.rejectValue(username.getName(), "validation.userExists");
		}

		if (consumerService.selectByEmail(email.getValue(), false) != null) {
			logger.debug(email.getName() + " is not unique");
			errors.rejectValue(email.getName(), "validation.emailExists");
		}
	}

	public Consumer getConsumer() {
		Consumer c = new Consumer(null, getUsername(), getActive(), getPassword(), getCredentials(), getEmail(), getLocale(), null);
		for (String s : getRoles()) {
			c.addRole(s);
		}
		return c;
	}
}

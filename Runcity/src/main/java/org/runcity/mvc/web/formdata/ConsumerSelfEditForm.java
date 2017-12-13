package org.runcity.mvc.web.formdata;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Consumer;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormEmailColumn;
import org.runcity.mvc.web.util.FormListboxLocaleColumn;
import org.runcity.mvc.web.util.FormPlainStringColumn;
import org.runcity.mvc.web.util.FormStringColumn;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public class ConsumerSelfEditForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(ConsumerSelfEditForm.class);

	@JsonView(Views.Public.class)
	private FormStringColumn username;

	@JsonView(Views.Public.class)
	private FormStringColumn credentials;

	@JsonView(Views.Public.class)
	private FormStringColumn email;

	@JsonView(Views.Public.class)
	private FormListboxLocaleColumn locale;

	public ConsumerSelfEditForm() {
		this(null);
	}
	
	public ConsumerSelfEditForm(DynamicLocaleList localeList) {
		super("consumerSelfEditForm", "/api/v1/consumerSelfEdit", null, "/api/v1/consumerSelfEdit");
		logger.trace("Creating form " + getFormName());
		setTitle("common.edit");
		this.username = new FormPlainStringColumn(this, new ColumnDefinition("username", "user.username"), true, 4, 32);
		this.credentials = new FormPlainStringColumn(this, new ColumnDefinition("credentials", "user.credentials"),
				true, 4, 32);

		this.email = new FormEmailColumn(this, new ColumnDefinition("email", "user.email"), true, 255);
		this.locale = new FormListboxLocaleColumn(this, new ColumnDefinition("locale", "user.locale"), localeList, false);
	}

	public ConsumerSelfEditForm(String username, String credentials, String email, String locale, DynamicLocaleList localeList) {
		this(localeList);
		setUsername(username);
		setCredentials(credentials);
		setEmail(email);
		setLocale(locale);
	}
	
	public ConsumerSelfEditForm(Consumer c, DynamicLocaleList localeList) {
		this(c.getUsername(), c.getCredentials(), c.getEmail(), c.getLocale(), localeList);
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
		email.validate(context, errors);
		locale.validate(context, errors);

		ConsumerService consumerService = context.getBean(ConsumerService.class);
		Consumer current = consumerService.getCurrent();

		if (!StringUtils.isEqual(current.getUsername(), username.getValue())
				&& consumerService.selectByUsername(username.getValue()) != null) {
			logger.debug(username.getName() + " changed but not unique");
			errors.rejectValue(username.getName(), "validation.userExists");
		}

		if (!StringUtils.isEqual(current.getEmail(), email.getValue())
				&& consumerService.selectByEmail(email.getValue()) != null) {
			logger.debug(email.getName() + " changed but not unique");
			errors.rejectValue(email.getName(), "validation.emailExists");
		}
	}
}

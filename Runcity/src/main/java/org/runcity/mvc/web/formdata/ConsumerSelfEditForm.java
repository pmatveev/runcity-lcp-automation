package org.runcity.mvc.web.formdata;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Consumer;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormEmailColumn;
import org.runcity.mvc.web.util.FormPlainStringColumn;
import org.runcity.mvc.web.util.FormStringColumn;
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

	public ConsumerSelfEditForm() {
		super("consumerSelfEditForm", "/api/v1/consumerSelfEdit", null, "/api/v1/consumerSelfEdit");
		logger.trace("Creating form " + getFormName());
		setTitle("common.edit");
		this.username = new FormPlainStringColumn(this, new ColumnDefinition("username", "user.username"), true, 4, 32);
		this.credentials = new FormPlainStringColumn(this, new ColumnDefinition("credentials", "user.credentials"),
				true, 4, 32);

		this.email = new FormEmailColumn(this, new ColumnDefinition("email", "user.email"), true, 255);
	}

	public ConsumerSelfEditForm(String username, String credentials, String email) {
		this();
		setUsername(username);
		setCredentials(credentials);
		setEmail(email);
	}
	
	public ConsumerSelfEditForm(Consumer c) {
		this(c.getUsername(), c.getCredentials(), c.getEmail());
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

	public FormStringColumn getUsernameColumn() {
		return username;
	}

	public FormStringColumn getCredentialsColumn() {
		return credentials;
	}

	public FormStringColumn getEmailColumn() {
		return email;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		username.validate(errors);
		credentials.validate(errors);
		email.validate(errors);

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

package org.runcity.mvc.web.formdata;

import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Consumer;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormEmailColumn;
import org.runcity.mvc.web.util.FormListboxActiveColumn;
import org.runcity.mvc.web.util.FormListboxUserRoleColumn;
import org.runcity.mvc.web.util.FormPlainStringColumn;
import org.runcity.mvc.web.util.FormStringColumn;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public class ConsumerEditForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(ConsumerEditForm.class);

	@JsonView(Views.Public.class)
	private FormStringColumn username;

	@JsonView(Views.Public.class)
	private FormStringColumn credentials;

	@JsonView(Views.Public.class)
	private FormStringColumn email;
	
	@JsonView(Views.Public.class)
	private FormListboxActiveColumn active;

	@JsonView(Views.Public.class)
	private FormListboxUserRoleColumn roles;
	
	public ConsumerEditForm() {
		super("consumerEditForm", "/api/v1/consumerEdit", "/secure/consumerEdit", "/api/v1/consumerEdit");
		logger.trace("Creating form " + getFormName());
		setTitle("common.edit");
		this.username = new FormPlainStringColumn(this, new ColumnDefinition("username", "user.username"), true, 4, 32);
		this.credentials = new FormPlainStringColumn(this, new ColumnDefinition("credentials", "user.credentials"),
				true, 4, 32);

		this.email = new FormEmailColumn(this, new ColumnDefinition("email", "user.email"), true, 255);
		this.active = new FormListboxActiveColumn(this, new ColumnDefinition("active", "user.active"), true);
		this.roles = new FormListboxUserRoleColumn(this, new ColumnDefinition("roles", "user.roles"), true);
	}

	public ConsumerEditForm(String username, String credentials, String email, boolean active, List<String> roles) {
		this();
		this.username.setValue(username);
		this.credentials.setValue(credentials);
		this.email.setValue(email);
		this.active.setValue(active);
		this.roles.setValue(roles);
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
	
	public Boolean getActive() {
		return active.getValue();
	}
	
	public void setActive(Boolean active) {
		this.active.setValue(active);
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

	public FormStringColumn getEmailColumn() {
		return email;
	}
	
	public FormListboxActiveColumn getActiveColumn() {
		return active;
	}
	
	public FormListboxUserRoleColumn getRolesColumn() {
		return roles;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		username.validate(errors);
		credentials.validate(errors);
		email.validate(errors);
		active.validate(errors);
		roles.validate(errors);
		
		ConsumerService consumerService = context.getBean(ConsumerService.class);
		
		if (getId() == null) {
			// new record not supported in this form
			logger.warn("unexpected id: " + id.getSafeValue());
			errors.reject("common.popupProcessError");
			return;
		} else {
			// editing existing record
			Consumer current = consumerService.selectById(getId());
			if (current == null) {
				logger.warn("unexpected id: " + id.getSafeValue());
				errors.reject("common.popupProcessError");
				return;
			}

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
}

package org.runcity.mvc.web.formdata;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Consumer;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormPasswordColumn;
import org.runcity.mvc.web.util.FormPasswordConfirmationColumn;
import org.runcity.mvc.web.util.FormPasswordPair;
import org.runcity.mvc.web.util.FormPasswordValidationColumn;
import org.runcity.mvc.web.util.FormStringColumn;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public class ChangePasswordByPasswordForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(ChangePasswordByPasswordForm.class);

	@JsonView(Views.Public.class)
	private FormStringColumn currPassword;

	@JsonView(Views.Public.class)
	private FormStringColumn password;

	@JsonView(Views.Public.class)
	private FormStringColumn password2;

	private Consumer consumerFor;

	public ChangePasswordByPasswordForm() {
		super("changePasswordByPasswordForm", null, null, "/api/v1/changePasswordByPassword");
		logger.trace("Creating form " + getFormName());
		setTitle("changePassword.header");
		this.currPassword = new FormPasswordValidationColumn(this,
				new ColumnDefinition("currPassword", "changePassword.currPassword"));
		FormPasswordPair passwords = new FormPasswordPair(
				new FormPasswordColumn(this, new ColumnDefinition("password", "changePassword.password"), true),
				new FormPasswordConfirmationColumn(this, new ColumnDefinition("password2", "changePassword.password2"),
						true));

		this.password = passwords.getPassword();
		this.password2 = passwords.getPasswordConfirmation();
	}

	public ChangePasswordByPasswordForm(String currPassword, String password, String password2) {
		this();
		setCurrPassword(currPassword);
		setPassword(password);
		setPassword2(password2);
	}

	public String getCurrPassword() {
		return currPassword.getValue();
	}

	public void setCurrPassword(String currPassword) {
		this.currPassword.setValue(currPassword);
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

	public FormStringColumn getCurrPasswordColumn() {
		return currPassword;
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
		currPassword.validate(context, errors);
		password.validate(context, errors);
		password2.validate(context, errors);

		ConsumerService consumerService = context.getBean(ConsumerService.class);
		consumerFor = consumerService.getCurrent();
		if (!consumerService.validatePassword(consumerFor, getCurrPassword())) {
			logger.debug("Wrong current password");
			errors.rejectValue(currPassword.getName(), "changePassword.invalidPwd");
		}
	}

	public Consumer getConsumerFor() {
		return consumerFor;
	}
}

package org.runcity.mvc.web.formdata;

import org.apache.log4j.Logger;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.web.util.*;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class PasswordRecoveryForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(PasswordRecoveryForm.class);

	private FormStringColumn email;

	public PasswordRecoveryForm() {
		this(null);
	}
	
	public PasswordRecoveryForm(DynamicLocaleList localeList) {
		super("passwordRecoveryForm", null, "/passwordRecovery", null, localeList);
		logger.trace("Creating form " + getFormName());
		setTitle("passwordRecovery.header");

		this.email = new FormEmailColumn(this, new ColumnDefinition("email", "user.email"));
		this.email.setRequired(true);
		this.email.setMaxLength(255);
	}

	public String getEmail() {
		return email.getValue();
	}

	public void setEmail(String email) {
		this.email.setValue(email);
	}

	public FormStringColumn getEmailColumn() {
		return email;
	}
	
	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		email.validate(context, errors);
		ConsumerService consumerService = context.getBean(ConsumerService.class);
		if (consumerService.selectByEmail(email.getValue(), false) == null) {
			logger.debug(email.getName() + " doesn't exist");
			errors.rejectValue(email.getName(), "passwordRecovery.unknownEmail");
		}
	}
}

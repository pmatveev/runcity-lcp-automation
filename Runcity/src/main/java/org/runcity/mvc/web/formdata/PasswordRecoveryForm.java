package org.runcity.mvc.web.formdata;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Consumer;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.*;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public class PasswordRecoveryForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(PasswordRecoveryForm.class);
	
	@JsonView(Views.Public.class)
	private FormStringColumn input;
	
	private Consumer consumer;

	public PasswordRecoveryForm() {
		this(null);
	}
	
	public PasswordRecoveryForm(DynamicLocaleList localeList) {
		super("passwordRecoveryForm", null, "/common/api/v1/passwordRecovery", localeList);
		logger.trace("Creating form " + getFormName());
		setTitle("passwordRecovery.header");

		this.input = new FormPlainStringColumn(this, new ColumnDefinition("input", "passwordRecovery.input"));
		this.input.setRequired(true);
		this.input.setMaxLength(255);
	}

	public String getInput() {
		return input.getValue();
	}

	public void setInput(String input) {
		this.input.setValue(input);
	}

	public FormStringColumn getInputColumn() {
		return input;
	}
	
	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		input.validate(context, errors);
		
		ConsumerService consumerService = context.getBean(ConsumerService.class);
		consumer = consumerService.selectByUsername(input.getValue(), Consumer.SelectMode.NONE);
		
		if (consumer == null) {
			consumer = consumerService.selectByEmail(input.getValue(), Consumer.SelectMode.NONE);
		}
		
		if (consumer == null) {
			logger.debug(input.getName() + " doesn't exist");
			errors.rejectValue(input.getName(), "passwordRecovery.unknownInput");
		}
	}
	
	public Consumer getConsumer() {
		return consumer;
	}
}

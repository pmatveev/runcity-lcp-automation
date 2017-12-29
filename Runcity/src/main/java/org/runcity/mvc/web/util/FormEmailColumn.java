package org.runcity.mvc.web.util;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormEmailColumn extends FormStringColumn {
	private static final Logger logger = Logger.getLogger(FormEmailColumn.class);
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	public FormEmailColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		if (value == null || !pattern.matcher(value).matches()) {
			logger.debug(getName() + " does not match email regexp");
			errors.rejectValue(getName(), "validation.invalidEmail");
		}
	}

	@Override
	public String getJsChecks() {
		return super.getJsChecks() + "email;";
	}
}

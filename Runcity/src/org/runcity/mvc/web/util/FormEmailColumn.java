package org.runcity.mvc.web.util;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.validation.Errors;

public class FormEmailColumn extends FormStringColumn {
	private static final Logger logger = Logger.getLogger(FormEmailColumn.class);
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	public FormEmailColumn(AbstractForm form, ColumnDefinition definition, String formName, boolean required, Integer maxLength) {
		super(form, definition, formName, required, null, maxLength);
	}

	public FormEmailColumn(AbstractForm form, ColumnDefinition definition, String formName, boolean required, Integer maxLength, String value) {
		super(form, definition, formName, required, null, maxLength, value);
	}

	@Override
	public void validate(Errors errors) {
		super.validate(errors);

		if (value == null || !pattern.matcher(value).matches()) {
			logger.debug(getName() + " does not match email regexp");
			errors.rejectValue(getName(), "js.invalidEmail");
		}
	}

	@Override
	public String getJsChecks() {
		return super.getJsChecks() + "email;";
	}
}

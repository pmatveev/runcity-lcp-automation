package org.runcity.mvc.web.util;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;

public class FormEmailColumn extends FormStringColumn {
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	public FormEmailColumn(Long id, ColumnDefinition definition, String formName, boolean required, Integer maxLength) {
		super(id, definition, formName, required, null, maxLength);
	}

	public FormEmailColumn(Long id, ColumnDefinition definition, String formName, boolean required, Integer maxLength, String value) {
		super(id, definition, formName, required, null, maxLength, value);
	}

	@Override
	public void validate(Errors errors) {
		super.validate(errors);

		if (!pattern.matcher(value).matches()) {
			errors.rejectValue(getName(), "js.invalidEmail");
		}
	}

	@Override
	public String getJsChecks() {
		return super.getJsChecks() + "email;";
	}
}

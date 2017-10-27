package org.runcity.mvc.web.util;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;

public class FormPasswordColumn extends FormStringColumn {
	private static final String PWD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";
	private Pattern pattern = Pattern.compile(PWD_PATTERN);

	public FormPasswordColumn(String name, boolean required) {
		super(name, required, null, null);
	}

	public FormPasswordColumn(String name, boolean required, String value) {
		super(name, required, null, null, value);
	}

	@Override
	public void validate(Errors errors) {
		super.validate(errors);
		
		if (!pattern.matcher(value).matches()) {
			errors.rejectValue(name, "js.passwordStrength");
		}
	}
}

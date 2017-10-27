package org.runcity.mvc.web.util;

import org.springframework.validation.Errors;

public class FormPlainStringColumn extends FormStringColumn {
	public FormPlainStringColumn(String name, boolean required, Integer minLength, Integer maxLength) {
		super(name, required, minLength, maxLength);
	}

	public FormPlainStringColumn(String name, boolean required, Integer minLength, Integer maxLength, String value) {
		super(name, required, minLength, maxLength, value);
	}
	
	@Override
	public void validate(Errors errors) {
		super.validate(errors);
	}
}

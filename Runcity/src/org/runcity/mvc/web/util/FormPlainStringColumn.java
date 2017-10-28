package org.runcity.mvc.web.util;

import org.springframework.validation.Errors;

public class FormPlainStringColumn extends FormStringColumn {
	public FormPlainStringColumn(Long id, ColumnDefinition definition, String formName, boolean required,
			Integer minLength, Integer maxLength) {
		super(id, definition, formName, required, minLength, maxLength);
	}

	public FormPlainStringColumn(Long id, ColumnDefinition definition, String formName, boolean required,
			Integer minLength, Integer maxLength, String value) {
		super(id, definition, formName, required, minLength, maxLength, value);
	}

	@Override
	public void validate(Errors errors) {
		super.validate(errors);
	}
}

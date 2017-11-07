package org.runcity.mvc.web.util;

import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.validation.Errors;

public class FormPlainStringColumn extends FormStringColumn {
	public FormPlainStringColumn(AbstractForm form, ColumnDefinition definition, String formName, boolean required,
			Integer minLength, Integer maxLength) {
		super(form, definition, formName, required, minLength, maxLength);
	}

	public FormPlainStringColumn(AbstractForm form, ColumnDefinition definition, String formName, boolean required,
			Integer minLength, Integer maxLength, String value) {
		super(form, definition, formName, required, minLength, maxLength, value);
	}

	@Override
	public void validate(Errors errors) {
		super.validate(errors);
	}
}

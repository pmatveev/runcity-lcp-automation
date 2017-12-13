package org.runcity.mvc.web.util;

import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormPlainStringColumn extends FormStringColumn {
	public FormPlainStringColumn(AbstractForm form, ColumnDefinition definition, boolean required, Integer minLength,
			Integer maxLength) {
		super(form, definition, required, minLength, maxLength);
	}

	public FormPlainStringColumn(AbstractForm form, ColumnDefinition definition, boolean required, Integer minLength,
			Integer maxLength, String value) {
		super(form, definition, required, minLength, maxLength, value);
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);
	}
}

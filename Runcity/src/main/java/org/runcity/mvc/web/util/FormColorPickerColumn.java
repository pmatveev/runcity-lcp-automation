package org.runcity.mvc.web.util;

import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormColorPickerColumn extends FormStringColumn {
	public FormColorPickerColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
		setMinLength(6);
		setMaxLength(6);
	}
	
	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);
	}
}

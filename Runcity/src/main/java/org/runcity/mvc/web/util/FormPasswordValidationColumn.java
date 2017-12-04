package org.runcity.mvc.web.util;

import org.runcity.mvc.web.formdata.AbstractForm;

public class FormPasswordValidationColumn extends FormStringColumn {

	public FormPasswordValidationColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition, true, null, null);
		this.passwordValue = true;
	}

	public FormPasswordValidationColumn(AbstractForm form, ColumnDefinition definition, String value) {
		super(form, definition, true, null, null, value);
		this.passwordValue = true;
	}
	
}

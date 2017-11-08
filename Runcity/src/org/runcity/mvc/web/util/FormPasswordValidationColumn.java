package org.runcity.mvc.web.util;

import org.runcity.mvc.web.formdata.AbstractForm;

public class FormPasswordValidationColumn extends FormStringColumn {

	public FormPasswordValidationColumn(AbstractForm form, ColumnDefinition definition, String formName) {
		super(form, definition, formName, true, null, null);
		this.passwordValue = true;
	}

	public FormPasswordValidationColumn(AbstractForm form, ColumnDefinition definition, String formName, String value) {
		super(form, definition, formName, true, null, null, value);
		this.passwordValue = true;
	}
	
}

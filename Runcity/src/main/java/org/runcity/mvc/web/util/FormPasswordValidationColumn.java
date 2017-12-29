package org.runcity.mvc.web.util;

import org.runcity.mvc.web.formdata.AbstractForm;

public class FormPasswordValidationColumn extends FormStringColumn {

	public FormPasswordValidationColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
		this.passwordValue = true;
		setRequired(true);
	}

}

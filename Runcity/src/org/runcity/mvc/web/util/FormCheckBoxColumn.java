package org.runcity.mvc.web.util;

import org.runcity.mvc.web.formdata.AbstractForm;

public class FormCheckBoxColumn extends FormColumn<Boolean> {
	public FormCheckBoxColumn(AbstractForm form, ColumnDefinition definition, String formName) {
		super(form, definition, formName);
	}

	public FormCheckBoxColumn(AbstractForm form, ColumnDefinition definition, String formName, Boolean value) {
		this(form, definition, formName);
		this.value = value;
	}
}

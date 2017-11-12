package org.runcity.mvc.web.util;

import org.runcity.mvc.web.formdata.AbstractForm;

public class FormCheckBoxColumn extends FormColumn<Boolean> {
	public FormCheckBoxColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
	}

	public FormCheckBoxColumn(AbstractForm form, ColumnDefinition definition, Boolean value) {
		this(form, definition);
		this.value = value;
	}
}

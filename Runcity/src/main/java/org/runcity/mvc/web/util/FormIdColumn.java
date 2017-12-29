package org.runcity.mvc.web.util;

import org.runcity.mvc.web.formdata.AbstractForm;

public class FormIdColumn extends FormColumn<Long> {
	public FormIdColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
	}
}

package org.runcity.mvc.web.util;

public class FormCheckBoxColumn extends FormColumn<Boolean> {
	public FormCheckBoxColumn(Long id, ColumnDefinition definition, String formName) {
		super(id, definition, formName);
	}

	public FormCheckBoxColumn(Long id, ColumnDefinition definition, String formName, Boolean value) {
		this(id, definition, formName);
		this.value = value;
	}
}

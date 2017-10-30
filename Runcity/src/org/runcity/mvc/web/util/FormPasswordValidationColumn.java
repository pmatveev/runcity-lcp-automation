package org.runcity.mvc.web.util;

public class FormPasswordValidationColumn extends FormStringColumn {

	public FormPasswordValidationColumn(Long id, ColumnDefinition definition, String formName) {
		super(id, definition, formName, true, null, null);
		this.passwordValue = true;
	}

	public FormPasswordValidationColumn(Long id, ColumnDefinition definition, String formName, String value) {
		super(id, definition, formName, true, null, null, value);
		this.passwordValue = true;
	}
	
}

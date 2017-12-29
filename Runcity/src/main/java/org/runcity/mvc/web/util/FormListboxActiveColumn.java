package org.runcity.mvc.web.util;

import org.runcity.mvc.web.formdata.AbstractForm;

public class FormListboxActiveColumn extends FormListboxColumn<Boolean> {	
	@Override
	protected void initDictionary() {
		options.put("true", "listbox.active");
		options.put("false", "listbox.locked");
	}
	
	public FormListboxActiveColumn() {
	}
	
	public FormListboxActiveColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition, false);
	}
}

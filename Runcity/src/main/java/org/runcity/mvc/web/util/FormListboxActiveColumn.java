package org.runcity.mvc.web.util;

import java.util.Map;

import org.runcity.mvc.web.formdata.AbstractForm;

public class FormListboxActiveColumn extends FormListboxColumn<Boolean> {	
	@Override
	protected void initDictionary() {
		options.put("true", "listbox.active");
		options.put("false", "listbox.locked");
	}
	
	public FormListboxActiveColumn() {
	}
	
	public FormListboxActiveColumn(AbstractForm form, ColumnDefinition definition, Boolean required) {
		super(form, definition, false, required);
	}

	public FormListboxActiveColumn(AbstractForm form, ColumnDefinition definition, Boolean required, Boolean value) {
		super(form, definition, required, value);
	}

	@Override
	public Map<String, String> getOptions() {
		return options;
	}

}

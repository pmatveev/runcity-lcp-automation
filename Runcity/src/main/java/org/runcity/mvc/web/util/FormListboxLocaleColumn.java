package org.runcity.mvc.web.util;

import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.DynamicLocaleList;

public class FormListboxLocaleColumn extends FormListboxColumn<String> {
	private DynamicLocaleList localeList;
	
	@Override
	protected void initDictionary() {
		if (localeList != null) {
			for (String s : localeList.locales()) {
				options.put(s, "locale." + s + "_long");
			}
		}
	}
	
	public FormListboxLocaleColumn() {
	}
	
	public FormListboxLocaleColumn(AbstractForm form, ColumnDefinition definition, DynamicLocaleList localeList, Boolean required) {
		super(form, definition, false, required);
		this.localeList = localeList;
		initDictionary();
	}
	
	public FormListboxLocaleColumn(AbstractForm form, ColumnDefinition definition, DynamicLocaleList localeList, Boolean required, String value) {
		super(form, definition, false, required, value);
		this.localeList = localeList;
		initDictionary();
	}
}

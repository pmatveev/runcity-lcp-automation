package org.runcity.mvc.web.util;

import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

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
	
	public FormListboxLocaleColumn(DynamicLocaleList localeList) {
		this.localeList = localeList;
		initDictionary();
	}
	
	public FormListboxLocaleColumn(AbstractForm form, ColumnDefinition definition, DynamicLocaleList localeList) {
		super(form, definition, false);
		this.localeList = localeList;
		initDictionary();
	}
	
	@Override
	public void validate(ApplicationContext context, Errors errors) {
		this.localeList = context.getBean(DynamicLocaleList.class);
		initDictionary();
		super.validate(context, errors);
	}
}

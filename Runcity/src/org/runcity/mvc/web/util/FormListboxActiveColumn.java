package org.runcity.mvc.web.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.MessageSource;

public class FormListboxActiveColumn extends FormListboxColumn<Boolean> {	
	private static Map<String, String> options = new HashMap<String, String>();
	
	static {
		options.put("true", "listbox.active");
		options.put("false", "listbox.locked");
	}
	
	public FormListboxActiveColumn(AbstractForm form, ColumnDefinition definition, Boolean required) {
		super(form, definition, false, required, options.size());
	}

	public FormListboxActiveColumn(AbstractForm form, ColumnDefinition definition, Boolean required, Boolean value) {
		super(form, definition, false, required, options.size(), value);
	}

	@Override
	public Map<String, String> getOptions() {
		return options;
	}

	public static String getOptionDisplay(String key, MessageSource messageSource, Locale l) {
		return messageSource.getMessage(options.get(key), null, l);
	}

}

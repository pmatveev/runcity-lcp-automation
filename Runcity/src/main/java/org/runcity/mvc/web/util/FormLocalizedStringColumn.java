package org.runcity.mvc.web.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.DynamicLocaleList;

public class FormLocalizedStringColumn extends FormColumn<Map<String, String>> {
	private static final Logger logger = Logger.getLogger(FormLocalizedStringColumn.class);

	protected boolean oneRequired;
	protected boolean allRequired;
	protected Integer minLength;
	protected Integer maxLength;

	public FormLocalizedStringColumn(AbstractForm form, ColumnDefinition definition, DynamicLocaleList localeList,
			boolean oneRequired, boolean allRequired, Integer minLength, Integer maxLength) {
		super(form, definition);
		
		this.oneRequired = oneRequired;
		this.allRequired = allRequired;
		this.minLength = minLength;
		this.maxLength = maxLength;
		if (localeList != null) {
			this.value = localeList.prepareMap();
		} else {
			this.value = new HashMap<String, String>();
		}
	}
	
	public FormLocalizedStringColumn(AbstractForm form, ColumnDefinition definition, DynamicLocaleList localeList,
			boolean oneRequired, boolean allRequired, Integer minLength, Integer maxLength, Map<String, String> value) {
		this(form, definition, localeList, oneRequired, allRequired, minLength, maxLength);
		setValue(value);
	}
	
	public String getGroupLabel() {
		return definition.getGroupLabel();
	}
	
	public Set<String> keySet() {
		return value.keySet();
	}
	
	public String get(Object key) {
		return value.get(key);
	}
	
	private void clearValue() {
		for (String s : value.keySet()) {
			value.put(s, "");
		}
	}
	
	@Override
	public void setValue(Map<String, String> value) {
		logger.trace("Setting value to " + getName() + ": " + getSafeValue());
		clearValue();
		for (String s : value.keySet()) {
			if (this.value.containsKey(s)) {
				this.value.put(s, value.get(s));
			}
		}
	}
}

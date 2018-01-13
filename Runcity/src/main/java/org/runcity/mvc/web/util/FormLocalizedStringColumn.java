package org.runcity.mvc.web.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormLocalizedStringColumn extends FormColumn<Map<String, String>> {
	private static final Logger logger = Logger.getLogger(FormLocalizedStringColumn.class);

	protected boolean longValue;
	protected boolean oneRequired;
	protected boolean allRequired;
	protected Integer minLength;
	protected Integer maxLength;

	public FormLocalizedStringColumn(AbstractForm form, ColumnDefinition definition, DynamicLocaleList localeList,
			boolean longValue, boolean oneRequired, boolean allRequired, Integer minLength, Integer maxLength) {
		super(form, definition);
		
		this.longValue = longValue;
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
	
	public String getGroupLabel() {
		return definition.getGroupLabel();
	}
	
	public Set<String> keySet() {
		return value.keySet();
	}
	
	public String get(Object key) {
		return value.get(key);
	}
	
	public void put(String k, String v) {
		value.put(k, v);
	}
	
	public void clearValue() {
		for (String s : value.keySet()) {
			value.put(s, "");
		}
	}
	
	@Override
	public void setValue(Map<String, String> value) {
		logger.trace("Setting value to " + getName() + ": " + getSafeValue());
		boolean empty = this.value.size() == 0;
		if (!empty) {
			clearValue();
		}
		for (String s : value.keySet()) {
			if (this.value.containsKey(s) || empty) {
				this.value.put(s, value.get(s));
			}
		}
	}
	
	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		int filled = 0;
		for (String s : value.keySet()) {
			String v = value.get(s);
			if (!StringUtils.isEmpty(v)) {
				if (minLength != null && v.length() < minLength) {
					logger.debug(getName() + " " + s + " expected minimum " + minLength + " chars long");
					errors.rejectValue(getName(), "validation.minLengthParm", new Object[] { minLength, s }, null);
				}
				if (maxLength != null && v.length() > maxLength) {
					logger.debug(getName() + " " + s + " expected maximum " + maxLength + " chars long");
					errors.rejectValue(getName(), "validation.maxLengthParm", new Object[] { maxLength, s }, null);
				}
				
				filled++;
			}
		}
		
		if (oneRequired && filled == 0) {
			logger.debug(getName() + " at least one value required");
			errors.rejectValue(getName(), "validation.oneRequired");
		}
		
		if (allRequired && filled < value.size()) {
			logger.debug(getName() + " all values required");
			errors.rejectValue(getName(), "validation.allRequired");			
		}
	}
	
	@Override
	public String getJsChecks() {
		StringBuilder sb = new StringBuilder();

		if (oneRequired) {
			sb.append("onerequired;");
		}
		
		if (allRequired) {
			sb.append("allrequired;");
		}

		if (minLength != null) {
			sb.append("min=").append(minLength).append(";");
		}

		if (maxLength != null) {
			sb.append("max=").append(maxLength).append(";");
		}

		return sb.toString();
	}

	public String getOnChange(String l) {
		return "onColChange($('#" + getHtmlId() + l + "'))";
	}
	
	public boolean isLongValue() {
		return longValue;
	}
}

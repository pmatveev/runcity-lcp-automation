package org.runcity.mvc.web.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;

public abstract class FormListboxColumn<T> extends FormColumn<T> {
	private static final Logger logger = Logger.getLogger(FormListboxColumn.class);
	protected final Map<String, String> options = new HashMap<String, String>();
	protected boolean multiple = false;
	protected boolean required = false;

	protected abstract void initDictionary();

	protected FormListboxColumn() {
		super(null, null);
		initDictionary();
	}

	protected FormListboxColumn(AbstractForm form, ColumnDefinition definition, boolean multiple) {
		super(form, definition);
		initDictionary();
		this.multiple = multiple;
	}

	public Map<String, String> getOptions() {
		return options;
	}

	public boolean isMultiple() {
		return multiple;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		if (required) {
			boolean failed = value == null;

			if (!failed && value instanceof String && StringUtils.isEmpty((String) value)) {
				failed = true;
			}

			if (!failed && value instanceof Collection && ((Collection<?>) value).isEmpty()) {
				failed = true;
			}

			if (failed) {
				logger.debug(getName() + " is required");
				errors.rejectValue(getName(), "validation.required");
			}
		}
		
		if (value instanceof Collection) {
			Iterator<?> i = ((Collection<?>) value).iterator();
			while (i.hasNext()) {
				Object o = i.next();
				if (!options.containsKey(o.toString())) {
					errors.rejectValue(getName(), "common.notFoundListbox", new Object[] {o.toString()}, null);
				}
			}
		} else {
			if (!StringUtils.isEmpty(value.toString()) && !options.containsKey(value.toString())) {
				errors.rejectValue(getName(), "common.notFoundListbox", new Object[] {value.toString()}, null);
			}			
		}
	}

	@Override
	public String getJsChecks() {
		return required ? "required" : "";
	}

	public String getOnChange() {
		return "onColChange($('#" + getHtmlId() + "'))";
	}

	public String getMultipleOptions() {
		return multiple ? "multiple" : "";
	}

	public boolean getLiveSearch() {
		return options.size() > 4;
	}

	public String getOptionDisplay(String key, MessageSource messageSource, Locale l) {
		return messageSource.getMessage(options.get(key), null, l);
	}

	public String getOptionDisplay(String key, ResourceBundle bundle) {
		return bundle.getString(options.get(key));
	}

	public List<Map.Entry<String, String>> renderOptions(ResourceBundle bundle) {
		Map<String, String> locOptions = new HashMap<String, String>();

		for (String key : options.keySet()) {
			locOptions.put(key, getOptionDisplay(key, bundle));
		}

		List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(locOptions.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		return list;
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}
}

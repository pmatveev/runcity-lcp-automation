package org.runcity.mvc.web.util;

import org.runcity.util.StringUtils;
import org.springframework.validation.Errors;

public abstract class FormStringColumn extends FormColumn<String> {
	protected boolean required;
	protected Integer minLength;
	protected Integer maxLength;

	protected FormStringColumn(String name, boolean required, Integer minLength, Integer maxLength) {
		super(name);
		this.required = required;
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	protected FormStringColumn(String name, boolean required, Integer minLength, Integer maxLength, String value) {
		this(name, required, minLength, maxLength);
		this.value = value;
	}
	
	public void validate(Errors errors) {
		if (StringUtils.isEmpty(value)) {
			if (required) {
				errors.rejectValue(name, "js.required");
			}
		} else {
			if (minLength != null && value.length() < minLength) {
				errors.rejectValue(name, "js.minLength", new Object[] { minLength }, null);
			}
			
			if (maxLength != null && value.length() > maxLength) {
				errors.rejectValue(name, "js.maxLength", new Object[] { maxLength }, null);
			}
		}
	}
}

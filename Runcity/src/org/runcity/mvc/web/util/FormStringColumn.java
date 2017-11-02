package org.runcity.mvc.web.util;

import org.apache.log4j.Logger;
import org.runcity.util.StringUtils;
import org.springframework.validation.Errors;

public abstract class FormStringColumn extends FormColumn<String> {
	private static final Logger logger = Logger.getLogger(FormStringColumn.class);
	
	protected boolean required;
	protected Integer minLength;
	protected Integer maxLength;

	protected FormStringColumn(Long id, ColumnDefinition definition, String formName, boolean required, Integer minLength, Integer maxLength) {
		super(id, definition, formName);
		this.required = required;
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	protected FormStringColumn(Long id, ColumnDefinition definition, String formName, boolean required, Integer minLength, Integer maxLength, String value) {
		this(id, definition, formName, required, minLength, maxLength);
		this.value = value;
	}
	
	public void validate(Errors errors) {
		super.validate(errors);
		
		if (StringUtils.isEmpty(value)) {
			if (required) {
				logger.debug(getName() + " is required");
				errors.rejectValue(getName(), "js.required");
			}
		} else {
			if (minLength != null && value.length() < minLength) {
				logger.debug(getName() + " expected minimum " + minLength + " chars long");
				errors.rejectValue(getName(), "js.minLength", new Object[] { minLength }, null);
			}
			
			if (maxLength != null && value.length() > maxLength) {
				logger.debug(getName() + " expected maximum " + maxLength + " chars long");
				errors.rejectValue(getName(), "js.maxLength", new Object[] { maxLength }, null);
			}
		}
	}
	
	public String getJsChecks() {
		StringBuilder sb = new StringBuilder();
		
		if (required) {
			sb.append("required;");
		}
		
		if (minLength != null) {
			sb.append("min=").append(minLength).append(";");
		}
		
		if (maxLength != null) {
			sb.append("max=").append(maxLength).append(";");
		}
		
		return sb.toString();
	}
	
	public String getOnChange() {
		return "checkInput($('#" + getHtmlId() + "'), translations)";
	}
}

package org.runcity.mvc.web.util;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public abstract class FormStringColumn extends FormColumn<String> {
	private static final Logger logger = Logger.getLogger(FormStringColumn.class);

	protected boolean longValue = false;
	protected boolean required = false;
	protected boolean hidden = false;
	protected Integer minLength = null;
	protected Integer maxLength = null;

	protected FormStringColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		if (StringUtils.isEmpty(value)) {
			if (required) {
				logger.debug(getName() + " is required");
				errors.rejectValue(getName(), "validation.required");
			}
		} else {
			if (minLength != null && value.length() < minLength) {
				logger.debug(getName() + " expected minimum " + minLength + " chars long");
				errors.rejectValue(getName(), "validation.minLength", new Object[] { minLength }, null);
			}

			if (maxLength != null && value.length() > maxLength) {
				logger.debug(getName() + " expected maximum " + maxLength + " chars long");
				errors.rejectValue(getName(), "validation.maxLength", new Object[] { maxLength }, null);
			}
		}
	}

	@Override
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
		return "onColChange($('#" + getHtmlId() + "'))";
	}
	
	public boolean isLongValue() {
		return longValue;
	}

	public void setLongValue(boolean longValue) {
		this.longValue = longValue;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}

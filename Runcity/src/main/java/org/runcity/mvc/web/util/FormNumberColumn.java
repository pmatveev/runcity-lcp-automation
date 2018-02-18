package org.runcity.mvc.web.util;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormNumberColumn extends FormColumn<Integer> {
	private static final Logger logger = Logger.getLogger(FormNumberColumn.class);
	
	protected boolean required = false;
	private Integer min = null;
	private Integer max = null;
	
	public FormNumberColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		if (value == null) {
			if (required) {
				logger.debug(getName() + " is required");
				errors.rejectValue(getName(), "validation.required");
			}
		} else {
			if (min != null && value < min) {
				logger.debug(getName() + " expected minimum " + min);
				errors.rejectValue(getName(), "validation.min", new Object[] { min }, null);
			}

			if (max != null && value > max) {
				logger.debug(getName() + " expected maximum " + max);
				errors.rejectValue(getName(), "validation.max", new Object[] { max }, null);
			}
		}
	}

	public String getOnChange() {
		return "onColChange($('#" + getHtmlId() + "'))";
	}
	
	@Override
	public String getJsChecks() {
		StringBuilder sb = new StringBuilder();

		if (required) {
			sb.append("required;");
		}

		if (min != null) {
			sb.append("minval=").append(min).append(";");
		}

		if (max != null) {
			sb.append("maxval=").append(max).append(";");
		}

		return sb.toString();
	}
	
	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
}

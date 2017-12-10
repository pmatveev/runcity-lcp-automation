package org.runcity.mvc.web.util;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.StringUtils;
import org.springframework.validation.Errors;

public abstract class FormDddwColumn<T> extends FormColumn<T> {
	private static final Logger logger = Logger.getLogger(FormDddwColumn.class);

	protected String ajaxSource;
	protected String[] ajaxParms;

	protected boolean multiple;
	protected boolean required;

	protected FormDddwColumn(AbstractForm form, ColumnDefinition definition, String ajaxSource, String[] ajaxParms,
			boolean multiple, boolean required) {
		super(form, definition);
		this.ajaxSource = ajaxSource;
		this.ajaxParms = ajaxParms;
		this.multiple = multiple;
		this.required = required;
	}

	public String getAjaxSource() {
		return ajaxSource;
	}

	public void setAjaxSource(String ajaxSource) {
		this.ajaxSource = ajaxSource;
	}

	public String[] getAjaxParms() {
		return ajaxParms;
	}

	public void setAjaxParms(String[] ajaxParms) {
		this.ajaxParms = ajaxParms;
	}

	public boolean isMultiple() {
		return multiple;
	}

	@Override
	public void validate(Errors errors) {
		super.validate(errors);

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
	}

	@Override
	public String getJsChecks() {
		return required ? "required" : "";
	}

	public String getOnChange() {
		return "checkInput($('#" + getHtmlId() + "'))";
	}

	public String getMultipleOptions() {
		return multiple ? "multiple" : "";
	}
}

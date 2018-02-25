package org.runcity.mvc.web.util;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public abstract class FormDddwColumn<T> extends FormColumn<T> {
	private static final Logger logger = Logger.getLogger(FormDddwColumn.class);

	protected ApplicationContext context;
	
	protected String initSource;
	protected FormColumn<?>[] initParms;
	protected String ajaxSource;
	protected FormColumn<?>[] ajaxParms;

	protected boolean multiple;
	protected boolean required;
	protected boolean forceRefresh = false;
	
	protected Object[] validation;

	protected FormDddwColumn(AbstractForm form, ColumnDefinition definition, String initSource, FormColumn<?>[] initParms, String ajaxSource, FormColumn<?>[] ajaxParms,
			boolean multiple, Object ... validation) {
		super(form, definition);
		this.initSource = initSource;
		this.initParms = initParms;
		this.ajaxSource = ajaxSource;
		this.ajaxParms = ajaxParms;
		this.multiple = multiple;
		this.validation = validation;
	}

	public String getAjaxSource() {
		return ajaxSource;
	}

	public void setAjaxSource(String ajaxSource) {
		this.ajaxSource = ajaxSource;
	}

	public String getInitSource() {
		return initSource;
	}

	public void setInitSource(String initSource) {
		this.initSource = initSource;
	}

	public FormColumn<?>[] getInitParms() {
		return initParms;
	}

	public void setInitParms(FormColumn<?>[] initParms) {
		this.initParms = initParms;
	}

	public FormColumn<?>[] getAjaxParms() {
		return ajaxParms;
	}

	public void setAjaxParms(FormColumn<?>[] ajaxParms) {
		this.ajaxParms = ajaxParms;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public boolean isForceRefresh() {
		return forceRefresh;
	}

	public void setForceRefresh(boolean forceRefresh) {
		this.forceRefresh = forceRefresh;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		this.context = context;
		
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
		return "onColChange($('#" + getHtmlId() + "'))";
	}

	public String getMultipleOptions() {
		return multiple ? "multiple" : "";
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isRequired() {
		return required;
	}
}

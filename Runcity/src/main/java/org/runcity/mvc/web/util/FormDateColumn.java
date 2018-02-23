package org.runcity.mvc.web.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormDateColumn extends FormColumn<Date> {
	private static final DateFormat dateFormat = new SimpleDateFormat(SpringRootConfig.DATE_FORMAT);
	private static final DateFormat dateTimeFormat = new SimpleDateFormat(SpringRootConfig.DATE_TIME_FORMAT);
	
	static {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	private static final Logger logger = Logger.getLogger(FormDateColumn.class);

	protected boolean required = false;
	protected boolean timeValue = false;
	
	public FormDateColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public void setTimeValue(boolean timeValue) {
		this.timeValue = timeValue;
	}
	
	public boolean isTimeValue() {
		return timeValue;
	}
	
	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		if (value == null && required) {
			logger.debug(getName() + " is required");
			errors.rejectValue(getName(), "validation.required");
		}
	}

	@Override
	public String getJsChecks() {
		StringBuilder sb = new StringBuilder();

		if (required) {
			sb.append("required;");
		}
		return sb.toString();
	}

	public String getOnChange() {
		return "onColChange($('#" + getHtmlId() + "'))";
	}
	
	public String getFormattedValue() {
		return timeValue ? dateTimeFormat.format(value) : dateFormat.format(value);
	}
}

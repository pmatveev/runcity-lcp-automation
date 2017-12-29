package org.runcity.mvc.web.util;

import java.util.Date;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormDateColumn extends FormColumn<Date> {
	private static final Logger logger = Logger.getLogger(FormDateColumn.class);

	protected boolean required;
	
	public FormDateColumn(AbstractForm form, ColumnDefinition definition, boolean required) {
		super(form, definition);
		this.required = required;
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
}

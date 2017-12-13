package org.runcity.mvc.web.util;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormPasswordConfirmationColumn extends FormStringColumn {
	private static final Logger logger = Logger.getLogger(FormPasswordConfirmationColumn.class);
	private FormStringColumn password;

	public FormPasswordConfirmationColumn(AbstractForm form, ColumnDefinition definition, boolean required) {
		super(form, definition, required, null, null);
		this.passwordValue = true;
	}

	public FormPasswordConfirmationColumn(AbstractForm form, ColumnDefinition definition, boolean required,
			String value) {
		super(form, definition, required, null, null, value);
		this.passwordValue = true;
	}

	public void setPassword(FormPasswordColumn password) {
		this.password = password;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		if (!StringUtils.isEqual(value, password.getValue())) {
			logger.debug(getName() + " is not equal to " + password.getName());
			errors.rejectValue(getName(), "validation.passwordMatch");
		}
	}

	@Override
	public String getOnChange() {
		return "checkPwdInput($('#" + password.getHtmlId() + "'), $('#" + getHtmlId() + "'))";
	}
}

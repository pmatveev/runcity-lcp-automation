package org.runcity.mvc.web.util;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormPasswordColumn extends FormStringColumn {
	private static final Logger logger = Logger.getLogger(FormPasswordColumn.class);
	
	private FormStringColumn passwordConfirmation;
	
	private static final String PWD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";
	private Pattern pattern = Pattern.compile(PWD_PATTERN);

	public FormPasswordColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
		this.passwordValue = true;
		setRequired(true);
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);
		
		if (!StringUtils.isEmpty(value) && !pattern.matcher(value).matches()) {
			logger.debug(getName() + " does not match password regexp");
			errors.rejectValue(getName(), "validation.passwordStrength");
		}
	}
	
	public void setPasswordConfirmation(FormPasswordConfirmationColumn passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	@Override
	public String getJsChecks() {
		return super.getJsChecks() + "pwd;";
	}
	
	@Override
	public String getOnChange() {
		return "checkPwdInput($('#" + getHtmlId() + "'), $('#" + passwordConfirmation.getHtmlId() + "'))";
	}
}

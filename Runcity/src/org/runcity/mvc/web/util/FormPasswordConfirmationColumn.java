package org.runcity.mvc.web.util;

import org.runcity.util.StringUtils;
import org.springframework.validation.Errors;

public class FormPasswordConfirmationColumn extends FormStringColumn {
	private FormStringColumn password;

	public FormPasswordConfirmationColumn(Long id, ColumnDefinition definition, String formName, boolean required,
			FormStringColumn password2) {
		super(id, definition, formName, required, null, null);
		this.password = password2;
	}

	public FormPasswordConfirmationColumn(Long id, ColumnDefinition definition, String formName, boolean required,
			FormPasswordColumn password, String value) {
		super(id, definition, formName, required, null, null, value);
		this.password = password;
	}

	@Override
	public void validate(Errors errors) {
		super.validate(errors);

		if (!StringUtils.isEqual(value, password.getValue())) {
			errors.rejectValue(getName(), "js.passwordMatch");
		}
	}	

	@Override
	public String getOnChange() {
		return "checkPwdInput('" + password.getHtmlId() + "', '" + getHtmlId() + "', translations)";
	}
}

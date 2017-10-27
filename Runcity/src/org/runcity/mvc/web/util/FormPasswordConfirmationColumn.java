package org.runcity.mvc.web.util;

import org.runcity.util.StringUtils;
import org.springframework.validation.Errors;

public class FormPasswordConfirmationColumn extends FormStringColumn {
	private FormStringColumn password;

	public FormPasswordConfirmationColumn(String name, boolean required, FormStringColumn password2) {
		super(name, required, null, null);
		this.password = password2;
	}

	public FormPasswordConfirmationColumn(String name, boolean required, FormPasswordColumn password, String value) {
		super(name, required, null, null, value);
		this.password = password;
	}

	@Override
	public void validate(Errors errors) {
		super.validate(errors);

		if (!StringUtils.isEqual(value, password.getValue())) {
			errors.rejectValue(name, "js.passwordMatch");
		}
	}
}

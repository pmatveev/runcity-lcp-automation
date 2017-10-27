package org.runcity.mvc.validator;

import org.runcity.mvc.web.util.ValidatedForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return ValidatedForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		((ValidatedForm) target).validate(errors);
	}

}

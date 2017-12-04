package org.runcity.mvc.validator;

import org.runcity.mvc.web.formdata.ValidatedForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormValidator implements Validator {
	@Autowired
	private ApplicationContext appContext;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return ValidatedForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		((ValidatedForm) target).validate(appContext, errors);
	}

}

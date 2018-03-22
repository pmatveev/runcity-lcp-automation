package org.runcity.mvc.rest;

import org.runcity.mvc.rest.util.RestPostResponseBody;
import org.runcity.mvc.validator.FormValidator;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.ResponseClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

public abstract class AbstractRestController {
	@Autowired
	protected FormValidator formValidator;

	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	protected DynamicLocaleList localeList;
	
	protected Errors validateForm(AbstractForm form, RestPostResponseBody result) {
		Errors errors = new BindException(form, form.getFormName());
		if (formValidator.supports(form.getClass())) {
			formValidator.validate(form, errors);
		}
		if (errors.hasErrors()) {
			result.setResponseClass(ResponseClass.ERROR);
			result.parseErrors(errors);
		}
		return errors;
	}
}

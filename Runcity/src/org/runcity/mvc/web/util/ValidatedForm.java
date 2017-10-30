package org.runcity.mvc.web.util;

import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public interface ValidatedForm {
	public void validate(ApplicationContext context, Errors errors);
}

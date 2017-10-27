package org.runcity.mvc.web.util;

import org.springframework.validation.Errors;

public interface ValidatedForm {
	public void validate(Errors errors);
}

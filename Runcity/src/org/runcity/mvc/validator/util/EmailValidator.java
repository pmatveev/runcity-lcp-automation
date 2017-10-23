package org.runcity.mvc.validator.util;

import java.util.regex.Pattern;

public class EmailValidator {
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	
	public boolean valid(String email) {
		return pattern.matcher(email).matches();
	}
}

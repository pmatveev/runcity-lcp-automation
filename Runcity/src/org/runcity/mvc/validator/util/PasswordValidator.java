package org.runcity.mvc.validator.util;

import java.util.regex.Pattern;

public class PasswordValidator {
	private static final String PWD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";
	private Pattern pattern = Pattern.compile(PWD_PATTERN);
	
	public boolean valid(String email) {
		return pattern.matcher(email).matches();
	}
}

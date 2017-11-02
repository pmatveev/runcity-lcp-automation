package org.runcity.mvc.web.util;

public class FormPasswordPair {
	private FormPasswordColumn password;
	private FormPasswordConfirmationColumn passwordConfirmation;

	public FormPasswordPair(FormPasswordColumn password, FormPasswordConfirmationColumn passwordConfirmation) {
		this.password = password;
		this.passwordConfirmation = passwordConfirmation;
		
		this.password.setPasswordConfirmation(passwordConfirmation);
		this.passwordConfirmation.setPassword(password);
	}

	public FormPasswordColumn getPassword() {
		return password;
	}

	public void setPassword(FormPasswordColumn password) {
		this.password = password;
	}

	public FormPasswordConfirmationColumn getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(FormPasswordConfirmationColumn passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
}

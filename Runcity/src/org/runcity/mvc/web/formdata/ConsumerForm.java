package org.runcity.mvc.web.formdata;

import org.runcity.mvc.web.util.*;
import org.springframework.validation.Errors;

public class ConsumerForm implements ValidatedForm {
	private FormStringColumn username;
	private FormStringColumn credentials;
	private FormStringColumn password;
	private FormStringColumn password2;
	private FormStringColumn email;

	public ConsumerForm() {
		this.username = new FormPlainStringColumn("username", true, 4, 32);
		this.credentials = new FormPlainStringColumn("credentials", true, 4, 32);
		this.password = new FormPasswordColumn("password", true);
		this.password2 = new FormPasswordConfirmationColumn("password2", true, this.password);
		this.email = new FormEmailColumn("email", true, 255);
	}

	public ConsumerForm(String username, String credentials, String password,
			String password2, String email) {
		this();
		this.username.setValue(username);
		this.credentials.setValue(credentials);
		this.password.setValue(password);
		this.password2.setValue(password2);
		this.email.setValue(email);
	}

	public FormStringColumn getPassword() {
		return password;
	}

	public void setPassword(FormPasswordColumn password) {
		this.password = password;
	}

	public FormStringColumn getPassword2() {
		return password2;
	}

	public void setPassword2(FormPasswordConfirmationColumn password2) {
		this.password2 = password2;
	}

	public FormStringColumn getUsername() {
		return username;
	}

	public void setUsername(FormPlainStringColumn username) {
		this.username = username;
	}

	public FormStringColumn getCredentials() {
		return credentials;
	}

	public void setCredentials(FormPlainStringColumn credentials) {
		this.credentials = credentials;
	}

	public FormStringColumn getEmail() {
		return email;
	}

	public void setEmail(FormEmailColumn email) {
		this.email = email;
	}

	@Override
	public void validate(Errors errors) {
		username.validate(errors);
		credentials.validate(errors);
		password.validate(errors);
		password2.validate(errors);
		email.validate(errors);
	}
}

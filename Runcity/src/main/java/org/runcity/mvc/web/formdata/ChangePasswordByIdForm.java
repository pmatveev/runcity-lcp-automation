package org.runcity.mvc.web.formdata;

import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormIdListColumn;
import org.runcity.mvc.web.util.FormPasswordColumn;
import org.runcity.mvc.web.util.FormPasswordConfirmationColumn;
import org.runcity.mvc.web.util.FormPasswordPair;
import org.runcity.mvc.web.util.FormStringColumn;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public class ChangePasswordByIdForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(ChangePasswordByIdForm.class);

	@JsonView(Views.Public.class) 
	private FormIdListColumn id;
	
	@JsonView(Views.Public.class)
	private FormStringColumn password;

	@JsonView(Views.Public.class)
	private FormStringColumn password2;

	public ChangePasswordByIdForm() {
		super("changePasswordByIdForm", null, null, "/api/v1/changePasswordById", null);
		logger.trace("Creating form " + getFormName());
		setTitle("changePassword.header");
		this.id = new FormIdListColumn(this, new ColumnDefinition("id", "id"));
		FormPasswordPair passwords = new FormPasswordPair(
				new FormPasswordColumn(this, new ColumnDefinition("password", "changePassword.password")),
				new FormPasswordConfirmationColumn(this, new ColumnDefinition("password2", "changePassword.password2")));

		this.password = passwords.getPassword();
		this.password2 = passwords.getPasswordConfirmation();
	}

	public ChangePasswordByIdForm(List<Long> id, String password, String password2) {
		this();
		setId(id);
		setPassword(password);
		setPassword2(password2);
	}

	public List<Long> getId() {
		return id.getValue();
	}

	public void setId(List<Long> id) {
		this.id.setValue(id);
	}

	public String getPassword() {
		return password.getValue();
	}

	public void setPassword(String password) {
		this.password.setValue(password);
	}

	public String getPassword2() {
		return password2.getValue();
	}

	public void setPassword2(String password2) {
		this.password2.setValue(password2);
	}

	public FormIdListColumn getIdColumn() {
		return id;
	}

	public FormStringColumn getPasswordColumn() {
		return password;
	}

	public FormStringColumn getPassword2Column() {
		return password2;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		id.validate(context, errors);
		password.validate(context, errors);
		password2.validate(context, errors);
	}
}

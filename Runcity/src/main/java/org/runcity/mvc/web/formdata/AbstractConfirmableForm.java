package org.runcity.mvc.web.formdata;

import org.runcity.util.DynamicLocaleList;

public abstract class AbstractConfirmableForm extends AbstractForm {
	private String confirmationToken;
	
	protected AbstractConfirmableForm(String formName, String urlOnOpenAjax, String urlOnSubmitAjax, DynamicLocaleList localeList) {
		super(formName, urlOnOpenAjax, urlOnSubmitAjax, localeList);
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}
}

package org.runcity.mvc.web.formdata;

import org.runcity.mvc.web.util.ValidatedForm;

public abstract class AbstractForm implements ValidatedForm {
	protected String formName;
	protected String formTitle;
	
	public String getTitle() {
		return formTitle;
	}

	public void setTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	public String getFormName() {
		return formName;
	}

	public String getHtmlId() {
		return formName;
	}

	public String getOnSubmit() {
		return "return validateForm('" + getHtmlId() + "', translations)";
	}

}

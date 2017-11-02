package org.runcity.mvc.web.formdata;

public abstract class AbstractForm implements ValidatedForm {
	protected String formName;
	protected String formTitle;
	
	protected AbstractForm(String formName) {
		this.formName = formName;
	}
	
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
		return "return validateForm($('#" + getHtmlId() + "'), translations)";
	}

	public String getOnModalSubmit() {
		return "submitModalForm($('#" + getHtmlId() + "'), translations)";
	}
}

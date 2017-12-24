package org.runcity.mvc.web.formdata;

import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.util.DynamicLocaleList;

public abstract class AbstractForm extends RestGetResponseBody implements ValidatedForm {
	protected DynamicLocaleList localeList;
	
	protected String formName;
	protected String formTitle;
	protected String urlOnOpenAjax;
	protected String urlOnSubmit;
	protected String urlOnSubmitAjax;

	protected AbstractForm(String formName, String urlOnOpenAjax, String urlOnSubmit, String urlOnSubmitAjax, DynamicLocaleList localeList) {
		super();
		this.formName = formName;
		this.urlOnOpenAjax = urlOnOpenAjax;
		this.urlOnSubmit = urlOnSubmit;
		this.urlOnSubmitAjax = urlOnSubmitAjax;
		this.localeList = localeList;
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
		return "return validateForm($('#" + getHtmlId() + "'), event)";
	}

	public String getOnModalSubmit() {
		return "return submitModalForm($('#" + getHtmlId() + "'), event)";
	}

	public String getUrlOnOpenAjax() {
		return urlOnOpenAjax;
	}

	public void setUrlOnOpenAjax(String urlOnOpenAjax) {
		this.urlOnOpenAjax = urlOnOpenAjax;
	}

	public String getUrlOnSubmit() {
		return urlOnSubmit;
	}

	public void setUrlOnSubmit(String urlOnSubmit) {
		this.urlOnSubmit = urlOnSubmit;
	}

	public String getUrlOnSubmitAjax() {
		return urlOnSubmitAjax;
	}

	public void setUrlOnSubmitAjax(String urlOnSubmitAjax) {
		this.urlOnSubmitAjax = urlOnSubmitAjax;
	}
}

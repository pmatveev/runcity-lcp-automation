package org.runcity.mvc.web.formdata;

import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.util.DynamicLocaleList;

public abstract class AbstractForm extends RestGetResponseBody implements ValidatedForm {
	protected DynamicLocaleList localeList;
	
	protected String id;
	protected String formName;
	protected String formTitle;
	protected String urlOnOpenAjax;
	protected String urlOnSubmit;

	protected AbstractForm(String formName, String urlOnOpenAjax, String urlOnSubmitAjax, DynamicLocaleList localeList) {
		super();
		this.id = formName;
		this.formName = formName;
		this.urlOnOpenAjax = urlOnOpenAjax;
		this.urlOnSubmit = urlOnSubmitAjax;
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
		return id;
	}

	public String getOnSubmit() {
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
	
	public void prefix(String referrer) {
		this.id = referrer + this.id;
	}
}

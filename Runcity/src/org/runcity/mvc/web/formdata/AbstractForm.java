package org.runcity.mvc.web.formdata;

import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormIdColumn;

public abstract class AbstractForm extends RestGetResponseBody implements ValidatedForm {
	protected FormIdColumn id = new FormIdColumn(this, new ColumnDefinition("id", "id"));
	protected String formName;
	protected String formTitle;
	protected String urlOnOpenAjax;
	protected String urlOnSubmit;
	protected String urlOnSubmitAjax;

	protected AbstractForm(String formName, String urlOnOpenAjax, String urlOnSubmit, String urlOnSubmitAjax) {
		super();
		this.formName = formName;
		this.urlOnOpenAjax = urlOnOpenAjax;
		this.urlOnSubmit = urlOnSubmit;
		this.urlOnSubmitAjax = urlOnSubmitAjax;
	}

	protected AbstractForm(Long id, String formName, String urlOnOpenAjax, String urlOnSubmit, String urlOnSubmitAjax) {
		this(formName, urlOnOpenAjax, urlOnSubmit, urlOnSubmitAjax);
		this.id.setValue(id);
	}

	public Long getId() {
		return id.getValue();
	}

	public void setId(Long id) {
		this.id.setValue(id);
	}
	
	public FormIdColumn getIdColumn() {
		return id;
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
		return "return validateForm($('#" + getHtmlId() + "'))";
	}

	public String getOnModalSubmit() {
		return "submitModalForm($('#" + getHtmlId() + "'))";
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

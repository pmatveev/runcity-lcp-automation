package org.runcity.mvc.web.formdata;

import org.runcity.util.DynamicLocaleList;

public abstract class AbstractLocalizedForm extends AbstractForm {
	protected DynamicLocaleList localeList;
	
	protected AbstractLocalizedForm(String formName, String urlOnOpenAjax, String urlOnSubmit, String urlOnSubmitAjax, DynamicLocaleList localeList) {
		super(formName, urlOnOpenAjax, urlOnSubmit, urlOnSubmitAjax);
		this.localeList = localeList;
	}
}

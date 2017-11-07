package org.runcity.mvc.web.util;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.StringUtils;
import org.springframework.validation.Errors;

public abstract class FormColumn<T> {
	private static final Logger logger = Logger.getLogger(FormColumn.class);
	
	protected AbstractForm form;
	protected ColumnDefinition definition;
	protected String formName;
	protected T value;
	protected boolean passwordValue = false;
	
	protected FormColumn(AbstractForm form, ColumnDefinition definition, String formName) {
		this.form = form;
		this.definition = definition;
		this.formName = formName;
	}
	
	public String getName() {
		return definition.getName();
	}
	
	public String getLabel() {
		return definition.getLabel();
	}
	
	public T getValue() {
		logger.trace("Reading value from " + getName() + ": " + value);
		return value;
	}

	public void setValue(T value) {
		logger.trace("Setting value to " + getName() + ": " + value);
		this.value = value;
	}
	
	public boolean isPasswordValue() {
		return passwordValue;
	}
	
	public String getSafeValue() {
		if (isPasswordValue()) {
			return "******";
		}
		return value.toString();
	}
	
	public String getHtmlId() {
		return StringUtils.concatNvl("_", form.getId(), formName, definition.getName());
	}
	
	public void validate(Errors errors) {
		logger.debug(getName() + "=" + getSafeValue());
	}
}

package org.runcity.mvc.web.util;

import org.apache.log4j.Logger;
import org.runcity.util.StringUtils;
import org.springframework.validation.Errors;

public abstract class FormColumn<T> {
	private static final Logger logger = Logger.getLogger(FormColumn.class);
	
	protected Long id;
	protected ColumnDefinition definition;
	protected String formName;
	protected T value;
	protected boolean passwordValue = false;
	
	protected FormColumn(Long id, ColumnDefinition definition, String formName) {
		this.id = id;
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
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	public boolean isPasswordValue() {
		return passwordValue;
	}
	
	public String getSafeValue() {
		if (isPasswordValue()) {
			return "******";
		}
		return getValue().toString();
	}
	
	public String getHtmlId() {
		return StringUtils.concatNvl("_", id, formName, definition.getName());
	}
	
	public void validate(Errors errors) {
		logger.debug(getName() + "=" + getSafeValue());
	}
}

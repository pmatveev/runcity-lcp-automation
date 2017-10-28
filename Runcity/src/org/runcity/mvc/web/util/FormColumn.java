package org.runcity.mvc.web.util;

import org.runcity.util.StringUtils;
import org.springframework.validation.Errors;

public abstract class FormColumn<T> {
	protected Long id;
	protected ColumnDefinition definition;
	protected String formName;
	protected T value;
	
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
	
	public String getHtmlId() {
		return StringUtils.concatNvl("_", id, formName, definition.getName());
	}
	
	public abstract void validate(Errors errors);
}

package org.runcity.mvc.web.util;

import org.springframework.validation.Errors;

public abstract class FormColumn<T> {
	protected T value;
	protected String name;
	
	protected FormColumn(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	
	public abstract void validate(Errors errors);
}

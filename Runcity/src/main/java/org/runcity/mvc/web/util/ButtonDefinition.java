package org.runcity.mvc.web.util;

public class ButtonDefinition {
	private String text;
	private String confirmation;
	private String className;
	private String action;
	private String extend;
	private String jsCondition;

	public ButtonDefinition() {
	}

	public ButtonDefinition(String text, String confirmation, String className, String action, String extend) {
		this.text = text;
		this.confirmation = confirmation;
		this.className = className;
		this.action = action;
		this.extend = extend;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getConfirmation() {
		return confirmation;
	}
	
	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public ButtonDefinition setJsCondition(String condition) {
		this.jsCondition = condition;
		return this;
	}
	
	public String getJsCondition() {
		return jsCondition;
	}
}

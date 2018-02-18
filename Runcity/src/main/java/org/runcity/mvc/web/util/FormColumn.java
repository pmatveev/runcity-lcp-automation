package org.runcity.mvc.web.util;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public abstract class FormColumn<T> {
	private static final Logger logger = Logger.getLogger(FormColumn.class);
	
	protected AbstractForm form;
	protected ColumnDefinition definition;
	protected T value;
	protected boolean passwordValue = false;
	
	// conditional fields
	protected String showCondition;
	
	protected FormColumn(AbstractForm form, ColumnDefinition definition) {
		this.form = form;
		this.definition = definition;
	}
	
	public String getName() {
		return definition.getName();
	}
	
	public String getLabel() {
		return MessageFormat.format(definition.getLabel(), definition.getSubstitution());
	}
	
	public T getValue() {
		logger.trace("Reading value from " + getName() + ": " + getSafeValue());
		return value;
	}

	public void setValue(T value) {
		logger.trace("Setting value to " + getName() + ": " + getSafeValue());
		this.value = value;
	}
	
	public boolean isPasswordValue() {
		return passwordValue;
	}
	
	public String getSafeValue() {
		if (value == null) {
			return "<<null>>";
		}
		if (isPasswordValue()) {
			return "******";
		}
		return value.toString();
	}
	
	public String getFormHtmlId() {
		return form.getHtmlId();
	}
	
	public String getHtmlId() {
		return StringUtils.concatNvl("_", form.getHtmlId(), definition.getName());
	}
	
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug(getName() + "=" + getSafeValue());
	}

	public String getJsChecks() {
		return "";
	}
	
	public void setShowCondition(String js, FormColumn<?> ... args) {
		showCondition = js;
		for (int i = 0; i < args.length; i++) {
			showCondition = showCondition.replace("{" + i + "}", "getData($('#" + args[i].getHtmlId() + "'))");
		}
	}
	
	public String getShowCondition() {
		return showCondition;
	}
	
	@Override
	public String toString() {
		return getHtmlId();
	}
}	
package org.runcity.mvc.web.util;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.StringUtils;
import org.springframework.validation.Errors;

public abstract class FormListboxColumn<T> extends FormColumn<T> {
	private static final Logger logger = Logger.getLogger(FormListboxColumn.class);
	private boolean multiple;
	private boolean required;
	private int length;

	protected FormListboxColumn(AbstractForm form, ColumnDefinition definition, boolean multiple, boolean required, int length) {
		super(form, definition);
		this.multiple = multiple;
		this.required = required;
		this.length = length;
	}

	protected FormListboxColumn(AbstractForm form, ColumnDefinition definition, boolean multiple, boolean required,
			int length, T value) {
		this(form, definition, multiple, required, length);
		this.value = value;
	}

	public abstract Map<String, String> getOptions();

	public boolean isMultiple() {
		return multiple;
	}

	public boolean isRequired() {
		return required;
	}

	@Override
	public void validate(Errors errors) {
		super.validate(errors);

		if (required) {
			boolean failed = value == null;
			
			if (!failed && value instanceof String && StringUtils.isEmpty((String) value)) {
				failed = true;
			}
			
			if (!failed && value instanceof Collection && ((Collection<?>) value).isEmpty()) {
				failed = true;
			}
			
			if (failed) {
				logger.debug(getName() + " is required");
				errors.rejectValue(getName(), "validation.required");
			}
		}
	}

	@Override
	public String getJsChecks() {
		return required ? "required" : "";
	}

	public String getOnChange() {
		return "checkInput($('#" + getHtmlId() + "'))";
	}
	
	public String getMultipleOptions() {
		return multiple ? "multiple" : "";
	}
	
	public boolean getLiveSearch() {
		return length > 4;
	}
}

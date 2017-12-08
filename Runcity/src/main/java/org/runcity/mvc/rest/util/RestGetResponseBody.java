package org.runcity.mvc.rest.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonView;

public class RestGetResponseBody {
	@JsonView(Views.Public.class)
	private RestResponseClass responseClass;
	
	@JsonView(Views.Public.class)
	private List<String> errors;
	
	protected MessageSource messageSource;
	protected Locale locale = LocaleContextHolder.getLocale();

	public RestGetResponseBody() {
		responseClass = RestResponseClass.INFO;
	}
	
	public RestGetResponseBody(MessageSource messageSource) {
		this();
		this.messageSource = messageSource;
	}
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public RestResponseClass getResponseClass() {
		return responseClass;
	}

	public void setResponseClass(RestResponseClass responseClass) {
		this.responseClass = responseClass;
	}
	
	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	public void addCommonError(String error, Object[] arguments) {
		if (this.errors == null) {
			this.errors = new LinkedList<String>();
		}
		this.errors.add(messageSource.getMessage(error, arguments, locale));
	}

	public void addCommonError(String error) {
		addCommonError(error, null);
	}

	public void addCommonError(ObjectError error) {
		addCommonError(error.getCode(), error.getArguments());
	}
}

package org.runcity.mvc.rest.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonView;

public class RestPostResponseBody {
	@JsonView(Views.Public.class)
	private RestResponseClass responseClass;

	@JsonView(Views.Public.class)
	private List<String> msg;

	@JsonView(Views.Public.class)
	private Map<String, List<String>> colErrors;

	private MessageSource messageSource;
	private Locale locale = LocaleContextHolder.getLocale();

	public RestPostResponseBody() {
		responseClass = RestResponseClass.INFO;
	}
	
	public RestPostResponseBody(MessageSource messageSource) {
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

	public List<String> getMsg() {
		return msg;
	}

	public void setMsg(List<String> msg) {
		this.msg = msg;
	}

	public Map<String, List<String>> getColErrors() {
		return colErrors;
	}

	public void setColErrors(Map<String, List<String>> colErrors) {
		this.colErrors = colErrors;
	}

	public void addCommonMsg(String msg, Object[] arguments) {
		if (this.msg == null) {
			this.msg = new LinkedList<String>();
		}
		this.msg.add(messageSource.getMessage(msg, arguments, locale));
	}

	public void addCommonMsg(String msg) {
		addCommonMsg(msg, null);
	}

	public void addCommonMsg(ObjectError msg) {
		addCommonMsg(msg.getCode(), msg.getArguments());
	}

	public void addColError(String col, String error, Object[] arguments) {
		if (this.colErrors == null) {
			this.colErrors = new HashMap<String, List<String>>();
		}

		List<String> e = this.colErrors.get(col);
		if (e == null) {
			e = new LinkedList<String>();
			this.colErrors.put(col, e);
		}

		e.add(messageSource.getMessage(error, arguments, locale));
	}

	public void addColError(String col, String error) {
		addColError(col, error, null);
	}
	
	public void addColError(FieldError error) {
		addColError(error.getField(), error.getCode(), error.getArguments());
	}

	public void parseErrors(Errors err) {
		for (ObjectError e : err.getAllErrors()) {
			if (e instanceof FieldError) {
				addColError((FieldError) e);
				continue;
			}

			if (e instanceof ObjectError) {
				addCommonMsg((ObjectError) e);
			}
		}
	}
}

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
	private List<String> msg;
	
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
	
	public List<String> getMsg() {
		return msg;
	}

	public void setMsg(List<String> msg) {
		this.msg = msg;
	}
	
	public void addCommonMsg(String msg, Object ... arguments) {
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
}

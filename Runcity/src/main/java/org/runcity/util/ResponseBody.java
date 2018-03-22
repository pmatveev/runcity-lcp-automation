package org.runcity.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonView;

public abstract class ResponseBody {
	@JsonView(Object.class)
	protected ResponseClass responseClass;
	
	@JsonView(Object.class)
	protected List<String> msg;
	
	protected MessageSource messageSource;
	protected Locale locale = LocaleContextHolder.getLocale();

	public ResponseBody() {
		responseClass = ResponseClass.INFO;
	}
	
	public ResponseBody(MessageSource messageSource) {
		this();
		this.messageSource = messageSource;
	}
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public MessageSource getMessageSource() {
		return messageSource;
	}
	
	public Locale getCurrentLocale() {
		return locale;
	}

	public ResponseClass getResponseClass() {
		return responseClass;
	}

	public void setResponseClass(ResponseClass responseClass) {
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

	public void addCommonMsg(ObjectError msg) {
		addCommonMsg(msg.getCode(), msg.getArguments());
	}
}

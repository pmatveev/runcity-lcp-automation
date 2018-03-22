package org.runcity.util;

import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonView;

public class ActionResponseBody extends ResponseBody {
	public ActionResponseBody() {
		super();
	}

	public ActionResponseBody(MessageSource messageSource) {
		super(messageSource);
	}

	@JsonView(Object.class)
	private String confirmationToken;

	public void confirm(String token, String message, Object ... arguments) {
		this.responseClass = ResponseClass.CONFIRMATION;
		addCommonMsg(message, arguments);
		this.confirmationToken = token;
	}
}

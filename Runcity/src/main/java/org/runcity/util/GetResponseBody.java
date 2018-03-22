package org.runcity.util;

import org.springframework.context.MessageSource;

public class GetResponseBody extends ResponseBody {
	public GetResponseBody() {
		super();
	}

	public GetResponseBody(MessageSource messageSource) {
		super(messageSource);
	}
}

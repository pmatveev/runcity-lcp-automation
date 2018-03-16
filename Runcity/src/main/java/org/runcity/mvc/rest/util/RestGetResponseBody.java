package org.runcity.mvc.rest.util;

import org.runcity.util.ResponseBody;
import org.springframework.context.MessageSource;

public class RestGetResponseBody extends ResponseBody {
	public RestGetResponseBody() {
		super();
	}
	
	public RestGetResponseBody(MessageSource messageSource) {
		super(messageSource);
	}
}

package org.runcity.mvc.rest.util;

import org.runcity.util.GetResponseBody;
import org.springframework.context.MessageSource;

public class RestGetResponseBody extends GetResponseBody {
	public RestGetResponseBody() {
		super();
	}
	
	public RestGetResponseBody(MessageSource messageSource) {
		super(messageSource);
	}
}

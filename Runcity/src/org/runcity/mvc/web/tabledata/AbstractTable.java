package org.runcity.mvc.web.tabledata;

import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.springframework.context.MessageSource;

public class AbstractTable extends RestGetResponseBody {
	protected AbstractTable(MessageSource messageSource) {
		super(messageSource);
	}
}

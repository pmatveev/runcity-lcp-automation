package org.runcity.mvc.rest.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonView;

public class RestGetDddwResponseBody<T> extends RestGetResponseBody {
	@JsonView(Views.Public.class)
	private Map<T, String> options = new HashMap<T, String>();

	public RestGetDddwResponseBody(MessageSource messageSource) {
		super(messageSource);
	}
	
	public Map<T, String> getOptions() {
		return options;
	}

	public void setOptions(Map<T, String> options) {
		if (options == null) {
			this.options = new HashMap<T, String>();
		} else {
			this.options = options;
		}
	}
	
	public void addOption(T key, String value) {
		this.options.put(key, value);
	}
}

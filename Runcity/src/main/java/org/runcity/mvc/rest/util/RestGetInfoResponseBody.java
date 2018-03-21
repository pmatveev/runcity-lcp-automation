package org.runcity.mvc.rest.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;

public class RestGetInfoResponseBody extends RestGetResponseBody {
	@JsonView(Views.Public.class)
	private Map<String, String> data = new HashMap<String, String>();

	public void addElement(String key, Object value) {
		data.put(key, value.toString());
	}
}

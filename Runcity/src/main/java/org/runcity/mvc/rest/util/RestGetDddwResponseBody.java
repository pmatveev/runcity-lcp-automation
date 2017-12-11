package org.runcity.mvc.rest.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonView;

public class RestGetDddwResponseBody<T> extends RestGetResponseBody {
	private boolean sorted = false;
	
	@JsonView(Views.Public.class)
	private List<Option> options = new ArrayList<Option>();

	protected class Option {
		@JsonView(Views.Public.class)
		private T key;
		
		@JsonView(Views.Public.class)
		private String value;
		
		public Option(T key, String value) {
			this.key = key;
			this.value = value;
		}

		public T getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}
	
	
	public RestGetDddwResponseBody(MessageSource messageSource) {
		super(messageSource);
	}
	
	public List<Option> getOptions() {
		if (!sorted) {
			Collections.sort(options, new Comparator<Option>() {
				public int compare(Option o1, Option o2) {
					return (o1.getValue()).compareTo(o2.getValue());
				}
			});
			sorted = true;
		}
		
		return options;
	}
	
	public void addOption(T key, String value) {
		this.options.add(new Option(key, value));
		sorted = false;
	}
}

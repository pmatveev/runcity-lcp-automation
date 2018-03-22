package org.runcity.mvc.rest.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.runcity.util.ActionResponseBody;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonView;

public class RestPostResponseBody extends ActionResponseBody {
	@JsonView(Views.Public.class)
	protected Map<String, List<String>> colErrors = new HashMap<String, List<String>>();

	public RestPostResponseBody() {
		super();
	}
	
	public RestPostResponseBody(MessageSource messageSource) {
		super(messageSource);
	}
	
	public Map<String, List<String>> getColErrors() {
		return colErrors;
	}

	public void setColErrors(Map<String, List<String>> colErrors) {
		this.colErrors = colErrors;
	}

	public void addColError(String col, String error, Object ... arguments) {
		if (this.colErrors == null) {
			this.colErrors = new HashMap<String, List<String>>();
		}

		List<String> e = this.colErrors.get(col);
		if (e == null) {
			e = new LinkedList<String>();
			this.colErrors.put(col, e);
		}

		e.add(messageSource.getMessage(error, arguments, locale));
	}

	public void addColError(String col, String error) {
		addColError(col, error, null);
	}
	
	public void addColError(FieldError error) {
		addColError(error.getField(), error.getCode(), error.getArguments());
	}

	public void parseErrors(Errors err) {
		for (ObjectError e : err.getAllErrors()) {
			if (e instanceof FieldError) {
				addColError((FieldError) e);
				continue;
			}

			if (e instanceof ObjectError) {
				addCommonMsg((ObjectError) e);
			}
		}
	}
}

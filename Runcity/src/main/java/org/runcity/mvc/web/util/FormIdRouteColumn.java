package org.runcity.mvc.web.util;

import org.runcity.db.entity.Route;
import org.runcity.db.service.GameService;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormIdRouteColumn extends FormIdColumn {
	private Route route;

	public FormIdRouteColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		GameService gameService = context.getBean(GameService.class);
		route = gameService.selectRouteById(value, false);

		if (route == null) {
			errors.reject("common.notFoundHiddenId", new Object[] { getLabel(), value }, null);
		}
	}
	
	public Route getRoute() {
		return route;
	}
}

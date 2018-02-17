package org.runcity.mvc.web.util;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.service.ControlPointService;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;

public class FormDddwControlPointColumn extends FormDddwColumn<Long> {
	private enum FetchType {
		BY_GAME_NOT_SELF, BY_ROUTE_UNUSED;
	}

	private ControlPoint controlPoint;
	private FetchType fetchType;

	private FormDddwControlPointColumn(AbstractForm form, ColumnDefinition definition, String[] initParms,
			String ajaxSource, String[] ajaxParms, FetchType fetchType, Object... validation) {
		super(form, definition, "/api/v1/dddw/controlPointId?id={0}", initParms, ajaxSource, ajaxParms, false,
				validation);
		this.fetchType = fetchType;
	}

	public static FormDddwControlPointColumn getMainByGameNotSelf(AbstractForm form, ColumnDefinition definition,
			FormIdColumn self, FormIdColumn gameCol) {
		return new FormDddwControlPointColumn(form, definition, null,
				"/api/v1/dddw/controlPointMainByGame?self={0}&game={1}",
				new String[] { self.getHtmlId(), gameCol.getHtmlId() }, FetchType.BY_GAME_NOT_SELF, self, gameCol);
	}

	public static FormDddwControlPointColumn getByRouteUnused(AbstractForm form, ColumnDefinition definition,
			FormIdColumn self, FormIdColumn route) {
		return new FormDddwControlPointColumn(form, definition, null,
				"/api/v1/dddw/controlPointByRoute?self={0}&route={1}",
				new String[] { self.getHtmlId(), route.getHtmlId() }, FetchType.BY_ROUTE_UNUSED, self, route);
	}

	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		if (value == null) {
			return;
		}

		ControlPointService controlPointService = context.getBean(ControlPointService.class);
		controlPoint = controlPointService.selectById(value, false);

		if (controlPoint == null) {
			errors.rejectValue(getName(), "common.notFoundId", new Object[] { value }, null);
			return;
		}

		switch (fetchType) {
		case BY_GAME_NOT_SELF:
			/*
			 * validation 
			 * 	0: self 
			 * 	1: game
			 */
			if (ObjectUtils.nullSafeEquals(((FormIdColumn) validation[0]).getValue(), controlPoint.getId())
					|| !ObjectUtils.nullSafeEquals(controlPoint.getGame().getId(),
							((FormIdColumn) validation[1]).getValue())) {
				errors.rejectValue(getName(), "common.notFoundId", new Object[] { value }, null);
			}
			break;
//		case BY_ROUTE_UNUSED:
			/*
			 * validation 
			 * 	0: self 
			 * 	1: route
			 */

			// TODO
		}
	}

	public ControlPoint getControlPoint() {
		return controlPoint;
	}
}

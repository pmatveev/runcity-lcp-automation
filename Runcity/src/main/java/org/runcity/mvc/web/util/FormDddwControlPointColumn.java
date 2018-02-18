package org.runcity.mvc.web.util;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.service.ControlPointService;
import org.runcity.db.service.RouteService;
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

	private FormDddwControlPointColumn(AbstractForm form, ColumnDefinition definition, FormColumn<?>[] initParms,
			String ajaxSource, FormColumn<?>[] ajaxParms, FetchType fetchType, Object... validation) {
		super(form, definition, "/api/v1/dddw/controlPointId?id={0}", initParms, ajaxSource, ajaxParms, false,
				validation);
		this.fetchType = fetchType;
	}

	public static FormDddwControlPointColumn getMainByGameNotSelf(AbstractForm form, ColumnDefinition definition,
			FormIdColumn self, FormIdColumn gameCol) {
		return new FormDddwControlPointColumn(form, definition, null,
				"/api/v1/dddw/controlPointMainByGame?self={0}&game={1}",
				new FormColumn<?>[] { self, gameCol }, FetchType.BY_GAME_NOT_SELF, self, gameCol);
	}

	public static FormDddwControlPointColumn getByRouteUnused(AbstractForm form, ColumnDefinition definition,
			FormIdColumn self, FormIdColumn route) {
		return new FormDddwControlPointColumn(form, definition, null,
				"/api/v1/dddw/controlPointByRoute?self={0}&route={1}",
				new FormColumn<?>[] { self, route }, FetchType.BY_ROUTE_UNUSED, self, route);
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
				return;
			}
			break;
		case BY_ROUTE_UNUSED:
			/*
			 * validation 
			 * 	0: routeItem 
			 * 	1: route
			 */

			RouteService routeService = context.getBean(RouteService.class);
			Route r = routeService.selectById(((FormIdColumn) validation[1]).getValue(), false);
			
			// by game
			if (!ObjectUtils.nullSafeEquals(controlPoint.getGame(), r.getGame())) {
				errors.rejectValue(getName(), "common.notFoundId", new Object[] { value }, null);	
				return;
			}
			
			// unused
			Long selfItem = ((FormIdColumn) validation[0]).getValue();
			boolean self = false;
			if (selfItem != null) {
				RouteItem ri = routeService.selectItemById(selfItem);
				if (ObjectUtils.nullSafeEquals(ri.getControlPoint(), controlPoint)) {
					self = true;
				}
			}
			
			if (!self && !controlPointService.selectByRouteNotUsed(r).contains(controlPoint)) {
				errors.rejectValue(getName(), "controlPoint.validation.alreadyUsed", new Object[] { value }, null);	
				return;				
			}
		}
	}

	public ControlPoint getControlPoint() {
		return controlPoint;
	}
}

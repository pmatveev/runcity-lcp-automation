package org.runcity.mvc.web.util;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.service.ControlPointService;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.ObjectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormDddwControlPointColumn extends FormDddwColumn<Long> {
	private enum FetchType {
		BY_GAME_NOT_SELF;
	}

	private ControlPoint controlPoint;
	private FetchType fetchType;

	// reference
	private FormIdColumn self;
	private FormIdColumn gameCol;

	private FormDddwControlPointColumn(AbstractForm form, ColumnDefinition definition, String[] initParms,
			String ajaxSource, String[] ajaxParms, FetchType fetchType) {
		super(form, definition, "/api/v1/dddw/controlPointId?id={0}", initParms, ajaxSource, ajaxParms, false);
		this.fetchType = fetchType;
	}

	public static FormDddwControlPointColumn getMainByGameNotSelf(AbstractForm form, ColumnDefinition definition,
			FormIdColumn self, FormIdColumn gameCol) {
		FormDddwControlPointColumn result = new FormDddwControlPointColumn(form, definition, null,
				"/api/v1/dddw/controlPointMainByGame?self={0}&game={1}",
				new String[] { self.getHtmlId(), gameCol.getHtmlId() }, FetchType.BY_GAME_NOT_SELF);
		result.self = self;
		result.gameCol = gameCol;
		return result;
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
			if (ObjectUtils.nullSafeEquals(self.getValue(), controlPoint.getId())
					|| !ObjectUtils.nullSafeEquals(controlPoint.getGame().getId(), gameCol.getValue())) {
				errors.rejectValue(getName(), "common.notFoundId", new Object[] { value }, null);
			}
			break;
		}
	}

	public ControlPoint getControlPoint() {
		return controlPoint;
	}
}

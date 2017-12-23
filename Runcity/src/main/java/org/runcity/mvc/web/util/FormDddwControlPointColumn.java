package org.runcity.mvc.web.util;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.service.ControlPointService;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormDddwControlPointColumn extends FormDddwColumn<Long> {
	private ControlPoint controlPoint;
	
	private FormDddwControlPointColumn(AbstractForm form, ColumnDefinition definition, String[] initParms, String ajaxSource,
			String[] ajaxParms, boolean required) {
		super(form, definition, "/api/v1/dddw/controlPointId?id={0}", initParms, ajaxSource, ajaxParms, false, required);
	}
	
	public static FormDddwControlPointColumn getMainByGame(AbstractForm form, ColumnDefinition definition, String gameCol, boolean required) {
		return new FormDddwControlPointColumn(form, definition, null, "/api/v1/dddw/controlPointMainByGame?game={0}", new String[] {gameCol}, required);
	}
	
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);
		
		ControlPointService controlPointService = context.getBean(ControlPointService.class);
		controlPoint = controlPointService.selectById(value, false);
		
		if (value != null && controlPoint == null) {
			errors.rejectValue(getName(), "common.notFoundId", new Object[] {value}, null);
		}
	}
	
	public ControlPoint getControlPoint() {
		return controlPoint;
	}
}

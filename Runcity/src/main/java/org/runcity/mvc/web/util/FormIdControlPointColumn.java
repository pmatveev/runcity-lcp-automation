package org.runcity.mvc.web.util;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.service.ControlPointService;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormIdControlPointColumn extends FormIdColumn {
	private ControlPoint controlPoint;
	private boolean mainOnly = false;

	public FormIdControlPointColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		ControlPointService controlPointService = context.getBean(ControlPointService.class);
		controlPoint = controlPointService.selectById(value, false);
		
		if (mainOnly && controlPoint.getParent() != null) {
			controlPoint = null;
		}

		if (controlPoint == null) {
			errors.reject("common.notFoundHiddenId", new Object[] { getLabel(), value }, null);
		}
	}

	public void setMainOnly(boolean mainOnly) {
		this.mainOnly = mainOnly;
	}
	
	public ControlPoint getControlPoint() {
		return controlPoint;
	}
}

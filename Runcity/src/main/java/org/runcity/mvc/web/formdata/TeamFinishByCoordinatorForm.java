package org.runcity.mvc.web.formdata;

import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class TeamFinishByCoordinatorForm extends TeamProcessAbstractForm {
	public TeamFinishByCoordinatorForm() {
		this(null);
	}

	public TeamFinishByCoordinatorForm(DynamicLocaleList localeList) {
		this("teamFinishByCoordinatorForm", "/api/v1/coordinator/teamFinish", "coordinator.teamFinish", localeList);
	}

	private TeamFinishByCoordinatorForm(String formName, String urlOnSubmitAjax, String title, DynamicLocaleList localeList) {
		super(formName, urlOnSubmitAjax, title);
	}
	
	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);
		
		if (volunteer.getControlPoint() != null) {
			errors.reject("teamProcessing.validation.notCoordinator");
		}
	}
}

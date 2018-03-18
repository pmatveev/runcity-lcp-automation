package org.runcity.mvc.web.formdata;

import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class TeamDisqualifyByCoordinatorForm extends TeamProcessAbstractForm {
	public TeamDisqualifyByCoordinatorForm() {
		this(null);
	}

	public TeamDisqualifyByCoordinatorForm(DynamicLocaleList localeList) {
		this("teamDisqualifyByCoordinatorForm", "/api/v1/coordinator/team/disqualify", "coordinator.teamDisqualify", localeList);
	}

	private TeamDisqualifyByCoordinatorForm(String formName, String urlOnSubmitAjax, String title, DynamicLocaleList localeList) {
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

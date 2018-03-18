package org.runcity.mvc.web.formdata;

import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class TeamNotStartedByCoordinatorForm extends TeamProcessAbstractForm {
	public TeamNotStartedByCoordinatorForm() {
		this(null);
	}

	public TeamNotStartedByCoordinatorForm(DynamicLocaleList localeList) {
		this("teamNotStartedByCoordinatorForm", "/api/v1/coordinator/team/notStarted", "coordinator.teamNotStart", localeList);
	}

	private TeamNotStartedByCoordinatorForm(String formName, String urlOnSubmitAjax, String title, DynamicLocaleList localeList) {
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

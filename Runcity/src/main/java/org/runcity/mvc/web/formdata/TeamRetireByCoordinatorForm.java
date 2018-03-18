package org.runcity.mvc.web.formdata;

import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class TeamRetireByCoordinatorForm extends TeamProcessAbstractForm {
	public TeamRetireByCoordinatorForm() {
		this(null);
	}

	public TeamRetireByCoordinatorForm(DynamicLocaleList localeList) {
		this("teamRetireByCoordinatorForm", "/api/v1/coordinator/team/retire", "coordinator.teamRetire", localeList);
	}

	private TeamRetireByCoordinatorForm(String formName, String urlOnSubmitAjax, String title, DynamicLocaleList localeList) {
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

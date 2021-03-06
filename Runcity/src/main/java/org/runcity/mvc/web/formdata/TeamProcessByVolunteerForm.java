package org.runcity.mvc.web.formdata;

import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class TeamProcessByVolunteerForm extends TeamProcessAbstractForm {
	public TeamProcessByVolunteerForm() {
		this(null);
	}

	public TeamProcessByVolunteerForm(DynamicLocaleList localeList) {
		this("teamProcessByVolunteerForm", "/api/v1/volunteer/teamProcess", null, localeList);
		this.volunteerId.setValidateActive(true);
	}

	private TeamProcessByVolunteerForm(String formName, String urlOnSubmitAjax, String title, DynamicLocaleList localeList) {
		super(formName, urlOnSubmitAjax, title);
	}
	
	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		if (volunteer.getControlPoint() == null) {
			errors.reject("teamProcessing.validation.noCP");
		}
	}
}

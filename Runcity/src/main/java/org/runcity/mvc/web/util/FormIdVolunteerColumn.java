package org.runcity.mvc.web.util;

import org.runcity.db.entity.Volunteer;
import org.runcity.db.service.VolunteerService;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.secure.SecureUserDetails;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormIdVolunteerColumn extends FormIdColumn {
	private boolean validateUser = false;
	private Volunteer volunteer;

	public FormIdVolunteerColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
	}
	
	public void setValidateUser(boolean validateUser) {
		this.validateUser = validateUser;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		VolunteerService routeService = context.getBean(VolunteerService.class);
		volunteer = routeService.selectById(value, Volunteer.SelectMode.NONE);

		if (volunteer == null) {
			errors.reject("common.notFoundHiddenId", new Object[] { getLabel(), value }, null);
			return;
		}
		
		if (validateUser) {
			if (!StringUtils.isEqual(SecureUserDetails.getCurrentUser().getUsername(), volunteer.getConsumer().getUsername())) {
				errors.reject("common.invalidUser");
			}
		}
	}
	
	public Volunteer getVolunteer() {
		return volunteer;
	}
}

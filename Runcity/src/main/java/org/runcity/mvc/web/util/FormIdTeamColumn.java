package org.runcity.mvc.web.util;

import org.runcity.db.entity.Team;
import org.runcity.db.service.TeamService;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormIdTeamColumn extends FormIdColumn {
	private Team team;

	public FormIdTeamColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		TeamService teamService = context.getBean(TeamService.class);
		team = teamService.selectById(value, Team.SelectMode.NONE);

		if (team == null) {
			errors.reject("common.notFoundHiddenId", new Object[] { getLabel(), value }, null);
			return;
		}
	}
	
	public Team getTeam() {
		return team;
	}
}

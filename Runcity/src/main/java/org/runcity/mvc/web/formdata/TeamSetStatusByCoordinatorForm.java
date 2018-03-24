package org.runcity.mvc.web.formdata;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Team;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.enumeration.TeamStatus;
import org.runcity.db.service.RouteService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormIdTeamColumn;
import org.runcity.mvc.web.util.FormIdVolunteerColumn;
import org.runcity.mvc.web.util.FormListboxTeamStatusColumn;
import org.runcity.mvc.web.util.FormNumberColumn;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public class TeamSetStatusByCoordinatorForm extends AbstractConfirmableForm {
	private static final Logger logger = Logger.getLogger(TeamSetStatusByCoordinatorForm.class);

	@JsonView(Views.Public.class)
	protected FormIdVolunteerColumn volunteerId;

	@JsonView(Views.Public.class)
	protected FormIdTeamColumn teamId;

	@JsonView(Views.Public.class)
	protected FormListboxTeamStatusColumn status;

	@JsonView(Views.Public.class)
	protected FormNumberColumn leg;

	protected Volunteer volunteer;
	protected Team team;

	public TeamSetStatusByCoordinatorForm() {
		this(null);
	}

	public TeamSetStatusByCoordinatorForm(DynamicLocaleList localeList) {
		super("teamSetStatusByCoordinatorForm", null, "/api/v1/coordinator/team/setStatus", localeList);
		logger.trace("Creating form " + getFormName());
		setTitle("coordinator.setTeamStatus");

		this.volunteerId = new FormIdVolunteerColumn(this, new ColumnDefinition("volunteerId", "volunteerId"));
		this.volunteerId.setValidateUser(true);
		
		this.teamId = new FormIdTeamColumn(this, new ColumnDefinition("teamId", "teamId"));
		
		this.status = new FormListboxTeamStatusColumn(this, new ColumnDefinition("status", "team.status"));
		this.status.setRequired(true);
		
		this.leg = new FormNumberColumn(this, new ColumnDefinition("leg", "team.leg"));
		this.leg.setRequired(true);
		this.leg.setMin(1);
		this.leg.setShowCondition("{0} == '" + TeamStatus.getStoredValue(TeamStatus.ACTIVE) + "'", this.status);
	}

	public Long getVolunteerId() {
		return volunteerId.getValue();
	}

	public void setVolunteerId(Long volunteerId) {
		this.volunteerId.setValue(volunteerId);
	}

	public Long getTeamId() {
		return teamId.getValue();
	}

	public void setTeamId(Long teamId) {
		this.teamId.setValue(teamId);
	}

	public String getStatus() {
		return status.getValue();
	}

	public void setStatus(String status) {
		this.status.setValue(status);
	}

	public Integer getLeg() {
		return leg.getValue();
	}

	public void setLeg(Integer leg) {
		this.leg.setValue(leg);
	}

	public FormIdVolunteerColumn getVolunteerIdColumn() {
		return volunteerId;
	}

	public FormIdTeamColumn getTeamIdColumn() {
		return teamId;
	}

	public FormListboxTeamStatusColumn getStatusColumn() {
		return status;
	}

	public FormNumberColumn getLegColumn() {
		return leg;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());

		volunteerId.validate(context, errors);
		teamId.validate(context, errors);
		status.validate(context, errors);
		
		if (TeamStatus.getByStoredValue(status.getValue()) == TeamStatus.ACTIVE) {
			leg.validate(context, errors);
		} else {
			leg.setValue(null);
		}
		
		if (!errors.hasErrors()) {
			volunteer = volunteerId.getVolunteer();
			team = teamId.getTeam();
			
			if (volunteer.getControlPoint() != null || !ObjectUtils.nullSafeEquals(team.getRoute().getGame(), volunteer.getVolunteerGame())) {
				errors.reject("teamProcessing.validation.notCoordinator");
				return;
			}

			if (TeamStatus.getByStoredValue(status.getValue()) == TeamStatus.ACTIVE) {
				RouteService routeService = context.getBean(RouteService.class);
				Long maxLeg = routeService.selectMaxLeg(team.getRoute());
				if (leg.getValue() > maxLeg) {
					errors.rejectValue(leg.getName(), "validation.max", new Object[] { maxLeg }, null);
					return;
				}
			}
		}
	}
	
	public Team getTeam() {
		return team;
	}
	
	public Volunteer getVolunteer() {
		return volunteer;
	}
}

package org.runcity.mvc.web.formdata;

import org.apache.log4j.Logger;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.Team;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.util.TeamRouteItem;
import org.runcity.db.service.TeamService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormIdVolunteerColumn;
import org.runcity.mvc.web.util.FormPlainStringColumn;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public abstract class TeamProcessAbstractForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(TeamProcessAbstractForm.class);

	@JsonView(Views.Public.class)
	protected FormIdVolunteerColumn volunteerId;

	@JsonView(Views.Public.class)
	protected FormPlainStringColumn number;
	
	protected Volunteer volunteer;
	protected Team team;
	protected RouteItem routeItem;

	public TeamProcessAbstractForm(String formName, String urlOnSubmitAjax, String title) {
		this(formName, urlOnSubmitAjax, title, null);
	}

	public TeamProcessAbstractForm(String formName, String urlOnSubmitAjax, String title, DynamicLocaleList localeList) {
		super(formName, null, urlOnSubmitAjax, localeList);
		logger.trace("Creating form " + getFormName());
		setTitle(title);

		this.volunteerId = new FormIdVolunteerColumn(this, new ColumnDefinition("volunteerId", "volunteerId"));
		this.number = new FormPlainStringColumn(this, new ColumnDefinition("number", "team.number"));
		this.number.setRequired(true);
		this.number.setMaxLength(32);
	}

	public Long getVolunteerId() {
		return volunteerId.getValue();
	}

	public void setVolunteerId(Long volunteerId) {
		this.volunteerId.setValue(volunteerId);
	}

	public String getNumber() {
		return number.getValue();
	}

	public void setNumber(String number) {
		this.number.setValue(number);
	}

	public FormIdVolunteerColumn getVolunteerIdColumn() {
		return volunteerId;
	}

	public FormPlainStringColumn getNumberColumn() {
		return number;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());

		volunteerId.validate(context, errors);
		number.validate(context, errors);
		
		volunteer = volunteerId.getVolunteer();
		if (volunteer != null) {
			TeamService teamService = context.getBean(TeamService.class);
			if (volunteer.getControlPoint() != null) {
				TeamRouteItem tr = teamService.selectByNumberCP(getNumber(), volunteer.getControlPoint(), Team.SelectMode.NONE);
				
				if (tr == null || tr.getTeam() == null || tr.getRouteItem() == null) {
					errors.reject("teamProcessing.validation.noTeam");
					return;
				}
				
				team = tr.getTeam();
				routeItem = tr.getRouteItem();
			} else {
				team = teamService.selectByNumberGame(getNumber(), volunteer.getVolunteerGame(), Team.SelectMode.NONE);
				
				if (team == null) {
					errors.reject("teamProcessing.validation.noTeam");
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
	
	public RouteItem getRouteItem() {
		return routeItem;
	}
}

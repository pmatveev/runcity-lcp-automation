package org.runcity.mvc.rest;

import org.apache.log4j.Logger;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Event;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.Team;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.service.ControlPointService;
import org.runcity.db.service.RouteService;
import org.runcity.db.service.TeamService;
import org.runcity.db.service.VolunteerService;
import org.runcity.exception.DBException;
import org.runcity.mvc.rest.util.RestGetInfoResponseBody;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.RestPostResponseBody;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.TeamProcessByVolunteerForm;
import org.runcity.mvc.web.tabledata.TeamEventTable;
import org.runcity.mvc.web.tabledata.VolunteerTeamTable;
import org.runcity.secure.SecureUserDetails;
import org.runcity.util.ResponseBody;
import org.runcity.util.ResponseClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class RestRuntimeController extends AbstractRestController {
	private static final Logger logger = Logger.getLogger(RestRuntimeController.class);

	@Autowired
	private VolunteerService volunteerService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private ControlPointService controlPointService;

	@Autowired
	private RouteService routeService;

	private boolean checkVolunteer(Volunteer v) {
		return ObjectUtils.nullSafeEquals(v.getConsumer().getUsername(),
				SecureUserDetails.getCurrentUser().getUsername());
	}

	private ControlPoint getAllowedControlPoint(Long controlPointId, ControlPoint.SelectMode selectMode,
			boolean requireActive, ResponseBody response) {
		ControlPoint controlPoint = controlPointService.selectById(controlPointId, selectMode);

		if (controlPoint == null) {
			response.setResponseClass(ResponseClass.ERROR);
			response.addCommonMsg("common.invalidRequest");
			return null;
		}

		String username = SecureUserDetails.getCurrentUser().getUsername();
		Volunteer v = volunteerService.selectByControlPointAndUsername(controlPoint, username, requireActive,
				Volunteer.SelectMode.NONE);
		if (v == null) {
			v = volunteerService.selectCoordinatorByUsername(controlPoint.getGame(), username,
					Volunteer.SelectMode.NONE);
			if (v == null) {
				response.setResponseClass(ResponseClass.ERROR);
				response.addCommonMsg("common.forbidden");
				return null;
			}
		}
		return controlPoint;
	}

	public static class OnsiteRequestBody {
		@JsonView(Views.Public.class)
		private Long volunteer;

		@JsonView(Views.Public.class)
		private Boolean onsite;

		public OnsiteRequestBody() {
		}

		public Long getVolunteer() {
			return volunteer;
		}

		public void setVolunteer(Long volunteer) {
			this.volunteer = volunteer;
		}

		public Boolean getOnsite() {
			return onsite;
		}

		public void setOnsite(Boolean onsite) {
			this.onsite = onsite;
		}
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/volunteer/onsite", method = RequestMethod.POST)
	public RestPostResponseBody onsite(@RequestBody OnsiteRequestBody request) {
		logger.info("POST /api/v1/volunteer/onsite");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);

		if (request.getVolunteer() == null || request.getOnsite() == null) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.invalidRequest");
			return result;
		}

		Volunteer v = volunteerService.selectById(request.getVolunteer(), Volunteer.SelectMode.WITH_ACTIVE);

		if (v == null || !checkVolunteer(v)) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("volunteer.volunteerNotFound");
			return result;
		}

		if (ObjectUtils.nullSafeEquals(request.getOnsite(), v.getActive())) {
			// no action needed
			return result;
		}

		try {
			volunteerService.setCurrent(v, request.getOnsite());
		} catch (DBException e) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.db.fail");
			logger.error("DB exception", e);
			return result;
		}

		return result;
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/volunteer/teamProcess", method = RequestMethod.POST)
	public RestPostResponseBody processTeam(@RequestBody TeamProcessByVolunteerForm form) {
		logger.info("POST /api/v1/volunteer/teamProcess");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}

		if (!checkVolunteer(form.getVolunteer())) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("volunteer.volunteerNotFound");
			return result;
		}

		try {
			teamService.processTeam(form.getTeam(), form.getConfirmationToken(), form.getRouteItem(),
					form.getVolunteer(), result);
		} catch (DBException e) {
			logger.error(e);
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.popupProcessError");
		}

		if (result.getResponseClass() == ResponseClass.INFO) {
			result.addCommonMsg("common.completed");
		}
		
		return result;
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/controlPoint/{cpId}/stat", method = RequestMethod.GET)
	public RestGetInfoResponseBody getStat(@PathVariable Long cpId) {
		RestGetInfoResponseBody result = new RestGetInfoResponseBody();
		ControlPoint cp = getAllowedControlPoint(cpId, ControlPoint.SelectMode.WITH_CHILDREN_AND_ITEMS, false, result);

		if (cp == null) {
			return result;
		}

		cp = cp.getMain();

		Long total = 0L;
		for (RouteItem ri : cp.getRouteItems()) {
			Long stat = teamService.selectActiveNumberByRouteItem(ri);
			total += stat;
			result.addElement("routeCounter_" + ri.getId(), stat);
		}

		for (ControlPoint ch : cp.getChildren()) {
			for (RouteItem ri : ch.getRouteItems()) {
				Long stat = teamService.selectActiveNumberByRouteItem(ri);
				total += stat;
				result.addElement("routeCounter_" + ri.getId(), stat);
			}
		}

		result.addElement("routeCounter_total", total);

		return result;
	}

	@JsonView(VolunteerTeamTable.ForCP.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/controlPoint/{cpId}/teamTable", method = RequestMethod.GET)
	public RestGetResponseBody getTeamsByCP(@PathVariable Long cpId) {
		VolunteerTeamTable result = VolunteerTeamTable.initRestResponse(messageSource);

		ControlPoint cp = getAllowedControlPoint(cpId, ControlPoint.SelectMode.NONE, false, result);
		if (cp == null) {
			return result;
		}

		result.add(teamService.selectPendingTeamsByCP(cpId, Team.SelectMode.NONE));
		return result.validate();
	}

	@JsonView(VolunteerTeamTable.ForCP.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/routeItem/{routeItemId}/teamTable", method = RequestMethod.GET)
	public RestGetResponseBody getTeamsByRouteItem(@PathVariable Long routeItemId) {
		VolunteerTeamTable result = VolunteerTeamTable.initRestResponse(messageSource);
		RouteItem ri = routeService.selectItemById(routeItemId);

		if (ri == null) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.invalidRequest");
			return result;
		}

		ControlPoint cp = getAllowedControlPoint(ri.getControlPoint().getId(), ControlPoint.SelectMode.NONE, false,
				result);
		if (cp == null) {
			return result;
		}

		result.add(teamService.selectPendingTeamsByRouteItem(ri, Team.SelectMode.NONE));
		return result.validate();
	}

	@JsonView(TeamEventTable.ForCP.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/controlPoint/{cpId}/history", method = RequestMethod.GET)
	public RestGetResponseBody getCpHistory(@PathVariable Long cpId) {
		TeamEventTable result = TeamEventTable.initRestResponse(messageSource);

		ControlPoint cp = getAllowedControlPoint(cpId, ControlPoint.SelectMode.NONE, true, result);
		if (cp == null) {
			return result;
		}

		result.add(teamService.selectTeamEvents(cp, Event.SelectMode.NONE));
		return result.validate();
	}

	@JsonView(TeamEventTable.ForTeam.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/team/{teamId}/history", method = RequestMethod.GET)
	public RestGetResponseBody getTeamHistory(@PathVariable Long teamId) {
		TeamEventTable result = TeamEventTable.initRestResponse(messageSource);
		Team team = teamService.selectById(teamId, Team.SelectMode.NONE);

		if (team == null) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.invalidRequest");
			return result;
		}

		Volunteer coordinator = volunteerService.selectCoordinatorByUsername(team.getRoute().getGame(),
				SecureUserDetails.getCurrentUser().getUsername(), Volunteer.SelectMode.NONE);
		if (coordinator == null && !volunteerService
				.isVolunteerForGame(SecureUserDetails.getCurrentUser().getUsername(), team.getRoute().getGame())) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.forbidden");
			return result;
		}

		result.add(teamService.selectTeamEvents(team, Event.SelectMode.NONE));
		return result.validate();
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/teamEvent/{eventId}", method = RequestMethod.DELETE)
	public RestPostResponseBody rollbackTeamEvent(@PathVariable Long eventId) {
		RestPostResponseBody result = new RestPostResponseBody(messageSource);

		Event e = teamService.selectTeamEvent(eventId, Event.SelectMode.NONE);

		if (e == null) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.invalidRequest");
			return result;
		}

		Volunteer current = volunteerService.selectCoordinatorByUsername(e.getVolunteer().getVolunteerGame(),
				SecureUserDetails.getCurrentUser().getUsername(), Volunteer.SelectMode.NONE);

		if (current == null) {
			current = volunteerService.selectByControlPointAndUsername(e.getVolunteer().getControlPoint(),
					SecureUserDetails.getCurrentUser().getUsername(), true, Volunteer.SelectMode.NONE);
			if (current == null) {
				result.setResponseClass(ResponseClass.ERROR);
				result.addCommonMsg("common.forbidden");
				return result;
			}
		}

		try {
			teamService.rollbackTeamEvent(e, current, result);
		} catch (DBException e1) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.db.fail");
			return result;
		}
		return result;
	}
}

package org.runcity.mvc.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.Team;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.enumeration.ControlPointMode;
import org.runcity.db.entity.enumeration.TeamStatus;
import org.runcity.db.service.ControlPointService;
import org.runcity.db.service.GameService;
import org.runcity.db.service.RouteService;
import org.runcity.db.service.TeamService;
import org.runcity.db.service.VolunteerService;
import org.runcity.exception.DBException;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.RestPostResponseBody;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.TeamDisqualifyByCoordinatorForm;
import org.runcity.mvc.web.formdata.TeamFinishByCoordinatorForm;
import org.runcity.mvc.web.formdata.TeamNotStartedByCoordinatorForm;
import org.runcity.mvc.web.formdata.TeamProcessAbstractForm;
import org.runcity.mvc.web.formdata.TeamRetireByCoordinatorForm;
import org.runcity.mvc.web.tabledata.CoordinatorControlPointTable;
import org.runcity.mvc.web.tabledata.CoordinatorTeamStatTable;
import org.runcity.mvc.web.tabledata.CoordinatorVolunteerTable;
import org.runcity.mvc.web.tabledata.VolunteerTeamTable;
import org.runcity.secure.SecureUserDetails;
import org.runcity.util.ResponseClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class RestCoordinatorController extends AbstractRestController {
	private static final Logger logger = Logger.getLogger(RestRuntimeController.class);

	@Autowired
	private RouteService routeService;

	@Autowired
	private GameService gameService;

	@Autowired
	private ControlPointService controlPointService;

	@Autowired
	private VolunteerService volunteerService;

	@Autowired
	private TeamService teamService;

	private boolean isCoordinator(Game game) {
		return volunteerService.selectCoordinatorByUsername(game, SecureUserDetails.getCurrentUser().getUsername(),
				Volunteer.SelectMode.NONE) != null;
	}

	private boolean isCoordinator(ControlPoint controlPoint) {
		return isCoordinator(controlPoint.getGame());
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/coordControlPointsTable", method = RequestMethod.GET)
	public RestGetResponseBody getControlPointTable(@RequestParam(required = true) Long gameId) {
		logger.info("GET /api/v1/coordControlPointsTable");

		Game g = gameService.selectById(gameId, Game.SelectMode.NONE);

		if (g == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.addCommonMsg("common.db.fail");
			return result;
		}

		if (!isCoordinator(g)) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.addCommonMsg("common.forbidden");
			return result;
		}

		CoordinatorControlPointTable result = new CoordinatorControlPointTable(messageSource, localeList, g);
		result.fetchByGame(controlPointService, g);
		return result.validate();
	}

	@JsonView(CoordinatorVolunteerTable.ByControlPoint.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/coordVolunteerTableByCP", method = RequestMethod.GET)
	public RestGetResponseBody getVolunteerTable(@RequestParam(required = true) Long controlPointId) {
		logger.info("GET /api/v1/coordVolunteerTableByCP");

		ControlPoint cp = controlPointService.selectById(controlPointId, ControlPoint.SelectMode.NONE);

		if (cp == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.addCommonMsg("common.db.fail");
			return result;
		}

		if (!isCoordinator(cp)) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.addCommonMsg("common.forbidden");
			return result;
		}

		CoordinatorVolunteerTable result = new CoordinatorVolunteerTable(messageSource, localeList, cp);
		List<Volunteer> v = volunteerService.selectByControlPoint(cp, Volunteer.SelectMode.WITH_ACTIVE);
		result.add(v);
		return result.validate();
	}

	private RestPostResponseBody setControlPointMode(List<Long> id, ControlPointMode mode) {
		RestPostResponseBody result = new RestPostResponseBody(messageSource);

		Iterable<ControlPoint> cp = controlPointService.selectById(id, ControlPoint.SelectMode.NONE);
		int num = 0;

		for (ControlPoint c : cp) {
			if (!isCoordinator(c)) {
				result.addCommonMsg("common.forbidden");
				return result;
			}
			num++;
		}

		if (!(num == id.size())) {
			result.addCommonMsg("common.popupProcessError");
			return result;
		}

		controlPointService.setMode(id, mode);
		return result;
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/cpOnline", method = RequestMethod.POST)
	public RestPostResponseBody controlPointOnline(@RequestBody List<Long> id) {
		logger.info("POST /api/v1/cpOnline");
		return setControlPointMode(id, ControlPointMode.ONLINE);
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/cpOffline", method = RequestMethod.POST)
	public RestPostResponseBody controlPointOffline(@RequestBody List<Long> id) {
		logger.info("POST /api/v1/cpOnline");
		return setControlPointMode(id, ControlPointMode.OFFLINE);
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "api/v1/coordTeamStatTable", method = RequestMethod.GET)
	public RestGetResponseBody getTeamStatTable(@RequestParam(required = true) Long gameId) {
		logger.info("GET /api/v1/coordControlPointsTable");

		Game g = gameService.selectById(gameId, Game.SelectMode.NONE);

		if (g == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.addCommonMsg("common.db.fail");
			return result;
		}

		if (!isCoordinator(g)) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.addCommonMsg("common.forbidden");
			return result;
		}

		Long maxStage = gameService.getMaxLegNumber(g);
		CoordinatorTeamStatTable result = new CoordinatorTeamStatTable(messageSource, localeList, maxStage, g);
		result.fetchByGame(teamService, g);
		return result.validate();
	}

	private RestPostResponseBody setTeamStatus(TeamProcessAbstractForm form, TeamStatus status) {
		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}

		if (!isCoordinator(form.getVolunteer().getVolunteerGame())) {
			result.addCommonMsg("common.forbidden");
			return result;
		}

		try {
			teamService.setTeamStatus(form.getTeam(), form.getConfirmationToken(), status, form.getVolunteer(), result);
		} catch (DBException e) {
			logger.error(e);
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.popupProcessError");
		}

		return result;
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/coordinator/team/notStarted", method = RequestMethod.POST)
	public RestPostResponseBody notStartedTeam(@RequestBody TeamNotStartedByCoordinatorForm form) {
		logger.info("POST /api/v1/coordinator/team/notStarted");
		return setTeamStatus(form, TeamStatus.NOT_STARTED);
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/coordinator/team/finish", method = RequestMethod.POST)
	public RestPostResponseBody finishTeam(@RequestBody TeamFinishByCoordinatorForm form) {
		logger.info("POST /api/v1/coordinator/team/finish");
		return setTeamStatus(form, TeamStatus.FINISHED);
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/coordinator/team/retire", method = RequestMethod.POST)
	public RestPostResponseBody retireTeam(@RequestBody TeamRetireByCoordinatorForm form) {
		logger.info("POST /api/v1/coordinator/team/retire");
		return setTeamStatus(form, TeamStatus.RETIRED);
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/coordinator/team/disqualify", method = RequestMethod.POST)
	public RestPostResponseBody disqualifyTeam(@RequestBody TeamDisqualifyByCoordinatorForm form) {
		logger.info("POST /api/v1/coordinator/team/disqualify");
		return setTeamStatus(form, TeamStatus.DISQUALIFIED);
	}

	@JsonView(VolunteerTeamTable.ForCoordinator.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/coordinator/route/{routeId}/teamTable", method = RequestMethod.GET)
	public RestGetResponseBody getTeamsByRoute(@PathVariable Long routeId,
			@RequestParam(required = false) String status) {
		logger.info("GET /api/v1/coordinator/route/{routeId}/teamTable");
		Route r = routeService.selectById(routeId, Route.SelectMode.NONE);

		if (r == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.addCommonMsg("common.db.fail");
			return result;
		}

		if (!isCoordinator(r.getGame())) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.addCommonMsg("common.forbidden");
			return result;
		}

		VolunteerTeamTable result = VolunteerTeamTable.initRestResponse(messageSource);
		result.add(teamService.selectTeamsByRouteWithStatus(r, status, Team.SelectMode.NONE));
		return result.validate();
	}
}

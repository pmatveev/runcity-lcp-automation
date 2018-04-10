package org.runcity.mvc.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.service.ControlPointService;
import org.runcity.db.service.GameService;
import org.runcity.db.service.RouteService;
import org.runcity.db.service.VolunteerService;
import org.runcity.exception.DBException;
import org.runcity.mvc.rest.util.RestGetDddwResponseBody;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.RestPostResponseBody;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.ControlPointCreateEditByGameForm;
import org.runcity.mvc.web.formdata.VolunteerCreateEditByCPForm;
import org.runcity.mvc.web.tabledata.ControlPointTable;
import org.runcity.mvc.web.tabledata.VolunteerTable;
import org.runcity.secure.SecureUserDetails;
import org.runcity.util.ResponseClass;
import org.runcity.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class RestControlPointController extends AbstractRestController {
	private static final Logger logger = Logger.getLogger(RestCategoryController.class);

	@Autowired
	private GameService gameService;

	@Autowired
	private ControlPointService controlPointService;

	@Autowired
	private RouteService routeService;

	@Autowired
	private VolunteerService volunteerService;

	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/controlPointsTable", method = RequestMethod.GET)
	public RestGetResponseBody getControlPointsTable(@RequestParam(required = true) Long gameId) {
		logger.info("GET /api/v1/controlPointsTable");
		logger.debug("\tgameId=" + gameId);

		Game g = gameService.selectById(gameId, Game.SelectMode.NONE);
		if (g == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.addCommonMsg("common.db.fail");
			return result;
		}

		ControlPointTable table = new ControlPointTable(messageSource, localeList, g);
		table.fetchByGame(controlPointService, g);
		return table.validate();
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/controlPointCreateEdit/{id}", method = RequestMethod.GET)
	public RestGetResponseBody initControlPointCreateEditForm(@PathVariable Long id) {
		ControlPoint c = controlPointService.selectById(id, ControlPoint.SelectMode.NONE);
		if (c == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.popupFetchError");
			return result;
		}

		return new ControlPointCreateEditByGameForm(c, localeList);
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/dddw/controlPointId", method = RequestMethod.GET)
	public RestGetResponseBody controlPointDddwInit(@RequestParam(required = true) Long id) {
		logger.info("GET /api/v1/dddw/controlPointId");

		ControlPoint c = controlPointService.selectById(id, ControlPoint.SelectMode.NONE);
		RestGetDddwResponseBody<Long> result = new RestGetDddwResponseBody<Long>(messageSource);
		result.addOption(c.getId(), StringUtils.xss(c.getNameDisplayWithType(messageSource, LocaleContextHolder.getLocale())));
		return result;
	}

	@JsonView(Views.Public.class)
	@Secured({ "ROLE_ADMIN", "ROLE_VOLUNTEER" })
	@RequestMapping(value = "/api/v1/dddw/controlPointMainByGame", method = RequestMethod.GET)
	public RestGetResponseBody controlPointDddwMainByGame(@RequestParam(required = false) Long self,
			@RequestParam(required = true) Long game) {
		logger.info("GET /api/v1/dddw/controlPointMainByGame");

		List<ControlPoint> controlPoints = controlPointService.selectMainByGame(game, ControlPoint.SelectMode.NONE);
		RestGetDddwResponseBody<Long> result = new RestGetDddwResponseBody<Long>(messageSource);
		for (ControlPoint c : controlPoints) {
			if (!ObjectUtils.nullSafeEquals(self, c.getId())) {
				result.addOption(c.getId(), StringUtils.xss(c.getNameDisplayWithType(messageSource, LocaleContextHolder.getLocale())));
			}
		}
		return result;
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/dddw/controlPointByRoute", method = RequestMethod.GET)
	public RestGetResponseBody controlPointDddwByRoute(@RequestParam(required = false) Long self,
			@RequestParam(required = true) Long route) {
		logger.info("GET /api/v1/dddw/controlPointMainByGame");

		List<ControlPoint> controlPoints = controlPointService.selectByRouteNotUsed(route,
				ControlPoint.SelectMode.NONE);
		if (self != null) {
			RouteItem ri = routeService.selectItemById(self);
			controlPoints.add(ri.getControlPoint());
		}

		RestGetDddwResponseBody<Long> result = new RestGetDddwResponseBody<Long>(messageSource);
		for (ControlPoint c : controlPoints) {
			result.addOption(c.getId(), StringUtils.xss(c.getNameDisplayWithType(messageSource, LocaleContextHolder.getLocale())));
		}
		return result;
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/controlPointCreateEdit", method = RequestMethod.POST)
	public RestPostResponseBody controlPointCreateEdit(@RequestBody ControlPointCreateEditByGameForm form) {
		logger.info("POST /api/v1/controlPointCreateEdit");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}

		ControlPoint c = null;
		try {
			c = controlPointService.addOrUpdate(form.getControlPoint());
		} catch (DBException e) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.db.fail");
			logger.error("DB exception", e);
			return result;
		}
		if (c == null) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.popupProcessError");
		}
		return result;
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/controlPointDelete", method = RequestMethod.DELETE)
	public RestPostResponseBody controlPointDelete(@RequestBody List<Long> id) {
		logger.info("DELETE /api/v1/controlPointDelete");
		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		try {
			controlPointService.delete(id);
		} catch (Exception e) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("commom.db.deleteConstraint");
		}
		return result;
	}

	@JsonView(VolunteerTable.ByControlPoint.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/volunteerTableByCP", method = RequestMethod.GET)
	public VolunteerTable getVolunteersTable(@RequestParam(required = true) Long controlPointId) {
		logger.info("GET /api/v1/volunteerTableByCP");
		logger.debug("\tcontrolPointId=" + controlPointId);

		ControlPoint cp = controlPointService.selectById(controlPointId, ControlPoint.SelectMode.NONE);

		VolunteerTable table = new VolunteerTable(messageSource, localeList, cp);
		table.add(volunteerService.selectByControlPoint(cp, Volunteer.SelectMode.NONE));
		return table.validate();
	}

	@JsonView(Views.Public.class)
	@Secured({ "ROLE_ADMIN", "ROLE_VOLUNTEER" })
	@RequestMapping(value = "/api/v1/volunteerCreateEditByCP/{id}", method = RequestMethod.GET)
	public RestGetResponseBody initVolunteerCreateEditForm(@PathVariable Long id) {
		Volunteer v = volunteerService.selectById(id, Volunteer.SelectMode.NONE);

		if (v == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.popupFetchError");
			return result;
		}

		SecureUserDetails user = SecureUserDetails.getCurrentUser();
		if (!user.isAdmin() && volunteerService.selectCoordinatorByUsername(v.getVolunteerGame(), user.getUsername(),
				Volunteer.SelectMode.NONE) == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.forbidden");
			return result;
		}

		return new VolunteerCreateEditByCPForm(v, localeList);
	}

	@JsonView(Views.Public.class)
	@Secured({ "ROLE_ADMIN", "ROLE_VOLUNTEER" })
	@RequestMapping(value = "/api/v1/volunteerCreateEditByCP", method = RequestMethod.POST)
	public RestPostResponseBody volunteerCreateEdit(@RequestBody VolunteerCreateEditByCPForm form) {
		logger.info("POST /api/v1/volunteerCreateEdit");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}

		SecureUserDetails user = SecureUserDetails.getCurrentUser();
		if (!user.isAdmin() && volunteerService.selectCoordinatorByUsername(form.getVolunteer().getVolunteerGame(),
				user.getUsername(), Volunteer.SelectMode.NONE) == null) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.forbidden");
			return result;
		}

		Volunteer v = null;
		try {
			v = volunteerService.addOrUpdate(form.getVolunteer());
		} catch (DBException e) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.db.fail");
			logger.error("DB exception", e);
			return result;
		}
		if (v == null) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.popupProcessError");
		}
		return result;
	}
}

package org.runcity.mvc.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.service.ControlPointService;
import org.runcity.db.service.GameService;
import org.runcity.db.service.VolunteerService;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.tabledata.CoordinatorControlPointTable;
import org.runcity.mvc.web.tabledata.CoordinatorVolunteerTable;
import org.runcity.secure.SecureUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class RestCoordinatorController extends AbstractRestController {
	private static final Logger logger = Logger.getLogger(RestRuntimeController.class);
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private ControlPointService controlPointService;
	
	@Autowired
	private VolunteerService volunteerService;
	
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
		
		if (!volunteerService.isCoordinator(g, SecureUserDetails.getCurrentUser().getUsername())) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.addCommonMsg("common.forbidden");
			return result;				
		}
		
		CoordinatorControlPointTable result = new CoordinatorControlPointTable(messageSource, localeList, g);
		result.fetchByGame(controlPointService, g);
		return result.validate();
	}	
	
	@JsonView({ CoordinatorVolunteerTable.ByControlPoint.class })
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
		
		if (!volunteerService.isCoordinator(cp.getGame(), SecureUserDetails.getCurrentUser().getUsername())) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.addCommonMsg("common.forbidden");
			return result;				
		}
		
		CoordinatorVolunteerTable result = new CoordinatorVolunteerTable(messageSource, localeList, cp);
		List<Volunteer> v = volunteerService.selectByControlPoint(cp, Volunteer.SelectMode.WITH_ACTIVE);
		result.add(v);
		return result.validate();
	}	
}

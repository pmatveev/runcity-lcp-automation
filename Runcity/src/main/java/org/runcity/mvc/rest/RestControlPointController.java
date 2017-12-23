package org.runcity.mvc.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.service.ControlPointService;
import org.runcity.db.service.GameService;
import org.runcity.mvc.rest.util.RestGetDddwResponseBody;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.tabledata.ControlPointTable;
import org.runcity.util.DynamicLocaleList;
import org.springframework.beans.factory.annotation.Autowired;
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
	private DynamicLocaleList localeList;
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/controlPointsTable", method = RequestMethod.GET)
	public RestGetResponseBody getConsumerTable(@RequestParam(required = true) Long gameId) {
		logger.info("GET /api/v1/controlPointsTable");
		logger.debug("\tgameId=" + gameId);
		
		Game g = gameService.selectById(gameId, false);
		if (g == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.addCommonError("common.db.fail");
			return result;
		}
		
		ControlPointTable table = new ControlPointTable(g, null, messageSource, localeList);
		table.fetchByGame(controlPointService, g);
		return table;
	}	
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/dddw/controlPointId", method = RequestMethod.GET)
	public RestGetResponseBody controlPointDddwInit(@RequestParam(required = true) Long id) {
		logger.info("GET /api/v1/dddw/controlPointId");
		
		ControlPoint c = controlPointService.selectById(id, false);
		RestGetDddwResponseBody<Long> result = new RestGetDddwResponseBody<Long>(messageSource);
		result.addOption(c.getId(), c.getNameDisplay());
		return result;
	}
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/dddw/controlPointMainByGame", method = RequestMethod.GET)
	public RestGetResponseBody controlPointDddwMainByGame(@RequestParam(required = true) Long game) {
		logger.info("GET /api/v1/dddw/controlPointMainByGame");
		
		List<ControlPoint> controlPoints = controlPointService.selectMainByGame(game);
		RestGetDddwResponseBody<Long> result = new RestGetDddwResponseBody<Long>(messageSource);
		for (ControlPoint c : controlPoints) {
			result.addOption(c.getId(), c.getNameDisplay());
		}
		return result;
	}
}

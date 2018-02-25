package org.runcity.mvc.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.service.CategoryService;
import org.runcity.db.service.GameService;
import org.runcity.db.service.RouteService;
import org.runcity.db.service.VolunteerService;
import org.runcity.exception.DBException;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.RestPostResponseBody;
import org.runcity.mvc.rest.util.RestResponseClass;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.RouteCreateForm;
import org.runcity.mvc.web.formdata.RouteItemCreateEditForm;
import org.runcity.mvc.web.formdata.VolunteerCreateEditByGameCPForm;
import org.runcity.mvc.web.formdata.VolunteerCreateEditByGameCoordForm;
import org.runcity.mvc.web.formdata.GameCreateEditForm;
import org.runcity.mvc.web.tabledata.RouteTable;
import org.runcity.mvc.web.tabledata.VolunteerTable;
import org.runcity.mvc.web.tabledata.GameTable;
import org.runcity.mvc.web.tabledata.RouteItemTable;
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
public class RestGameController extends AbstractRestController {
	private static final Logger logger = Logger.getLogger(RestGameController.class);
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private RouteService routeService;
	
	@Autowired 
	private VolunteerService volunteerService;
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/gameTable", method = RequestMethod.GET)
	public GameTable getGameTable() {
		logger.info("GET /api/v1/gameTable");
		GameTable table = new GameTable(messageSource, localeList);
		table.fetchAll(gameService, localeList);
		return table;
	}	
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/gameCreateEdit/{id}", method = RequestMethod.GET)
	public RestGetResponseBody initGameCreateEditForm(@PathVariable Long id) {		
		Game g = gameService.selectById(id, true);
		if (g == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupFetchError");
			return result;
		}

		return new GameCreateEditForm(g, localeList);
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/gameCreateEdit", method = RequestMethod.POST)
	public RestPostResponseBody gameCreateEdit(@RequestBody GameCreateEditForm form) {
		logger.info("POST /api/v1/gameCreateEdit");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}
		
		Game g = null;
		try {
			g = gameService.addOrUpdate(form.getGame());
		} catch (DBException e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.db.fail");
			logger.error("DB exception", e);
			return result;			
		}
		if (g == null) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupProcessError");
		}
		return result;
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/gameDelete", method = RequestMethod.DELETE)
	public RestPostResponseBody gameDelete(@RequestBody List<Long> id) {
		logger.info("DELETE /api/v1/gameDelete");
		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		try {
			gameService.delete(id);
		} catch (Exception e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("commom.db.deleteConstraint");
		}
		return result;		
	}

	@JsonView(RouteTable.ByGame.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/routeTableByGame", method = RequestMethod.GET)
	public RouteTable getRouteTableByGame(@RequestParam(required = true) Long gameId) {
		logger.info("GET /api/v1/routeTableByGame");
		logger.debug("\tgameId=" + gameId);
		
		Game game = gameService.selectById(gameId, true);
		RouteTable table = new RouteTable(messageSource, localeList, game);
		table.fill(game);
		return table;
	}

	@JsonView(RouteTable.ByCategory.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/routeTableByCategory", method = RequestMethod.GET)
	public RouteTable getRouteTableByCategory(@RequestParam(required = true) Long categoryId) {
		logger.info("GET /api/v1/routeTableByCategory");
		logger.debug("\tcategoryId=" + categoryId);
		
		Category category = categoryService.selectById(categoryId, true);
		RouteTable table = new RouteTable(messageSource, localeList, category);
		table.fill(category);
		return table;
	}	
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/routeCreate", method = RequestMethod.POST)
	public RestPostResponseBody routeCreate(@RequestBody RouteCreateForm form) {
		logger.info("POST /api/v1/routeEdit");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}
		
		Game g = null;
		try {
			g = gameService.addOrUpdate(form.getGame());
		} catch (DBException e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.db.fail");
			logger.error("DB exception", e);
			return result;			
		}
		if (g == null) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupProcessError");
		}
		return result;
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/routeDelete/", method = RequestMethod.DELETE)
	public RestPostResponseBody routeDelete(@RequestBody List<Long> id) {
		logger.info("DELETE /api/v1/routeDelete");
		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		try {
			routeService.delete(id);
		} catch (Exception e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("commom.db.deleteConstraint");
		}
		return result;		
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/routeItemTable", method = RequestMethod.GET)
	public RouteItemTable getRouteItemTable(@RequestParam(required = true) Long routeId) {
		logger.info("GET /api/v1/routeTable");
		logger.debug("\trouteId=" + routeId);
		
		Route r = routeService.selectById(routeId, true);
		RouteItemTable table = new RouteItemTable(messageSource, localeList, r);
		table.fill(r);
		return table;
	}	
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/routeItemCreateEdit/{id}", method = RequestMethod.GET)
	public RestGetResponseBody initRouteItemCreateEditForm(@PathVariable Long id) {		
		RouteItem ri = routeService.selectItemById(id);
		if (ri == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupFetchError");
			return result;
		}
		
		return new RouteItemCreateEditForm(ri, localeList);
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/routeItemCreateEdit", method = RequestMethod.POST)
	public RestPostResponseBody routeItemCreateEdit(@RequestBody RouteItemCreateEditForm form) {		
		logger.info("POST /api/v1/routeItemCreateEdit");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}
		
		RouteItem ri = null;
		try {
			ri = routeService.addOrUpdateItem(form.getRouteItem());
		} catch (DBException e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.db.fail");
			logger.error("DB exception", e);
			return result;			
		}
		if (ri == null) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupProcessError");
		}
		return result;
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/routeItemDelete/", method = RequestMethod.DELETE)
	public RestPostResponseBody routeItemDelete(@RequestBody List<Long> id) {
		logger.info("DELETE /api/v1/routeItemDelete");
		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		try {
			routeService.deleteItem(id);
		} catch (Exception e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("commom.db.deleteConstraint");
		}
		return result;		
	}
	
	@JsonView(VolunteerTable.ByGame.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/coordinatorTableByGame", method = RequestMethod.GET)
	public VolunteerTable getCoordinatorsTable(@RequestParam(required = true) Long gameId) {
		logger.info("GET /api/v1/coordinatorTableByGame");
		logger.debug("\tgameId=" + gameId);
		
		Game g = gameService.selectById(gameId, false);
		
		VolunteerTable table = VolunteerTable.initCoordinatorsByGame(messageSource, localeList, g);
		table.add(gameService.selectCoordinators(g));
		return table;
	}	
	
	@JsonView(VolunteerTable.ByGameControlPoint.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/volunteerTableByGame", method = RequestMethod.GET)
	public VolunteerTable getVolunteersTable(@RequestParam(required = true) Long gameId) {
		logger.info("GET /api/v1/volunteerTableByGame");
		logger.debug("\tgameId=" + gameId);
		
		Game g = gameService.selectById(gameId, false);

		VolunteerTable table = VolunteerTable.initVolunteersByGame(messageSource, localeList, g);
		table.add(gameService.selectVolunteers(g));
		return table;
	}	
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/volunteerCreateEditByGameCP/{id}", method = RequestMethod.GET)
	public RestGetResponseBody initVolunteerCreateEditForm(@PathVariable Long id) {		
		Volunteer v = volunteerService.selectById(id);
		
		if (v == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupFetchError");
			return result;
		}

		return new VolunteerCreateEditByGameCPForm(v, localeList);
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/volunteerCreateEditByGameCP", method = RequestMethod.POST)
	public RestPostResponseBody volunteerCreateEdit(@RequestBody VolunteerCreateEditByGameCPForm form) {
		logger.info("POST /api/v1/volunteerCreateEditByGameCP");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}
		
		Volunteer v = null;
		try {
			v = volunteerService.addOrUpdate(form.getVolunteer());
		} catch (DBException e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.db.fail");
			logger.error("DB exception", e);
			return result;			
		}
		if (v == null) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupProcessError");
		}
		return result;
	}	
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/volunteerCreateEditByGameCoord/{id}", method = RequestMethod.GET)
	public RestGetResponseBody initCoordinatorCreateEditForm(@PathVariable Long id) {		
		Volunteer v = volunteerService.selectById(id);
		
		if (v == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupFetchError");
			return result;
		}

		return new VolunteerCreateEditByGameCoordForm(v, localeList);
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/volunteerCreateEditByGameCoord", method = RequestMethod.POST)
	public RestPostResponseBody coordinatorCreateEdit(@RequestBody VolunteerCreateEditByGameCoordForm form) {
		logger.info("POST /api/v1/volunteerCreateEditByGameCoord");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}
		
		Volunteer v = null;
		try {
			v = volunteerService.addOrUpdate(form.getVolunteer());
		} catch (DBException e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.db.fail");
			logger.error("DB exception", e);
			return result;			
		}
		if (v == null) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupProcessError");
		}
		return result;
	}	
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/volunteerDelete/", method = RequestMethod.DELETE)
	public RestPostResponseBody volunteerDelete(@RequestBody List<Long> id) {
		logger.info("DELETE /api/v1/volunteerDelete");
		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		try {
			volunteerService.delete(id);
		} catch (Exception e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("commom.db.deleteConstraint");
		}
		return result;	
	}
}

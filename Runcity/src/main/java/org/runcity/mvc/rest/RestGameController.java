package org.runcity.mvc.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Game;
import org.runcity.db.service.GameService;
import org.runcity.exception.DBException;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.RestPostResponseBody;
import org.runcity.mvc.rest.util.RestResponseClass;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.GameCreateEditForm;
import org.runcity.mvc.web.tabledata.GameTable;
import org.runcity.util.DynamicLocaleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class RestGameController extends AbstractRestController {
	private static final Logger logger = Logger.getLogger(RestGameController.class);
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private DynamicLocaleList localeList;

	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/gameTable", method = RequestMethod.GET)
	public GameTable getGameTable() {
		logger.info("GET /api/v1/categoryTable");
		GameTable table = new GameTable(null, messageSource, localeList);
		table.fetchAll(gameService, localeList);
		return table;
	}	
	
	@JsonView(Views.Public.class)
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
	@RequestMapping(value = "/api/v1/gameCreateEdit", method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
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
	@RequestMapping(value = "/api/v1/gameDelete", method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
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
}

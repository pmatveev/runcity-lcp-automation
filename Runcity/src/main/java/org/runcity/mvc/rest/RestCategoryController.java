package org.runcity.mvc.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.runcity.db.service.CategoryService;
import org.runcity.db.service.GameService;
import org.runcity.exception.DBException;
import org.runcity.mvc.rest.util.RestGetDddwResponseBody;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.RestPostResponseBody;
import org.runcity.mvc.rest.util.RestResponseClass;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.CategoryCreateEditForm;
import org.runcity.mvc.web.tabledata.CategoryTable;
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
public class RestCategoryController extends AbstractRestController {
	private static final Logger logger = Logger.getLogger(RestCategoryController.class);
	
	@Autowired 
	private CategoryService categoryService;
	
	@Autowired 
	private GameService gameService;
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/categoryTable", method = RequestMethod.GET)
	public CategoryTable getConsumerTable() {
		logger.info("GET /api/v1/categoryTable");
		CategoryTable table = new CategoryTable(messageSource, localeList);
		table.fetchAll(categoryService);
		return table;
	}	
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/categoryCreateEdit/{id}", method = RequestMethod.GET)
	public RestGetResponseBody initCategoryCreateEditForm(@PathVariable Long id) {		
		Category c = categoryService.selectById(id, false);
		if (c == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonMsg("common.popupFetchError");
			return result;
		}

		return new CategoryCreateEditForm(c, localeList);
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/categoryCreateEdit", method = RequestMethod.POST)
	public RestPostResponseBody categoryCreateEdit(@RequestBody CategoryCreateEditForm form) {
		logger.info("POST /api/v1/categoryCreateEdit");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}
		
		Category c = null;
		try {
			c = categoryService.addOrUpdate(form.getCategory());
		} catch (DBException e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonMsg("common.db.fail");
			logger.error("DB exception", e);
			return result;			
		}
		if (c == null) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonMsg("common.popupProcessError");
		}
		return result;
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/categoryDelete", method = RequestMethod.DELETE)
	public RestPostResponseBody categoryDelete(@RequestBody List<Long> id) {
		logger.info("DELETE /api/v1/categoryDelete");
		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		try {
			categoryService.delete(id);
		} catch (Exception e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonMsg("commom.db.deleteConstraint");
		}
		return result;	
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/dddw/categoriesId", method = RequestMethod.GET)
	public RestGetResponseBody categoriesDddwInit(@RequestParam(required = true) List<Long> id, @RequestParam(required = true) String locale) {
		logger.info("GET /api/v1/dddw/categoriesId");
		
		Iterable<Category> categories = categoryService.selectById(id, false);
		RestGetDddwResponseBody<Long> result = new RestGetDddwResponseBody<Long>(messageSource);
		for (Category c : categories) {
			result.addOption(c.getId(), c.getLocalizedName(locale));
		}
		return result;
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/dddw/categories", method = RequestMethod.GET)
	public RestGetResponseBody categoriesDddw(@RequestParam(required = true) String locale) {
		logger.info("GET /api/v1/dddw/categories");
		
		List<Category> categories = categoryService.selectAll(false);
		RestGetDddwResponseBody<Long> result = new RestGetDddwResponseBody<Long>(messageSource);
		for (Category c : categories) {
			result.addOption(c.getId(), c.getLocalizedName(locale));
		}
		return result;
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/dddw/unusedCategories", method = RequestMethod.GET)
	public RestGetResponseBody unusedCategoriesDddw(@RequestParam(required = true) Long gameId) {
		logger.info("GET /api/v1/dddw/unusedCategories");
		
		Game game = gameService.selectById(gameId, false);
		List<Category> categories = categoryService.selectUnused(game, false);
		
		RestGetDddwResponseBody<Long> result = new RestGetDddwResponseBody<Long>(messageSource);
		for (Category c : categories) {
			result.addOption(c.getId(), c.getLocalizedName(game.getLocale()));
		}
		return result;
	}
}

package org.runcity.mvc.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Category;
import org.runcity.db.service.CategoryService;
import org.runcity.exception.DBException;
import org.runcity.mvc.rest.util.RestGetDddwResponseBody;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.RestPostResponseBody;
import org.runcity.mvc.rest.util.RestResponseClass;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.CategoryCreateEditForm;
import org.runcity.mvc.web.tabledata.CategoryTable;
import org.runcity.util.DynamicLocaleList;
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
	private DynamicLocaleList localeList;
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/categoryTable", method = RequestMethod.GET)
	public CategoryTable getConsumerTable() {
		logger.info("GET /api/v1/categoryTable");
		CategoryTable table = new CategoryTable(null, messageSource, localeList);
		table.fetchAll(categoryService);
		return table;
	}	
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/categoryCreateEdit/{id}", method = RequestMethod.GET)
	public RestGetResponseBody initCategoryCreateEditForm(@PathVariable Long id) {		
		Category c = categoryService.selectById(id);
		if (c == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupFetchError");
			return result;
		}

		return new CategoryCreateEditForm(c, localeList);
	}
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/categoryCreateEdit", method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
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
			result.addCommonError("common.db.fail");
			logger.error("DB exception", e);
			return result;			
		}
		if (c == null) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupProcessError");
		}
		return result;
	}
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/categoryDelete", method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public RestPostResponseBody categoryDelete(@RequestBody List<Long> id) {
		logger.info("DELETE /api/v1/consumerDelete");
		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		
		try {
			categoryService.delete(id);
		} catch (DBException e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.db.fail");
			logger.error("DB exception", e);
			return result;
		}
		
		return result;	
	}
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/dddw/categories", method = RequestMethod.GET)
	public RestGetResponseBody categoriesDddw(@RequestParam(required = true) String locale) {
		logger.info("GET /api/v1/dddw/categories");
		
		try {
			List<Category> categories = categoryService.selectAll();
			RestGetDddwResponseBody<Long> result = new RestGetDddwResponseBody<Long>(messageSource);
			for (Category c : categories) {
				result.addOption(c.getId(), c.getNameDisplay(locale));
			}
			return result;
		} catch (Exception e) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.db.fail");
			logger.error("DB exception", e);
			return result;
		}
	}
}

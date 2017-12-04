package org.runcity.mvc.rest;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Category;
import org.runcity.db.service.CategoryService;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.RestResponseClass;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.CategoryCreateEditForm;
import org.runcity.mvc.web.tabledata.CategoryTable;
import org.runcity.util.DynamicLocaleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
}

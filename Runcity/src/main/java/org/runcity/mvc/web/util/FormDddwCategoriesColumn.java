package org.runcity.mvc.web.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.runcity.db.entity.Category;
import org.runcity.db.service.CategoryService;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormDddwCategoriesColumn extends FormDddwColumn<List<Long>> {
	private enum FetchType {
		ALL;
	}

	private Set<Category> categories;
	private FetchType fetchType;

	private FormDddwCategoriesColumn(AbstractForm form, ColumnDefinition definition, String[] initParms,
			String ajaxSource, String[] ajaxParms, FetchType fetchType) {
		super(form, definition, "/api/v1/dddw/categoriesId?id={0}&locale={1}", initParms, ajaxSource, ajaxParms, true);
		
		this.value = new ArrayList<Long>();
		this.fetchType = fetchType;
	}

	public static FormDddwCategoriesColumn getAllByLocale(AbstractForm form, ColumnDefinition definition,
			FormColumn<?> localeCol) {
		return new FormDddwCategoriesColumn(form, definition, new String[] { localeCol.getHtmlId() },
				"/api/v1/dddw/categories?locale={0}", new String[] { localeCol.getHtmlId() }, FetchType.ALL);
	}

	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		CategoryService categoryService = context.getBean(CategoryService.class);
		categories = new HashSet<Category>();

		if (value.size() == 0) {
			return;
		}
		
		boolean proceed = true;
		for (Long id : value) {
			Category c = categoryService.selectById(id, false);
			if (c == null) {
				errors.rejectValue(getName(), "common.notFoundId", new Object[] { id }, null);
				proceed = false;
			} else {
				categories.add(c);
			}
		}
		
		if (!proceed) {
			return;
		}
		
		switch (fetchType) {
		case ALL:
			break;
		}
	}

	public Set<Category> getCategories() {
		return categories;
	}
}

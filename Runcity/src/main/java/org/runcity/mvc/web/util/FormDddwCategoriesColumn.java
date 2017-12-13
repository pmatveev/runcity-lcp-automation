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
	private Set<Category> categories;
	
	private FormDddwCategoriesColumn(AbstractForm form, ColumnDefinition definition, String[] initParms, String ajaxSource,
			String[] ajaxParms, boolean required) {
		super(form, definition, "/api/v1/dddw/categoriesId?id={0}&locale={1}", initParms, ajaxSource, ajaxParms, true, required);
		this.value = new ArrayList<Long>();
	}

	public static FormDddwCategoriesColumn getAllByLocale(AbstractForm form, ColumnDefinition definition, String localeCol, boolean required) {
		return new FormDddwCategoriesColumn(form, definition, new String[] {localeCol}, "/api/v1/dddw/categories?locale={0}", new String[] {localeCol}, required);
	}
	
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);
		
		CategoryService categoryService = context.getBean(CategoryService.class);
		categories = new HashSet<Category>();
		
		for (Long id : value) {
			Category c = categoryService.selectById(id);
			if (c == null) {
				errors.rejectValue(getName(), "common.notFoundId", new Object[] {id}, null);
			} else {
				categories.add(c);
			}
		}
	}
	
	public Set<Category> getCategories() {
		return categories;
	}
}

package org.runcity.mvc.web.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.service.CategoryService;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormDddwCategoriesColumn extends FormDddwColumn<List<Long>> {
	private enum FetchType {
		ALL, UNUSED;
	}

	private Set<Category> categories;
	private FetchType fetchType;

	private FormDddwCategoriesColumn(AbstractForm form, ColumnDefinition definition, String[] initParms,
			String ajaxSource, String[] ajaxParms, FetchType fetchType, Object... validation) {
		super(form, definition, "/api/v1/dddw/categoriesId?id={0}&locale={1}", initParms, ajaxSource, ajaxParms, true,
				validation);

		this.value = new ArrayList<Long>();
		this.fetchType = fetchType;
	}

	public static FormDddwCategoriesColumn getAllByLocale(AbstractForm form, ColumnDefinition definition,
			FormColumn<?> localeCol) {
		return new FormDddwCategoriesColumn(form, definition, new String[] { localeCol.getHtmlId() },
				"/api/v1/dddw/categories?locale={0}", new String[] { localeCol.getHtmlId() }, FetchType.ALL);
	}

	public static FormDddwCategoriesColumn getUnusedByGame(AbstractForm form, ColumnDefinition definition,
			FormColumn<?> gameCol) {
		return new FormDddwCategoriesColumn(form, definition, new String[] { gameCol.getHtmlId() },
				"/api/v1/dddw/unusedCategories?gameId={0}", new String[] { gameCol.getHtmlId() }, FetchType.UNUSED,
				gameCol);
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
		case UNUSED:
			/*
			 * validation
			 * 	0: game
			 */
			Game g = ((FormIdGameColumn) validation[0]).getGame();

			for (Route gc : g.getCategories()) {
				if (categories.contains(gc.getCategory())) {
					errors.rejectValue(getName(), "common.illegalValue",
							new Object[] { gc.getCategory().getLocalizedName(g.getLocale()) }, null);
				}
			}

			break;
		}
	}

	public Set<Category> getCategories() {
		return categories;
	}
}

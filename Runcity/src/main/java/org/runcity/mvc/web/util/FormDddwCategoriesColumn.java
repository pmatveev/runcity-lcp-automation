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
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormDddwCategoriesColumn extends FormDddwColumn<List<Long>> {
	private enum FetchType {
		ALL, UNUSED;
	}

	private Set<Category> categories;
	private FetchType fetchType;
	private boolean checkSubstring;

	private FormDddwCategoriesColumn(AbstractForm form, ColumnDefinition definition, FormColumn<?>[] initParms,
			String ajaxSource, FormColumn<?>[] ajaxParms, FetchType fetchType, Object... validation) {
		super(form, definition, "/api/v1/dddw/categoriesId?id={0}&locale={1}", initParms, ajaxSource, ajaxParms, true,
				validation);

		this.value = new ArrayList<Long>();
		this.fetchType = fetchType;
	}

	public static FormDddwCategoriesColumn getAllByLocale(AbstractForm form, ColumnDefinition definition,
			FormStringColumn localeCol) {
		return new FormDddwCategoriesColumn(form, definition, new FormColumn<?>[] { localeCol },
				"/api/v1/dddw/categories?locale={0}", new FormColumn<?>[] { localeCol }, FetchType.ALL);
	}

	public static FormDddwCategoriesColumn getUnusedByGame(AbstractForm form, ColumnDefinition definition,
			FormIdGameColumn gameCol) {
		return new FormDddwCategoriesColumn(form, definition, new FormColumn<?>[] { gameCol },
				"/api/v1/dddw/unusedCategories?gameId={0}", new FormColumn<?>[] { gameCol }, FetchType.UNUSED, gameCol);
	}

	public void setCheckSubstring(boolean checkSubstring) {
		this.checkSubstring = checkSubstring;
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
			Category c = categoryService.selectById(id, Category.SelectMode.NONE);
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

		String locale = null;

		switch (fetchType) {
		case ALL:
			/*
			 * validation 0: locale
			 */
			locale = ((FormStringColumn) validation[0]).getValue();
			break;
		case UNUSED:
			/*
			 * validation 0: game
			 */
			Game g = ((FormIdGameColumn) validation[0]).getGame();

			if (g != null) {
				locale = g.getLocale();
				for (Route gc : g.getCategories()) {
					if (categories.contains(gc.getCategory())) {
						errors.rejectValue(getName(), "common.illegalValue",
								new Object[] { StringUtils.xss(gc.getCategory().getLocalizedName(g.getLocale())) }, null);
					}
				}
			}

			break;
		}

		if (checkSubstring) {
			for (Category c1 : categories) {
				for (Category c2 : categories) {
					if (c1 != c2 && StringUtils.startsWith(c1.getPrefix(), c2.getPrefix())) {
						errors.rejectValue(getName(), "game.validation.prefixConflict",
								new Object[] { c1.getLocalizedName(locale), c2.getLocalizedName(locale), c1.getPrefix(),
										c2.getPrefix() },
								null);
					}
				}
			}
		}
	}

	public Set<Category> getCategories() {
		return categories;
	}
}

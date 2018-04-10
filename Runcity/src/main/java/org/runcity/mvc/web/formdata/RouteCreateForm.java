package org.runcity.mvc.web.formdata;

import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormDddwCategoriesColumn;
import org.runcity.mvc.web.util.FormIdGameColumn;
import org.runcity.mvc.web.util.FormIdColumn;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public class RouteCreateForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(RouteCreateForm.class);

	@JsonView(Views.Public.class)
	private FormIdGameColumn gameId;

	@JsonView(Views.Public.class)
	private FormDddwCategoriesColumn categories;

	private boolean categoriesAdded = false;

	public RouteCreateForm() {
		this(null);
	}

	public RouteCreateForm(DynamicLocaleList localeList) {
		super("routeCreateForm", null, "/api/v1/routeCreate", localeList);
		setTitle("category.header");

		this.gameId = new FormIdGameColumn(this, new ColumnDefinition("gameId", "gameid"));
		this.gameId.setCategories(true);
		this.categories = FormDddwCategoriesColumn.getUnusedByGame(this,
				new ColumnDefinition("categories", "game.categories"), gameId);
		this.categories.setForceRefresh(true);
		this.categories.setCheckSubstring(true);
	}

	public Long getGameId() {
		return gameId.getValue();
	}

	public void setGameId(Long gameId) {
		this.gameId.setValue(gameId);
	}

	public List<Long> getCategories() {
		return categories.getValue();
	}

	public void setCategories(List<Long> categories) {
		this.categories.setValue(categories);
	}

	public FormIdColumn getGameIdColumn() {
		return gameId;
	}

	public FormDddwCategoriesColumn getCategoriesColumn() {
		return categories;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		gameId.validate(context, errors);
		categories.validate(context, errors);

		String locale = gameId.getGame().getLocale();
		for (Route r : gameId.getGame().getCategories()) {
			for (Category c : categories.getCategories()) {
				if (StringUtils.startsWith(r.getCategory().getPrefix(), c.getPrefix())
						|| StringUtils.startsWith(c.getPrefix(), r.getCategory().getPrefix())) {
					errors.rejectValue(categories.getName(), "game.validation.prefixUsed",
							new Object[] { StringUtils.xss(c.getLocalizedName(locale)), StringUtils.xss(r.getControlPointName()), StringUtils.xss(c.getPrefix()),
									StringUtils.xss(r.getCategory().getPrefix()) },
							null);
				}
			}
		}
	}

	public Game getGame() {
		if (!categoriesAdded) {
			for (Category c : categories.getCategories()) {
				gameId.getGame().addCategory(c);
			}
			categoriesAdded = true;
		}
		return gameId.getGame();
	}
}

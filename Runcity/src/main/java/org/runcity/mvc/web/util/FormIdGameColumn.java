package org.runcity.mvc.web.util;

import org.runcity.db.entity.Game;
import org.runcity.db.service.GameService;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormIdGameColumn extends FormIdColumn {
	private Game game;
	private boolean categories = false;

	public FormIdGameColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);

		GameService gameService = context.getBean(GameService.class);
		game = gameService.selectById(value, categories ? Game.SelectMode.WITH_CATEGORIES : Game.SelectMode.NONE);

		if (game == null) {
			errors.reject("common.notFoundHiddenId", new Object[] { getLabel(), value }, null);
		}
	}

	public void setCategories(boolean categories) {
		this.categories = categories;
	}
	
	public Game getGame() {
		return game;
	}
}

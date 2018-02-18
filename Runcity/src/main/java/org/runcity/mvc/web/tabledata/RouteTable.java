package org.runcity.mvc.web.tabledata;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.web.formdata.RouteCreateForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

public class RouteTable extends AbstractTable {
	public static class ByGame {
	}

	public static class ByCategory {
	}

	@JsonView({ ByGame.class, ByCategory.class })
	private List<TableRow> data = new LinkedList<TableRow>();

	protected class TableRow {
		@JsonView({ ByGame.class, ByCategory.class })
		private Long id;

		@JsonView(ByGame.class)
		private String category;

		@JsonView(ByCategory.class)
		private String game;

		@JsonView(ByGame.class)
		private String categoryBadge;

		@JsonView(ByGame.class)
		private String categoryDescription;

		@JsonView(ByCategory.class)
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_FORMAT)
		private Date gameDate;

		@JsonView(ByCategory.class)
		private String gameCity;

		@JsonView(ByCategory.class)
		private String gameCountry;

		public TableRow(Route r, MessageSource messageSource, Locale l, Class<?> tableView) {
			this.id = r.getId();

			if (ByGame.class.equals(tableView)) {
				this.categoryBadge = r.getCategory().getBadge();
				this.category = r.getCategory().getLocalizedName(r.getGame().getLocale());
				this.categoryDescription = r.getCategory().getLocalizedDescription(r.getGame().getLocale());
			}

			if (ByCategory.class.equals(tableView)) {
				this.game = r.getGame().getName();
				this.gameDate = r.getGame().getDate();
				this.gameCity = r.getGame().getCity();
				this.gameCountry = r.getGame().getCountry();
			}
		}

		public Long getId() {
			return id;
		}

		public String getCategory() {
			return category;
		}

		public String getCategoryBadge() {
			return categoryBadge;
		}

		public String getCategoryDescription() {
			return categoryDescription;
		}

		public String getGame() {
			return game;
		}

		public Date getGameDate() {
			return gameDate;
		}

		public String getGameCity() {
			return gameCity;
		}

		public String getGameCountry() {
			return gameCountry;
		}
	}

	public RouteTable(String ajaxData, MessageSource messageSource, DynamicLocaleList localeList, Game g) {
		super("routeTable", "route.tableHeaderByGame", "route.simpleTableHeader", ajaxData, messageSource, localeList,
				g.getName());

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("categoryBadge", "category.badge"));
		this.columns.add(new ColumnDefinition("category", "route.category").setSort("asc", 0));
		this.columns.add(new ColumnDefinition("categoryDescription", "category.description"));

		this.expandFrame = "/secure/iframe/route/{0}:id";

		this.buttons.add(new ButtonDefinition("actions.create", null, "btn", "form:routeCreateForm", null));
		this.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn",
				"ajax:DELETE:/api/v1/routeDelete/:id", "selected"));

		RouteCreateForm createForm = new RouteCreateForm(localeList);
		createForm.setGameId(g.getId());
		this.relatedForms.add(createForm);
	}

	public RouteTable(String ajaxData, MessageSource messageSource, DynamicLocaleList localeList, Category c) {
		super("routeTable", "route.tableHeaderByCategory", "route.simpleTableHeader", ajaxData, messageSource,
				localeList, c.getLocalizedName(LocaleContextHolder.getLocale().toString()));

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("game", "route.game"));
		this.columns.add(new ColumnDefinition("gameCity", "game.city"));
		this.columns.add(new ColumnDefinition("gameCountry", "game.country"));
		this.columns.add(new ColumnDefinition("gameDate", "game.date").setDateFormat().setSort("desc", 0));

		this.expandFrame = "/secure/iframe/route/{0}/:id";
	}

	public void fill(Game g) {
		for (Route r : g.getCategories()) {
			data.add(new TableRow(r, messageSource, locale, ByGame.class));
		}
	}

	public void fill(Category c) {
		for (Route r : c.getGames()) {
			data.add(new TableRow(r, messageSource, locale, ByCategory.class));
		}
	}

	public List<TableRow> getData() {
		return data;
	}
}

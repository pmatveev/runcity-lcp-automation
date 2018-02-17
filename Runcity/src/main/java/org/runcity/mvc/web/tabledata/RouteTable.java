package org.runcity.mvc.web.tabledata;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.RouteCreateForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.annotation.JsonView;

public class RouteTable extends AbstractTable {

	@JsonView(Views.Public.class)
	private List<TableRow> data = new LinkedList<TableRow>();

	protected class TableRow {
		@JsonView(Views.Public.class)
		private Long id;

		@JsonView(Views.Public.class)
		private String category;

		@JsonView(Views.Public.class)
		private String game;

		@JsonView(Views.Public.class)
		private String badge;

		@JsonView(Views.Public.class)
		private String description;

		@JsonView(Views.Public.class)
		private String frame;

		public TableRow(Route r, MessageSource messageSource, Locale l) {
			this.id = r.getId();
			this.badge = r.getCategory().getBadge();
			this.category = r.getCategory().getLocalizedName(r.getGame().getLocale());
			this.description = r.getCategory().getLocalizedDescription(r.getGame().getLocale());
			this.game = r.getGame().getName();
		}

		public Long getId() {
			return id;
		}

		public String getCategory() {
			return category;
		}

		public String getBadge() {
			return badge;
		}

		public String getDescription() {
			return description;
		}

		public String getGame() {
			return game;
		}

		public String getFrame() {
			return frame;
		}
	}

	public RouteTable(String ajaxData, MessageSource messageSource, DynamicLocaleList localeList, Game g) {
		super("routeTable", "route.tableHeaderByGame", "route.simpleTableHeader", ajaxData,
				messageSource, localeList, g.getName());

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("badge", "category.badge"));
		this.columns.add(new ColumnDefinition("category", "route.category").setSort("asc", 0));
		this.columns.add(new ColumnDefinition("description", "category.description"));

		this.expandFrame = "/secure/iframe/route/{0}:id";

		this.buttons.add(new ButtonDefinition("actions.create", null, "btn", "form:routeCreateForm", null));
		this.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn",
				"ajax:DELETE:/api/v1/routeDelete/:id", "selected"));

		RouteCreateForm createForm = new RouteCreateForm(localeList);
		createForm.setGameId(g.getId());
		this.relatedForms.add(createForm);
	}

	public RouteTable(String ajaxData, MessageSource messageSource, DynamicLocaleList localeList, Category c) {
		super("gameCategoryTable", "route.tableHeaderByCategory", "route.simpleTableHeader", ajaxData,
				messageSource, localeList, c.getLocalizedName(LocaleContextHolder.getLocale().toString()));

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("game", "route.game"));

		this.expandFrame = "/secure/iframe/route/{0}/:id";
	}

	public void fill(Game g) {
		for (Route r : g.getCategories()) {
			data.add(new TableRow(r, messageSource, locale));
		}
	}

	public List<TableRow> getData() {
		return data;
	}
}

package org.runcity.mvc.web.tabledata;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.service.RouteService;
import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.web.formdata.RouteCreateForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
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
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
		private Date gameDateFrom;

		@JsonView(ByCategory.class)
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
		private Date gameDateTo;

		@JsonView(ByCategory.class)
		private String gameCity;

		@JsonView(ByCategory.class)
		private String gameCountry;

		@JsonView(ByGame.class)
		private Long teamNumber;

		public TableRow(Route r, ApplicationContext context) {
			this.id = r.getId();
			this.categoryBadge = r.getCategory().getBadge();
			this.category = StringUtils.xss(r.getCategory().getLocalizedName(r.getGame().getLocale()));
			this.categoryDescription = StringUtils.xss(r.getCategory().getLocalizedDescription(r.getGame().getLocale()));
			this.game = StringUtils.xss(r.getGame().getName());
			this.gameDateFrom = r.getGame().getDateFrom();
			this.gameDateTo = r.getGame().getDateTo();
			this.gameCity = StringUtils.xss(r.getGame().getCity());
			this.gameCountry = StringUtils.xss(r.getGame().getCountry());
			
			if (context != null) {
				RouteService routeService = context.getBean(RouteService.class);
				this.teamNumber = routeService.selectTeamNumber(r);
			}
		}
	}

	public RouteTable(MessageSource messageSource, DynamicLocaleList localeList, Game g) {
		super("routeTable", "route.tableHeaderByGame", "route.simpleTableHeader", "/api/v1/routeTableByGame?gameId=" + g.getId(), messageSource, localeList,
				g.getName());

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("categoryBadge", "category.badge"));
		this.columns.add(new ColumnDefinition("category", "route.category").setSort("asc", 0));
		this.columns.add(new ColumnDefinition("teamNumber", "route.teamNumber"));
		
		this.extensions.add(new ColumnDefinition("categoryDescription", "category.description"));

		this.expandFrame = "/secure/iframe/route/{0}:id";

		this.buttons.add(new ButtonDefinition("actions.create", null, "btn", "createform:routeCreateForm", null));
		this.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn",
				"ajax:DELETE:/api/v1/routeDelete/:id", "selected"));

		RouteCreateForm createForm = new RouteCreateForm(localeList);
		createForm.setGameId(g.getId());
		this.relatedForms.add(createForm);
	}

	public RouteTable(MessageSource messageSource, DynamicLocaleList localeList, Category c) {
		super("routeTable", "route.tableHeaderByCategory", "route.simpleTableHeader", "/api/v1/routeTableByCategory?categoryId=" + c.getId(), messageSource,
				localeList, c.getLocalizedName(LocaleContextHolder.getLocale().toString()));

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("game", "route.game"));
		this.columns.add(new ColumnDefinition("gameCity", "game.city"));
		this.columns.add(new ColumnDefinition("gameCountry", "game.country"));
		this.columns.add(new ColumnDefinition("gameDateFrom", "game.dateFrom").setDateTimeFormat().setSort("desc", 0));
		this.columns.add(new ColumnDefinition("gameDateTo", "game.dateTo").setDateTimeFormat().setSort("desc", 1));

		this.expandFrame = "/secure/iframe/route/{0}:id";
	}

	public void fill(Game g, ApplicationContext context) {
		for (Route r : g.getCategories()) {
			data.add(new TableRow(r, context));
		}
	}

	public void fill(Category c) {
		for (Route r : c.getGames()) {
			data.add(new TableRow(r, null));
		}
	}

	public List<TableRow> getData() {
		return data;
	}
	
	@Override
	public RouteTable validate() {
		return this;
	}
}

package org.runcity.mvc.web.tabledata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.service.GameService;
import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.GameCreateEditForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormListboxLocaleColumn;
import org.runcity.mvc.web.util.FormListboxTimezoneColumn;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

public class GameTable extends AbstractTable {
	
	@JsonView(Views.Public.class)
	private List<TableRow> data = new LinkedList<TableRow>();
	
	protected class TableRow {
		@JsonView(Views.Public.class)
		private Long id;
		
		@JsonView(Views.Public.class)
		private String locale;
		
		@JsonView(Views.Public.class)
		private String name;
		
		@JsonView(Views.Public.class)
		private String city;
		
		@JsonView(Views.Public.class)
		private String country;
		
		@JsonView(Views.Public.class)
		private String timezone;
		
		@JsonView(Views.Public.class)
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
		private Date dateFrom;
		
		@JsonView(Views.Public.class)
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
		private Date dateTo;
		
		@JsonView(Views.Public.class)
		private String categories;

		public TableRow(Game g,  MessageSource messageSource, Locale l, DynamicLocaleList localeList) {
			this.id = g.getId();
			this.locale = new FormListboxLocaleColumn(localeList).getOptionDisplay(g.getLocale(), messageSource, l);
			this.name = StringUtils.xss(g.getName());
			this.city = StringUtils.xss(g.getCity());
			this.country = StringUtils.xss(g.getCountry());
			this.timezone = StringUtils.xss(new FormListboxTimezoneColumn().getOptionDisplay(g.getTimezone(), messageSource, l));
			this.dateFrom = g.getDateFrom();
			this.dateTo = g.getDateTo();
			
			List<String> categories = new ArrayList<String>(g.getCategories().size());
			for (Route gc : g.getCategories()) {
				categories.add(gc.getCategory().getLocalizedName(g.getLocale()));
			}
			Collections.sort(categories);
			this.categories = StringUtils.xss(StringUtils.toString(categories));
		}
		
		public Long getId() {
			return id;
		}

		public String getLocale() {
			return locale;
		}

		public String getName() {
			return name;
		}

		public String getCity() {
			return city;
		}

		public String getCountry() {
			return country;
		}

		public String getTimezone() {
			return timezone;
		}

		public Date getDateFrom() {
			return dateFrom;
		}

		public Date getDateTo() {
			return dateTo;
		}

		public String getCategories() {
			return categories;
		}
	}

	public GameTable(MessageSource messageSource, DynamicLocaleList localeList) {
		super("gameTable", "game.tableHeader", "game.tableHeader", "/api/v1/gameTable", messageSource, localeList);

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("name", "game.name"));
		this.columns.add(new ColumnDefinition("city", "game.city"));
		this.columns.add(new ColumnDefinition("country", "game.country"));
		this.columns.add(new ColumnDefinition("dateFrom", "game.dateFrom").setDateTimeFormat().setSort("desc", 0));
		this.columns.add(new ColumnDefinition("dateTo", "game.dateTo").setDateTimeFormat().setSort("desc", 1));
		this.columns.add(new ColumnDefinition("categories", "game.categories"));

		this.extensions.add(new ColumnDefinition("locale", "game.locale"));
		this.extensions.add(new ColumnDefinition("timezone", "game.timezone"));
		
		this.buttons.add(new ButtonDefinition("actions.create", null, "btn", "createform:gameCreateEditForm", null));
		this.buttons.add(new ButtonDefinition("actions.edit", null, "btn", "form:gameCreateEditForm:id", "selectedSingle"));
		this.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn", "ajax:DELETE:/api/v1/gameDelete/:id", "selected")); 
		this.buttons.add(new ButtonDefinition("controlPoint.link", null, "btn dt-link", "link:/secure/games/{0}/controlPoints:id", "selectedSingle"));
		this.buttons.add(new ButtonDefinition("routes.gameLink", null, "btn dt-link", "link:/secure/games/{0}/categories:id", "selectedSingle"));

		this.relatedForms.add(new GameCreateEditForm(localeList));
		
		this.expandFrame = "/secure/iframe/game/{0}:id";
	}
	
	public void fetchAll(GameService service, DynamicLocaleList localeList) {
		List<Game> games = service.selectAll(true);
		for (Game g : games) {
			data.add(new TableRow(g, messageSource, locale, localeList));
		}
	}	
	
	public List<TableRow> getData() {
		return data;
	}
}

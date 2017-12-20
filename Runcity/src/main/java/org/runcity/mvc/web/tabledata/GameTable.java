package org.runcity.mvc.web.tabledata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.runcity.db.service.GameService;
import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.GameCreateEditForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormListboxLocaleColumn;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

public class GameTable extends AbstractLocalizedTable {
	
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
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_FORMAT)
		private Date date;
		
		@JsonView(Views.Public.class)
		private String categories;

		public TableRow(Game g,  MessageSource messageSource, Locale l, DynamicLocaleList localeList) {
			id = g.getId();
			locale = new FormListboxLocaleColumn(localeList).getOptionDisplay(g.getLocale(), messageSource, l);
			name = StringUtils.xss(g.getName());
			city = StringUtils.xss(g.getCity());
			country = StringUtils.xss(g.getCountry());
			date = g.getDate();
			
			List<String> categories = new ArrayList<String>(g.getCategories().size());
			for (Category c : g.getCategories()) {
				categories.add(c.getNameDisplay(g.getLocale()));
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

		public Date getDate() {
			return date;
		}

		public String getCategories() {
			return categories;
		}
	}

	public GameTable(String ajaxData, MessageSource messageSource, DynamicLocaleList localeList) {
		super("gameTable", "game.tableHeader", ajaxData, messageSource, localeList);

		this.columns.add(new ColumnDefinition("id", null));
		this.columns.add(new ColumnDefinition("locale", "game.locale"));
		this.columns.add(new ColumnDefinition("name", "game.name"));
		this.columns.add(new ColumnDefinition("city", "game.city"));
		this.columns.add(new ColumnDefinition("country", "game.country"));
		this.columns.add(new ColumnDefinition("date", "game.date").setDate().setSort("desc"));
		this.columns.add(new ColumnDefinition("categories", "game.categories"));

		this.buttons.add(new ButtonDefinition("actions.create", null, "btn", "form:gameCreateEditForm", null));
		this.buttons.add(new ButtonDefinition("actions.edit", null, "btn", "form:gameCreateEditForm:id", "selectedSingle"));
		this.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn", "ajax:DELETE:/api/v1/gameDelete/:id", "selected"));

		this.relatedForms.add(new GameCreateEditForm(localeList));
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

package org.runcity.mvc.web.tabledata;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.runcity.db.entity.Category;
import org.runcity.db.entity.Translation;
import org.runcity.db.service.CategoryService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.CategoryCreateEditForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonView;

public class CategoryTable extends AbstractTable {
	
	@JsonView(Views.Public.class)
	private List<TableRow> data = new LinkedList<TableRow>();
	
	protected class TableRow {
		@JsonView(Views.Public.class)
		private Long id;
		
		@JsonView(Views.Public.class)
		private Map<String, String> name; 
		
		@JsonView(Views.Public.class)
		private String badge;

		@JsonView(Views.Public.class)
		private Map<String, String> description; 

		public TableRow(Category c, MessageSource messageSource, Locale l) {
			this.id = c.getId();
			this.badge = c.getBadge();
			
			this.name = localeList.prepareMap();
			
			for (Translation t : c.getNames()) {
				if (localeList.contains(t.getLocale())) {
					this.name.put(t.getLocale(), StringUtils.xss(t.getContent()));
				}
			}
			
			this.description = localeList.prepareMap();
			
			for (Translation t : c.getDescriptions()) {
				if (localeList.contains(t.getLocale())) {
					this.description.put(t.getLocale(), StringUtils.xss(t.getContent()));
				}
			}
		}

		public Long getId() {
			return id;
		}

		public Map<String, String> getNames() {
			return name;
		}
		
		public String getBadge() {
			return badge;
		}

		public Map<String, String> getDescription() {
			return description;
		}
	}

	public CategoryTable(MessageSource messageSource, DynamicLocaleList localeList) {
		super("categoryTable", "category.tableHeader", "category.tableHeader", "/api/v1/categoryTable", messageSource, localeList);

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("badge", "category.badge"));
		addLocalizedColumn(this.columns, "name", "category.nameLoc", "asc", 1);
		addLocalizedColumn(this.extensions, "description", "category.descriptionLoc");
		
		this.buttons.add(new ButtonDefinition("actions.create", null, "btn", "form:categoryCreateEditForm", null));
		this.buttons.add(new ButtonDefinition("actions.edit", null, "btn", "form:categoryCreateEditForm:id", "selectedSingle"));
		this.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn", "ajax:DELETE:/api/v1/categoryDelete/:id", "selected"));
		this.buttons.add(new ButtonDefinition("routes.categoryLink", null, "btn dt-link", "link:/secure/categories/{0}/games:id", "selectedSingle"));

		this.relatedForms.add(new CategoryCreateEditForm(localeList));
		
		this.expandFrame = ":id";
	}
	
	public void fetchAll(CategoryService service) {
		List<Category> categories = service.selectAll(false);
		for (Category c : categories) {
			data.add(new TableRow(c, messageSource, locale));
		}
	}	
	
	public List<TableRow> getData() {
		return data;
	}
}

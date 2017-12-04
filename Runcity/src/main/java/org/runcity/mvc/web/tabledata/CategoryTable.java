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

public class CategoryTable extends AbstractLocalizedTable {
	private static final String DEFAULT_POSTFIX = "XX";
	
	@JsonView(Views.Public.class)
	private List<TableRow> data = new LinkedList<TableRow>();
	
	protected class TableRow {
		@JsonView(Views.Public.class)
		private String id;
		
		@JsonView(Views.Public.class)
		private Map<String, String> name; 
		
		@JsonView(Views.Public.class)
		private String prefix;

		private String bgcolor;
		private String color;
		
		public TableRow(Category c, MessageSource messageSource, Locale l) {
			this.id = StringUtils.xss(c.getId());
			this.prefix = StringUtils.xss(c.getPrefix()) + DEFAULT_POSTFIX;
			this.bgcolor = StringUtils.xss(c.getBgcolor());
			this.color = StringUtils.xss(c.getColor());
			
			this.name = localeList.prepareMap();
			
			for (Translation t : c.getNames()) {
				if (localeList.contains(t.getLocale())) {
					this.name.put(t.getLocale(), t.getContent());
				}
			}
		}

		public String getId() {
			return id;
		}

		public Map<String, String> getNames() {
			return name;
		}

		public String getPrefix() {
			return "<span class='label' style='background-color: #" + bgcolor + "; color: #" + color + "'>" + prefix + "</span>";
		}
	}

	public CategoryTable(String ajaxData, MessageSource messageSource, DynamicLocaleList localeList) {
		super("categoryTable", "category.tableHeader", ajaxData, messageSource, localeList);

		this.columns.add(new ColumnDefinition("id", null));
		this.columns.add(new ColumnDefinition("prefix", "category.prefix"));
		addLocalizedColumn("name", "category.name");

		this.buttons.add(new ButtonDefinition("actions.create", null, "btn", "form:categoryCreateEditForm", null));
		this.buttons.add(new ButtonDefinition("actions.edit", null, "btn", "form:categoryCreateEditForm:id", "selectedSingle"));
		
		this.relatedForms.add(new CategoryCreateEditForm(localeList));
	}
	
	public void fetchAll(CategoryService service) {
		List<Category> categories = service.selectAll();
		for (Category c : categories) {
			data.add(new TableRow(c, messageSource, locale));
		}
	}	
	
	public List<TableRow> getData() {
		return data;
	}
}

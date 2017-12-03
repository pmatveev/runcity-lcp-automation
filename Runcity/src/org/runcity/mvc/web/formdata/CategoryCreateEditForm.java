package org.runcity.mvc.web.formdata;

import java.util.Map;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Category;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormIdColumn;
import org.runcity.mvc.web.util.FormLocalizedStringColumn;
import org.runcity.mvc.web.util.FormPlainStringColumn;
import org.runcity.mvc.web.util.FormStringColumn;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public class CategoryCreateEditForm extends AbstractLocalizedForm {
	private static final Logger logger = Logger.getLogger(CategoryCreateEditForm.class);
	
	@JsonView(Views.Public.class)
	private FormIdColumn id;

	@JsonView(Views.Public.class)
	private FormLocalizedStringColumn name;
	
	@JsonView(Views.Public.class)
	private FormPlainStringColumn prefix;
	
	public CategoryCreateEditForm() {
		this(null);
	}
	
	public CategoryCreateEditForm(DynamicLocaleList localeList) {
		super("categoryCreateEditForm", "/api/v1/categoryCreateEdit/{0}", null, "/api/v1/categoryCreateEdit", localeList);
		setTitle("category.header");
		this.id = new FormIdColumn(this, new ColumnDefinition("id", "id"));
		this.name = new FormLocalizedStringColumn(this, new ColumnDefinition("name", "category.namegroup", "category.name"), localeList, true, false, null, 32);
		this.prefix = new FormPlainStringColumn(this, new ColumnDefinition("prefix", "category.prefix"), true, 1, 3);
	}	
	
	public CategoryCreateEditForm(Long id, Map<String, String> name, String prefix, DynamicLocaleList localeList) {
		this(localeList);
		setId(id);
		setName(name);
		setPrefix(prefix);
	}
	
	public CategoryCreateEditForm(Category c, DynamicLocaleList localeList) {
		this(c.getId(), c.getStringNames(), c.getPrefix(), localeList);
	}

	public Long getId() {
		return id.getValue();
	}

	public void setId(Long id) {
		this.id.setValue(id);
	}
	
	public Map<String, String> getName() {
		return name.getValue();
	}
	
	public void setName(Map<String, String> name) {
		this.name.setValue(name);
	}
	
	public String getPrefix() {
		return prefix.getValue();
	}
	
	public void setPrefix(String prefix) {
		this.prefix.setValue(prefix);
	}
	
	public FormIdColumn getIdColumn() {
		return id;
	}
	
	public FormLocalizedStringColumn getNameColumn() {
		return name;
	}
	
	public FormStringColumn getPrefixColumn() {
		return prefix;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		id.validate(errors);
		name.validate(errors);
		prefix.validate(errors);
		// TODO
	}
}

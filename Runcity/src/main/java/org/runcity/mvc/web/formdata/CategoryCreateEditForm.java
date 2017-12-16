package org.runcity.mvc.web.formdata;

import java.util.Map;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Category;
import org.runcity.db.service.CategoryService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormColorPickerColumn;
import org.runcity.mvc.web.util.FormIdColumn;
import org.runcity.mvc.web.util.FormLocalizedStringColumn;
import org.runcity.mvc.web.util.FormPlainStringColumn;
import org.runcity.mvc.web.util.FormStringColumn;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
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

	@JsonView(Views.Public.class)
	private FormColorPickerColumn fontColor;

	@JsonView(Views.Public.class)
	private FormColorPickerColumn bgColor;

	@JsonView(Views.Public.class)
	private FormLocalizedStringColumn description;

	public CategoryCreateEditForm() {
		this(null);
	}

	public CategoryCreateEditForm(DynamicLocaleList localeList) {
		super("categoryCreateEditForm", "/api/v1/categoryCreateEdit/{0}", null, "/api/v1/categoryCreateEdit",
				localeList);
		setTitle("category.header");
		this.id = new FormIdColumn(this, new ColumnDefinition("id", "id"));
		this.name = new FormLocalizedStringColumn(this,
				new ColumnDefinition("name", "category.namegroup", "category.name"), localeList, false, true, false,
				null, 32);
		this.prefix = new FormPlainStringColumn(this, new ColumnDefinition("prefix", "category.prefix"), true, 1, 3);
		this.fontColor = new FormColorPickerColumn(this, new ColumnDefinition("fontColor", "category.fontColor"), true,
				"ffffff");
		this.bgColor = new FormColorPickerColumn(this, new ColumnDefinition("bgColor", "category.bgColor"), true,
				"000000");
		this.description = new FormLocalizedStringColumn(this,
				new ColumnDefinition("description", "category.descriptiongroup", "category.description"), localeList,
				true, false, false, null, 4000);
	}

	public CategoryCreateEditForm(Long id, Map<String, String> name, String prefix, String fontColor, String bgColor, Map<String, String> description,
			DynamicLocaleList localeList) {
		this(localeList);
		setId(id);
		setName(name);
		setPrefix(prefix);
		setFontColor(fontColor);
		setBgColor(bgColor);
		setDescription(description);
	}

	public CategoryCreateEditForm(Category c, DynamicLocaleList localeList) {
		this(c.getId(), c.getStringNames(), c.getPrefix(), c.getColor(), c.getBgcolor(), c.getStringDescriptions(), localeList);
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

	public void setName(String locale, String content) {
		this.name.put(locale, content);
	}

	public String getPrefix() {
		return prefix.getValue();
	}

	public void setPrefix(String prefix) {
		this.prefix.setValue(prefix);
	}

	public String getFontColor() {
		return fontColor.getValue();
	}

	public void setFontColor(String color) {
		this.fontColor.setValue(color);
	}

	public String getBgColor() {
		return bgColor.getValue();
	}

	public void setBgColor(String color) {
		this.bgColor.setValue(color);
	}

	public Map<String, String> getDescription() {
		return description.getValue();
	}

	public void setDescription(Map<String, String> description) {
		this.description.setValue(description);
	}

	public void setDescription(String locale, String content) {
		this.description.put(locale, content);
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

	public FormStringColumn getFontColorColumn() {
		return fontColor;
	}

	public FormStringColumn getBgColorColumn() {
		return bgColor;
	}

	public FormLocalizedStringColumn getDescriptionColumn() {
		return description;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		id.validate(context, errors);
		name.validate(context, errors);
		prefix.validate(context, errors);
		description.validate(context, errors);

		if (getId() != null) {
			CategoryService categoryService = context.getBean(CategoryService.class);
			Category c = categoryService.selectById(getId());

			if (c == null) {
				errors.reject("common.notFoundId", new Object[] { getId() }, null);
				return;
			}

			if (!StringUtils.isEqual(prefix.getValue(), c.getPrefix())) {
				c = categoryService.selectWithGames(getId());
				if (c.getGames().size() > 0) {
					errors.rejectValue(prefix.getName(), "category.prefixChangeError");
				}
			}
		}
	}

	public Category getCategory() {
		Category c = new Category(getId(), null, getBgColor(), getFontColor(), getPrefix(), null);
		
		for (String s : getName().keySet()) {
			c.addName(s, getName().get(s));
		}
		
		for (String s : getDescription().keySet()) {
			c.addDescription(s, getDescription().get(s));
		}
		return c;
	}
}

package org.runcity.mvc.web.formdata;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormDateColumn;
import org.runcity.mvc.web.util.FormDddwCategoriesColumn;
import org.runcity.mvc.web.util.FormIdColumn;
import org.runcity.mvc.web.util.FormListboxLocaleColumn;
import org.runcity.mvc.web.util.FormPlainStringColumn;
import org.runcity.mvc.web.util.FormStringColumn;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.ObjectUtils;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

public class GameCreateEditForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(GameCreateEditForm.class);

	@JsonView(Views.Public.class)
	private FormIdColumn id;

	@JsonView(Views.Public.class)
	private FormListboxLocaleColumn locale;

	@JsonView(Views.Public.class)
	private FormPlainStringColumn name;

	@JsonView(Views.Public.class)
	private FormPlainStringColumn city;

	@JsonView(Views.Public.class)
	private FormPlainStringColumn country;

	@JsonView(Views.Public.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_FORMAT)
	private FormDateColumn date;

	@JsonView(Views.Public.class)
	private FormDddwCategoriesColumn categories;

	public GameCreateEditForm() {
		this(null);
	}

	public GameCreateEditForm(DynamicLocaleList localeList) {
		super("gameCreateEditForm", "/api/v1/gameCreateEdit/{0}", null, "/api/v1/gameCreateEdit", localeList);
		logger.trace("Creating form " + getFormName());
		setTitle("game.header");

		this.id = new FormIdColumn(this, new ColumnDefinition("id", "id"));
		this.locale = new FormListboxLocaleColumn(this, new ColumnDefinition("locale", "game.locale"), localeList);
		this.locale.setRequired(true);
		this.name = new FormPlainStringColumn(this, new ColumnDefinition("name", "game.name"));
		this.name.setRequired(true);
		this.name.setMaxLength(32);
		this.city = new FormPlainStringColumn(this, new ColumnDefinition("city", "game.city"));
		this.city.setRequired(true);
		this.city.setMaxLength(32);
		this.country = new FormPlainStringColumn(this, new ColumnDefinition("country", "game.country"));
		this.country.setRequired(true);
		this.country.setMaxLength(32);
		this.date = new FormDateColumn(this, new ColumnDefinition("date", "game.date"), true);
		this.categories = FormDddwCategoriesColumn.getAllByLocale(this,
				new ColumnDefinition("categories", "game.categories"), locale);
		this.categories.setRequired(true);
	}

	public GameCreateEditForm(Long id, String locale, String name, String city, String country, Date date,
			List<Long> categories, DynamicLocaleList localeList) {
		this(localeList);
		setId(id);
		setLocale(locale);
		setName(name);
		setCity(city);
		setCountry(country);
		setDate(date);
		setCategories(categories);
	}

	public GameCreateEditForm(Game g, DynamicLocaleList localeList) {
		this(g.getId(), g.getLocale(), g.getName(), g.getCity(), g.getCountry(), g.getDate(), g.getCategoryIds(),
				localeList);
	}

	public Long getId() {
		return id.getValue();
	}

	public void setId(Long id) {
		this.id.setValue(id);
	}

	public String getLocale() {
		return locale.getValue();
	}

	public void setLocale(String locale) {
		this.locale.setValue(locale);
	}

	public String getName() {
		return name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public String getCity() {
		return city.getValue();
	}

	public void setCity(String city) {
		this.city.setValue(city);
	}

	public String getCountry() {
		return country.getValue();
	}

	public void setCountry(String country) {
		this.country.setValue(country);
	}

	public Date getDate() {
		return date.getValue();
	}

	public void setDate(Date date) {
		this.date.setValue(date);
	}

	public List<Long> getCategories() {
		return categories.getValue();
	}

	public void setCategories(List<Long> categories) {
		this.categories.setValue(categories);
	}

	public FormIdColumn getIdColumn() {
		return id;
	}

	public FormListboxLocaleColumn getLocaleColumn() {
		return locale;
	}

	public FormStringColumn getNameColumn() {
		return name;
	}

	public FormStringColumn getCityColumn() {
		return city;
	}

	public FormStringColumn getCountryColumn() {
		return country;
	}

	public FormDateColumn getDateColumn() {
		return date;
	}

	public FormDddwCategoriesColumn getCategoriesColumn() {
		return categories;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		id.validate(context, errors);
		locale.validate(context, errors);
		name.validate(context, errors);
		city.validate(context, errors);
		country.validate(context, errors);
		date.validate(context, errors);
		categories.validate(context, errors);

		for (Category c1 : categories.getCategories()) {
			for (Category c2 : categories.getCategories()) {
				if (ObjectUtils.nullSafeEquals(c1.getId(), c2.getId())) {
					continue;
				}
				if (StringUtils.isEmpty(c1.getPrefix())) {
					// should never be executed but still better to check
					errors.rejectValue(categories.getName(), "game.noPrefixCategory",
							new Object[] { c1.getNameDisplay(locale.getValue()) }, null);
					continue;
				}
				if (c1.getPrefix().startsWith(c2.getPrefix())) {
					errors.rejectValue(categories.getName(), "game.prefixConflict",
							new Object[] { c1.getNameDisplay(locale.getValue()), c2.getNameDisplay(locale.getValue()),
									c1.getPrefix(), c2.getPrefix() },
							null);
				}
			}
		}
	}

	public Game getGame() {
		return new Game(getId(), getLocale(), getName(), getCity(), getCountry(), getDate(),
				categories.getCategories());
	}
}

package org.runcity.mvc.web.formdata;

import java.util.Map;

import org.apache.log4j.Logger;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.enumeration.ControlPointType;
import org.runcity.db.service.ControlPointService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormColumn;
import org.runcity.mvc.web.util.FormDddwControlPointColumn;
import org.runcity.mvc.web.util.FormFileColumn;
import org.runcity.mvc.web.util.FormIdGameColumn;
import org.runcity.mvc.web.util.FormListboxControlPointTypeColumn;
import org.runcity.mvc.web.util.FormIdColumn;
import org.runcity.mvc.web.util.FormLocalizedStringColumn;
import org.runcity.mvc.web.util.FormPlainStringColumn;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public class ControlPointCreateEditByGameForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(ControlPointCreateEditByGameForm.class);

	@JsonView(Views.Public.class)
	private FormIdColumn id;

	@JsonView(Views.Public.class)
	private FormIdGameColumn gameId;

	@JsonView(Views.Public.class)
	private FormListboxControlPointTypeColumn type;

	@JsonView(Views.Public.class)
	private FormDddwControlPointColumn parent;

	@JsonView(Views.Public.class)
	private FormPlainStringColumn idt;

	@JsonView(Views.Public.class)
	private FormPlainStringColumn name;

	@JsonView(Views.Public.class)
	private FormLocalizedStringColumn address;

	@JsonView(Views.Public.class)
	private FormPlainStringColumn description;

	@JsonView(Views.Public.class)
	private FormFileColumn image;

	public ControlPointCreateEditByGameForm() {
		this(null);
	}

	public ControlPointCreateEditByGameForm(DynamicLocaleList localeList) {
		super("controlPointCreateEditByGameForm", "/api/v1/controlPointCreateEdit/{0}", null,
				"/api/v1/controlPointCreateEdit", localeList);
		setTitle("controlPoint.header");
		this.id = new FormIdColumn(this, new ColumnDefinition("id", "id"));
		this.gameId = new FormIdGameColumn(this, new ColumnDefinition("gameId", "gameid"));
		this.type = new FormListboxControlPointTypeColumn(this, new ColumnDefinition("type", "controlPoint.type"));
		this.type.setRequired(true);
		this.parent = FormDddwControlPointColumn.getMainByGameNotSelf(this,
				new ColumnDefinition("parent", "controlPoint.parent"), id, gameId);
		this.parent.setForceRefresh(true);
		this.idt = new FormPlainStringColumn(this, new ColumnDefinition("idt", "controlPoint.idt"));
		this.idt.setRequired(true);
		this.idt.setMaxLength(16);
		this.name = new FormPlainStringColumn(this, new ColumnDefinition("name", "controlPoint.name"));
		this.name.setRequired(true);
		this.name.setMaxLength(32);
		this.address = new FormLocalizedStringColumn(this,
				new ColumnDefinition("address", "controlPoint.address", "controlPoint.addressLoc"), localeList, true,
				true, false, null, 4000);
		this.address.setShowCondition("emptyValue({0})", this.parent);
		this.description = new FormPlainStringColumn(this,
				new ColumnDefinition("description", "controlPoint.description"));
		this.description.setRequired(true);
		this.description.setLongValue(true);
		this.description.setMaxLength(4000);
		this.image = new FormFileColumn(this, new ColumnDefinition("image", "controlPoint.image"),
				"/api/v1/uploadImage", "/secure/controlPointImage?id={0}", new FormColumn<?>[] { this.id }, null);
	}

	public ControlPointCreateEditByGameForm(Long id, Long gameId, String type, Long parent, String idt, String name,
			Map<String, String> address, String description, String image, DynamicLocaleList localeList) {
		this(localeList);
		setId(id);
		setGameId(gameId);
		setType(type);
		setParent(parent);
		setIdt(idt);
		setName(name);
		setAddress(address);
		setDescription(description);
		setImage(image);
	}

	public ControlPointCreateEditByGameForm(ControlPoint c, DynamicLocaleList localeList) {
		this(c.getId(), c.getGame().getId(), ControlPointType.getStoredValue(c.getType()),
				c.getParent() == null ? null : c.getParent().getId(), c.getIdt(), c.getName(),
				c.getParent() == null ? c.getStringAddresses() : c.getParent().getStringAddresses(), c.getDescription(),
				StringUtils.toNvlString(c.getImage()), localeList);
	}

	public Long getId() {
		return id.getValue();
	}

	public void setId(Long id) {
		this.id.setValue(id);
	}

	public Long getGameId() {
		return gameId.getValue();
	}

	public void setGameId(Long gameId) {
		this.gameId.setValue(gameId);
	}

	public String getType() {
		return type.getValue();
	}

	public void setType(String type) {
		this.type.setValue(type);
	}

	public Long getParent() {
		return parent.getValue();
	}

	public void setParent(Long parent) {
		this.parent.setValue(parent);
	}

	public String getIdt() {
		return idt.getValue();
	}

	public void setIdt(String idt) {
		this.idt.setValue(idt);
	}

	public String getName() {
		return name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public Map<String, String> getAddress() {
		return address.getValue();
	}

	public void setAddress(Map<String, String> address) {
		this.address.setValue(address);
	}

	public void setAddress(String locale, String content) {
		this.address.put(locale, content);
	}

	public String getDescription() {
		return description.getValue();
	}

	public void setDescription(String description) {
		this.description.setValue(description);
	}

	public String getImage() {
		return image.getValue();
	}

	public void setImage(String image) {
		this.image.setValue(image);
	}

	public FormIdColumn getIdColumn() {
		return id;
	}

	public FormIdColumn getGameIdColumn() {
		return gameId;
	}

	public FormListboxControlPointTypeColumn getTypeColumn() {
		return type;
	}

	public FormDddwControlPointColumn getParentColumn() {
		return parent;
	}

	public FormPlainStringColumn getIdtColumn() {
		return idt;
	}

	public FormPlainStringColumn getNameColumn() {
		return name;
	}

	public FormLocalizedStringColumn getAddressColumn() {
		return address;
	}

	public FormPlainStringColumn getDescriptionColumn() {
		return description;
	}

	public FormFileColumn getImageColumn() {
		return image;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		id.validate(context, errors);
		gameId.validate(context, errors);
		type.validate(context, errors);
		parent.validate(context, errors);
		idt.validate(context, errors);
		name.validate(context, errors);

		if (parent.getControlPoint() == null) {
			address.validate(context, errors);
		} else {
			address.clearValue();
		}
		description.validate(context, errors);
		image.validate(context, errors);

		ControlPointService controlPointService = context.getBean(ControlPointService.class);
		if (getParent() != null && getId() != null && !controlPointService.selectByParent(getId()).isEmpty()) {
			errors.rejectValue(parent.getName(), "controlPoint.errorParentChain");
		}
	}

	public ControlPoint getControlPoint() {
		ControlPoint c = new ControlPoint(getId(), gameId.getGame(), ControlPointType.getByStoredValue(type.getValue()),
				parent.getControlPoint(), getIdt(), getName(), null, getDescription(), null);
		c.setImageData(image.getByteValue());

		for (String s : getAddress().keySet()) {
			c.addAddress(s, getAddress().get(s));
		}

		return c;
	}
}

package org.runcity.mvc.web.formdata;

import java.util.Map;

import org.apache.log4j.Logger;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormFileColumn;
import org.runcity.mvc.web.util.FormIdColumn;
import org.runcity.mvc.web.util.FormLocalizedStringColumn;
import org.runcity.mvc.web.util.FormPlainStringColumn;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public class ControlPointCreateEditByGameForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(ControlPointCreateEditByGameForm.class);

	@JsonView(Views.Public.class)
	private FormIdColumn id;

	@JsonView(Views.Public.class)
	private FormIdColumn gameId;

	// TODO parent

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
		super("controlPointCreateEditByGameForm", "/api/v1/cpCreateEdit/{0}", null, "/api/v1/cpCreateEdit",
				localeList);
		setTitle("controlPoint.header");
		this.id = new FormIdColumn(this, new ColumnDefinition("id", "id"));
		this.gameId = new FormIdColumn(this, new ColumnDefinition("gameId", "gameid"));
		this.idt = new FormPlainStringColumn(this, new ColumnDefinition("idt", "controlPoint.idt"), false, true, 0, 16);
		this.name = new FormPlainStringColumn(this, new ColumnDefinition("name", "controlPoint.name"), false, true, 0, 32);
		this.address = new FormLocalizedStringColumn(this,
				new ColumnDefinition("address", "controlPoint.addressgroup", "controlPoint.address"), localeList, true, true, false,
				null, 4000);
		this.description = new FormPlainStringColumn(this, new ColumnDefinition("description", "controlPoint.description"), true, true, 0, 4000);
		this.image = new FormFileColumn(this, new ColumnDefinition("image", "controlPoint.image"), null);
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

	public Byte[] getImage() {
		return image.getValue();
	}

	public void setImage(Byte[] image) {
		this.image.setValue(image);
	}

	public FormIdColumn getIdColumn() {
		return id;
	}

	public FormIdColumn getGameIdColumn() {
		return gameId;
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
		// TODO Auto-generated method stub
	}
}

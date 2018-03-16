package org.runcity.mvc.web.formdata;

import java.util.Date;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.enumeration.SecureUserRole;
import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormDateColumn;
import org.runcity.mvc.web.util.FormDddwConsumerColumn;
import org.runcity.mvc.web.util.FormIdColumn;
import org.runcity.mvc.web.util.FormIdGameColumn;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

public class VolunteerCreateEditByGameCoordForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(VolunteerCreateEditByGameCoordForm.class);

	@JsonView(Views.Public.class)
	private FormIdColumn id;

	@JsonView(Views.Public.class)
	private FormIdGameColumn gameId;

	@JsonView(Views.Public.class)
	private FormDddwConsumerColumn consumer;

	@JsonView(Views.Public.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
	private FormDateColumn dateFrom;

	@JsonView(Views.Public.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
	private FormDateColumn dateTo;

	public VolunteerCreateEditByGameCoordForm() {
		this(null);
	}

	public VolunteerCreateEditByGameCoordForm(DynamicLocaleList localeList) {
		super("volunteerCreateEditByGameCoordForm", "/api/v1/volunteerCreateEditByGameCoord/{0}",
				"/api/v1/volunteerCreateEditByGameCoord", localeList);
		logger.trace("Creating form " + getFormName());
		setTitle("volunteer.header");

		this.id = new FormIdColumn(this, new ColumnDefinition("id", "id"));
		this.gameId = new FormIdGameColumn(this, new ColumnDefinition("gameId", "gameId"));

		this.consumer = FormDddwConsumerColumn.getAll(this, new ColumnDefinition("consumer", "volunteer.consumer"),
				true, SecureUserRole.VOLUNTEER);
		this.consumer.setRequired(true);

		this.dateFrom = new FormDateColumn(this, new ColumnDefinition("dateFrom", "volunteer.dateFrom"));
		this.dateFrom.setTimeValue(true);
		this.dateFrom.setRequired(true);

		this.dateTo = new FormDateColumn(this, new ColumnDefinition("dateTo", "volunteer.dateTo"));
		this.dateTo.setTimeValue(true);
		this.dateTo.setRequired(true);
	}

	public VolunteerCreateEditByGameCoordForm(Long id, Long gameId, Long consumer, Date dateFrom, Date dateTo,
			DynamicLocaleList localeList) {
		this(localeList);
		setId(id);
		setGameId(gameId);
		setConsumer(consumer);
		setDateFrom(dateFrom);
		setDateTo(dateTo);
	}

	public VolunteerCreateEditByGameCoordForm(Volunteer v, DynamicLocaleList localeList) {
		this(v.getId(), v.getGame() == null ? null : v.getGame().getId(),
				v.getConsumer() == null ? null : v.getConsumer().getId(), v.getDateFrom(), v.getDateTo(), localeList);
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

	public Long getConsumer() {
		return consumer.getValue();
	}

	public void setConsumer(Long consumer) {
		this.consumer.setValue(consumer);
	}

	public Date getDateFrom() {
		return dateFrom.getValue();
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom.setValue(dateFrom);
	}

	public Date getDateTo() {
		return dateTo.getValue();
	}

	public void setDateTo(Date dateTo) {
		this.dateTo.setValue(dateTo);
	}

	public FormIdColumn getIdColumn() {
		return id;
	}

	public FormIdGameColumn getGameIdColumn() {
		return gameId;
	}

	public FormDddwConsumerColumn getConsumerColumn() {
		return consumer;
	}

	public FormDateColumn getDateFromColumn() {
		return dateFrom;
	}

	public FormDateColumn getDateToColumn() {
		return dateTo;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		id.validate(context, errors);
		gameId.validate(context, errors);
		consumer.validate(context, errors);
		dateFrom.validate(context, errors);
		dateTo.validate(context, errors);
	}

	public Volunteer getVolunteer() {
		return new Volunteer(getId(), consumer.getConsumer(), null, gameId.getGame(), getDateFrom(), getDateTo());
	}
}

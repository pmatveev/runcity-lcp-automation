package org.runcity.mvc.web.formdata;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.Team;
import org.runcity.db.service.TeamService;
import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormDateColumn;
import org.runcity.mvc.web.util.FormIdColumn;
import org.runcity.mvc.web.util.FormIdRouteColumn;
import org.runcity.mvc.web.util.FormPlainStringColumn;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

public class TeamCreateEditByRouteForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(TeamCreateEditByRouteForm.class);
	private static final String NO_SHOW = "1 == 2";

	@JsonView(Views.Public.class)
	private FormIdColumn id;

	@JsonView(Views.Public.class)
	private FormIdRouteColumn routeId;

	@JsonView(Views.Public.class)
	private FormPlainStringColumn number;

	@JsonView(Views.Public.class)
	private FormPlainStringColumn name;

	@JsonView(Views.Public.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
	private FormDateColumn start;

	@JsonView(Views.Public.class)
	private FormPlainStringColumn contact;

	@JsonView(Views.Public.class)
	private FormPlainStringColumn addData;

	public TeamCreateEditByRouteForm() {
		this(null);
	}

	public TeamCreateEditByRouteForm(DynamicLocaleList localeList) {
		super("teamCreateEditByRouteForm", "/api/v1/teamCreateEdit/{0}", null, "/api/v1/teamCreateEdit/",
				localeList);
		logger.trace("Creating form " + getFormName());
		setTitle("team.header");

		this.id = new FormIdColumn(this, new ColumnDefinition("id", "id"));
		this.routeId = new FormIdRouteColumn(this, new ColumnDefinition("routeId", "routeid"));
		this.number = new FormPlainStringColumn(this, new ColumnDefinition("number", "team.number"));
		this.number.setRequired(true);
		this.number.setMaxLength(32);
		this.name = new FormPlainStringColumn(this, new ColumnDefinition("name", "team.name"));
		this.name.setRequired(true);
		this.name.setMaxLength(255);
		this.start = new FormDateColumn(this, new ColumnDefinition("start", "team.start"));
		this.start.setTimeValue(true);
		this.start.setRequired(true);
		this.contact = new FormPlainStringColumn(this, new ColumnDefinition("contact", "team.contact"));
		this.contact.setRequired(true);
		this.contact.setMaxLength(255);
		this.addData = new FormPlainStringColumn(this, new ColumnDefinition("addData", "team.addData"));
		this.addData.setLongValue(true);
		this.addData.setMaxLength(4000);
	}

	public TeamCreateEditByRouteForm(Long id, Long routeId, String number, String name, Date start, String contact,
			String addData, DynamicLocaleList localeList) {
		this(localeList);
		setId(id);
		setRouteId(routeId);
		setNumber(number);
		setName(name);
		setStart(start);
		setContact(contact);
		setAddData(addData);
	}

	public TeamCreateEditByRouteForm(Team t, DynamicLocaleList localeList) {
		this(t.getId(), t.getRoute().getId(), t.getNumber(), t.getName(), t.getStart(), t.getContact(), t.getAddData(),
				localeList);
	}

	public void prepare(Route r) {
		if (r == null) {
			return;
		}

		if (getRouteId() == null) {
			setRouteId(r.getId());
		}
		if (r.getGame() != null && r.getGame().getDelay() != null) {
			this.start.setShowCondition(NO_SHOW);
		}
		if (r.getCategory() != null && getNumber() == null) {
			setNumber(r.getCategory().getPrefix());
		}
	}

	public Long getId() {
		return id.getValue();
	}

	public void setId(Long id) {
		this.id.setValue(id);
	}

	public Long getRouteId() {
		return routeId.getValue();
	}

	public void setRouteId(Long routeId) {
		this.routeId.setValue(routeId);
	}

	public String getNumber() {
		return number.getValue();
	}

	public void setNumber(String number) {
		this.number.setValue(number);
	}

	public String getName() {
		return name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public Date getStart() {
		return start.getValue();
	}

	public void setStart(Date start) {
		this.start.setValue(start);
	}

	public String getContact() {
		return contact.getValue();
	}

	public void setContact(String contact) {
		this.contact.setValue(contact);
	}

	public String getAddData() {
		return addData.getValue();
	}

	public void setAddData(String addData) {
		this.addData.setValue(addData);
	}

	public FormIdColumn getIdColumn() {
		return id;
	}

	public FormIdRouteColumn getRouteIdColumn() {
		return routeId;
	}

	public FormPlainStringColumn getNumberColumn() {
		return number;
	}

	public FormPlainStringColumn getNameColumn() {
		return name;
	}

	public FormDateColumn getStartColumn() {
		return start;
	}

	public FormPlainStringColumn getContactColumn() {
		return contact;
	}

	public FormPlainStringColumn getAddDataColumn() {
		return addData;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		id.validate(context, errors);
		routeId.validate(context, errors);
		Route r = routeId.getRoute();

		prepare(routeId.getRoute());

		number.validate(context, errors);

		if (r != null) {
			if (!StringUtils.isEqualPrefix(getNumber(), r.getCategory().getPrefix(),
					r.getCategory().getPrefix().length())) {
				errors.rejectValue(number.getName(), "team.validation.numberPrefix",
						new Object[] { r.getCategory().getPrefix() }, null);
			}
		}

		Integer num = null;
		if (r != null && getNumber() != null) {
			try {
				num = new Integer(getNumber().substring(r.getCategory().getPrefix().length()));
			} catch (Throwable t) {
				errors.rejectValue(number.getName(), "team.validation.numberFormat");
			}
		}

		name.validate(context, errors);

		if (!NO_SHOW.equals(start.getShowCondition())) {
			start.validate(context, errors);
		} else if (r != null && num != null) {
			Game g = r.getGame();
			Calendar c = Calendar.getInstance();
			c.setTime(g.getDateFrom());
			c.add(Calendar.MINUTE, g.getDelay() * num);
			setStart(c.getTime());
		}

		contact.validate(context, errors);
		addData.validate(context, errors);
		
		if (r != null) {
			TeamService teamService = context.getBean(TeamService.class);
			Team t = teamService.selectByNumberGame(getNumber(), r.getGame());
			
			if (t != null && !ObjectUtils.nullSafeEquals(t.getId(), getId())) {
				errors.rejectValue(number.getName(), "team.validation.numberUsed");				
			}
		}
	}

	public Team getTeam() {
		return new Team(getId(), routeId.getRoute(), getNumber(), getName(), getStart(), getContact(), getAddData());
	}

}

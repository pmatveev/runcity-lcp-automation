package org.runcity.mvc.web.tabledata;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Volunteer;
import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.web.formdata.VolunteerCreateEditByCPForm;
import org.runcity.mvc.web.formdata.VolunteerCreateEditByGameCPForm;
import org.runcity.mvc.web.formdata.VolunteerCreateEditByGameCoordForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

public class VolunteerTable extends AbstractTable {
	public static class ByControlPoint {
	}

	public static class ByConsumerGame {
	}
	
	public static class ByConsumerControlPoint {
	}

	public static class ByGame {
	}

	public static class ByGameControlPoint {
	}

	@JsonView({ ByControlPoint.class, ByConsumerGame.class, ByConsumerControlPoint.class, ByGame.class, ByGameControlPoint.class })
	private List<TableRow> data = new LinkedList<TableRow>();

	protected class TableRow {
		@JsonView({ ByControlPoint.class, ByConsumerGame.class, ByConsumerControlPoint.class, ByGame.class, ByGameControlPoint.class })
		private Long id;

		@JsonView({ ByControlPoint.class, ByGame.class, ByGameControlPoint.class })
		private String name;

		@JsonView({ ByConsumerGame.class, ByConsumerControlPoint.class })
		private String game;

		@JsonView({ ByConsumerControlPoint.class, ByGameControlPoint.class })
		private String controlPoint;

		@JsonView({ ByControlPoint.class, ByConsumerGame.class, ByConsumerControlPoint.class, ByGame.class, ByGameControlPoint.class })
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
		private Date dateFrom;

		@JsonView({ ByControlPoint.class, ByConsumerGame.class, ByConsumerControlPoint.class, ByGame.class, ByGameControlPoint.class })
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
		private Date dateTo;

		public TableRow(Volunteer v, MessageSource messageSource, Locale l) {
			this.id = v.getId();
			this.name = StringUtils.xss(v.getConsumer().getCredentials());
			this.game = v.getGame() != null ? StringUtils.xss(v.getGame().getDisplay())
					: StringUtils.xss(v.getControlPoint().getGame().getDisplay());
			this.controlPoint = v.getControlPoint() != null ? StringUtils.xss(v.getControlPoint().getNameDisplay())
					: null;
			this.dateFrom = v.getDateFrom();
			this.dateTo = v.getDateTo();
		}
	}

	protected VolunteerTable(String id, String title, String simpleTitle, String ajaxData, MessageSource messageSource, DynamicLocaleList localeList, Object ... titleArgs) {
		super(id, title, simpleTitle, ajaxData, messageSource, localeList, titleArgs);
	}
	
	public static VolunteerTable initVolunteersByGame(MessageSource messageSource, DynamicLocaleList localeList, Game g) {
		VolunteerTable table = new VolunteerTable("volunteerTableByGame", "volunteer.tableHeaderByGameControlPoint", "volunteer.simpleTableHeaderByGameControlPoint", "/api/v1/volunteerTableByGame?gameId=" + g.getId(), messageSource, localeList, g.getName());
		
		table.columns.add(new ColumnDefinition("id", null).setHidden(true));
		table.columns.add(new ColumnDefinition("controlPoint", "volunteer.controlPoint"));
		table.columns.add(new ColumnDefinition("name", "volunteer.name").setSort("asc", 0));
		table.columns.add(new ColumnDefinition("dateFrom", "volunteer.dateFrom").setDateTimeFormat());
		table.columns.add(new ColumnDefinition("dateTo", "volunteer.dateTo").setDateTimeFormat());
		
		table.buttons.add(new ButtonDefinition("actions.create", null, "btn", "createform:volunteerCreateEditByGameCPForm", null));
		table.buttons.add(new ButtonDefinition("actions.edit", null, "btn", "form:volunteerCreateEditByGameCPForm:id", "selectedSingle"));
		table.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn", "ajax:DELETE:/api/v1/volunteerDelete/:id", "selected")); 
		
		VolunteerCreateEditByGameCPForm form = new VolunteerCreateEditByGameCPForm(localeList);
		form.setGameId(g.getId());
		form.setDateFrom(g.getDateFrom());
		form.setDateTo(g.getDateTo());
		table.relatedForms.add(form);
		return table;
	}
	
	public static VolunteerTable initCoordinatorsByGame(MessageSource messageSource, DynamicLocaleList localeList, Game g) {
		VolunteerTable table = new VolunteerTable("coordinatorTableByGame", "volunteer.tableHeaderByGame", "volunteer.simpleTableHeaderByGame", "/api/v1/coordinatorTableByGame?gameId=" + g.getId(), messageSource, localeList, g.getName());
		
		table.columns.add(new ColumnDefinition("id", null).setHidden(true));
		table.columns.add(new ColumnDefinition("name", "volunteer.name").setSort("asc", 0));
		table.columns.add(new ColumnDefinition("dateFrom", "volunteer.dateFrom").setDateTimeFormat());
		table.columns.add(new ColumnDefinition("dateTo", "volunteer.dateTo").setDateTimeFormat());
		
		table.buttons.add(new ButtonDefinition("actions.create", null, "btn", "createform:volunteerCreateEditByGameCoordForm", null));
		table.buttons.add(new ButtonDefinition("actions.edit", null, "btn", "form:volunteerCreateEditByGameCoordForm:id", "selectedSingle"));
		table.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn", "ajax:DELETE:/api/v1/volunteerDelete/:id", "selected")); 
		
		VolunteerCreateEditByGameCoordForm form = new VolunteerCreateEditByGameCoordForm(localeList);
		form.setGameId(g.getId());
		form.setDateFrom(g.getDateFrom());
		form.setDateTo(g.getDateTo());
		table.relatedForms.add(form);
		return table;		
	}

	public static VolunteerTable initVolunteersByConsumer(MessageSource messageSource, DynamicLocaleList localeList, Consumer c) {
		VolunteerTable table = new VolunteerTable("volunteerTableByConsumer", "volunteer.tableHeaderByConsumerControlPoint", "volunteer.simpleTableHeaderByConsumerControlPoint", "/api/v1/volunteerTableByConsumer?consumerId=" + c.getId(), messageSource, localeList, c.getCredentials());
		
		table.columns.add(new ColumnDefinition("id", null).setHidden(true));
		table.columns.add(new ColumnDefinition("game", "volunteer.game"));
		table.columns.add(new ColumnDefinition("controlPoint", "volunteer.controlPoint"));
		table.columns.add(new ColumnDefinition("dateFrom", "volunteer.dateFrom").setDateTimeFormat().setSort("desc", 0));
		table.columns.add(new ColumnDefinition("dateTo", "volunteer.dateTo").setDateTimeFormat());
		
		return table;
	}

	public static VolunteerTable initCoordinatorsByConsumer(MessageSource messageSource, DynamicLocaleList localeList, Consumer c) {
		VolunteerTable table = new VolunteerTable("coordinatorTableByConsumer", "volunteer.tableHeaderByConsumerGame", "volunteer.simpleTableHeaderByConsumerGame", "/api/v1/coordinatorTableByConsumer?consumerId=" + c.getId(), messageSource, localeList, c.getCredentials());
		
		table.columns.add(new ColumnDefinition("id", null).setHidden(true));
		table.columns.add(new ColumnDefinition("game", "volunteer.game"));
		table.columns.add(new ColumnDefinition("dateFrom", "volunteer.dateFrom").setDateTimeFormat().setSort("desc", 0));
		table.columns.add(new ColumnDefinition("dateTo", "volunteer.dateTo").setDateTimeFormat());
		
		return table;
	}

	public VolunteerTable(MessageSource messageSource, DynamicLocaleList localeList, ControlPoint c) {
		super("volunteerTableByCP", "volunteer.tableHeaderByControlPoint", "volunteer.simpleTableHeaderByControlPoint",
				"/api/v1/volunteerTableByCP?controlPointId=" + c.getId(), messageSource, localeList, c.getName());

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("name", "volunteer.name").setSort("asc", 0));
		this.columns.add(new ColumnDefinition("dateFrom", "volunteer.dateFrom").setDateTimeFormat());
		this.columns.add(new ColumnDefinition("dateTo", "volunteer.dateTo").setDateTimeFormat());

		this.buttons.add(new ButtonDefinition("actions.create", null, "btn", "createform:volunteerCreateEditByCPForm", null));
		this.buttons.add(new ButtonDefinition("actions.edit", null, "btn", "form:volunteerCreateEditByCPForm:id", "selectedSingle"));
		this.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn", "ajax:DELETE:/api/v1/volunteerDelete/:id", "selected")); 
		
		VolunteerCreateEditByCPForm form = new VolunteerCreateEditByCPForm(localeList);
		form.setControlPointId(c.getParent() == null ? c.getId() : c.getParent().getId());
		if (c.getGame() != null) {
			form.setDateFrom(c.getGame().getDateFrom());
			form.setDateTo(c.getGame().getDateTo());
		}
		this.relatedForms.add(form);
	}

	public void add(Collection<Volunteer> volenteers) {
		for (Volunteer v : volenteers) {
			data.add(new TableRow(v, messageSource, locale));
		}
	}
}

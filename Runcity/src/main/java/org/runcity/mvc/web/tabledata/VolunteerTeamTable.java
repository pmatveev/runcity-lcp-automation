package org.runcity.mvc.web.tabledata;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.runcity.db.entity.Route;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.Team;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.enumeration.TeamStatus;
import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

public class VolunteerTeamTable extends AbstractTable {

	public class ForVolunteer {
	}

	public class ForCoordinator {
	}

	@JsonView({ ForVolunteer.class, ForCoordinator.class })
	private List<TableRow> data = new LinkedList<TableRow>();

	protected class TableRow {
		@JsonView({ ForVolunteer.class, ForCoordinator.class })
		private Long id;

		@JsonView({ ForVolunteer.class, ForCoordinator.class })
		private String category;

		@JsonView({ ForVolunteer.class, ForCoordinator.class })
		private String number;

		@JsonView({ ForVolunteer.class, ForCoordinator.class })
		private String name;

		@JsonView({ ForVolunteer.class, ForCoordinator.class })
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
		private Date start;

		@JsonView(ForCoordinator.class)
		private String contact;

		@JsonView(ForCoordinator.class)
		private String addData;

		@JsonView({ ForVolunteer.class, ForCoordinator.class })
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
		private String status;

		public TableRow(Team t) {
			this.id = t.getId();
			this.category = StringUtils.xss(t.getRoute().getCategory().getLocalizedName(t.getRoute().getGame().getLocale()));
			this.number = StringUtils.xss(t.getNumber());
			this.name = StringUtils.xss(t.getName());
			this.start = t.getStart();
			this.contact = StringUtils.xss(t.getContact());
			this.addData = StringUtils.xss(t.getAddData());

			TeamStatus status = t.getStatus();
			if (status == TeamStatus.ACTIVE) {
				this.status = messageSource.getMessage("teamStatus.leg", new Object[] { t.getLeg() }, locale);
			} else {
				this.status = TeamStatus.getDisplayName(status, messageSource, locale);
			}
		}
	}

	private VolunteerTeamTable(MessageSource messageSource) {
		super(null, null, null, null, messageSource, null, null);
	}
	
	protected VolunteerTeamTable(String id, String title, String simpleTitle, String ajaxData,
			MessageSource messageSource, DynamicLocaleList localeList, Object... titleArgs) {
		super(id, title, simpleTitle, ajaxData, messageSource, localeList, titleArgs);
	}

	private void initVolunteerTable(boolean category) {
		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		if (category) {
			this.columns.add(new ColumnDefinition("category", "team.category"));
		}
		this.columns.add(new ColumnDefinition("number", "team.number").setSort("asc", 1));
		this.columns.add(new ColumnDefinition("status", "team.status").setSort("desc", 0));

		this.extensions.add(new ColumnDefinition("name", "team.name"));
		this.extensions.add(new ColumnDefinition("start", "team.start").setDateTimeFormat());
		this.expandFrame = ":id";
	}

	private void initCoordinatorTable() {
		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("number", "team.number").setSort("asc", 1));
		this.columns.add(new ColumnDefinition("status", "team.status").setSort("desc", 0));

		this.extensions.add(new ColumnDefinition("name", "team.name"));
		this.extensions.add(new ColumnDefinition("start", "team.start").setDateTimeFormat());
		this.extensions.add(new ColumnDefinition("contact", "team.contact"));
		this.extensions.add(new ColumnDefinition("addData", "team.addData"));
		this.expandFrame = ":id";
	}

	public static VolunteerTeamTable initForVolunteer(Volunteer volunteer, MessageSource messageSource,
			DynamicLocaleList localeList) {
		VolunteerTeamTable table = new VolunteerTeamTable("volunteerTeamTable", "jsp.volunteer.teamsCP.header",
				"jsp.volunteer.teams.simpleHeader",
				"/api/v1/controlPoint/" + volunteer.getControlPoint().getId() + "/teamTable", messageSource, localeList,
				volunteer.getControlPoint().getNameDisplayWithChildren());

		table.initVolunteerTable(true);
		return table;
	}

	public static VolunteerTeamTable initForVolunteer(Volunteer volunteer, RouteItem routeItem,
			MessageSource messageSource, DynamicLocaleList localeList) {
		VolunteerTeamTable table = new VolunteerTeamTable("volunteerTeamTable", "jsp.volunteer.teamsCPCat.header",
				"jsp.volunteer.teams.simpleHeader", "/api/v1/routeItem/" + routeItem.getId() + "/teamTable",
				messageSource, localeList, volunteer.getControlPoint().getNameDisplayWithChildren(),
				routeItem.getRoute().getCategory().getLocalizedName(volunteer.getVolunteerGame().getLocale()));

		table.initVolunteerTable(false);
		return table;
	}

	public static VolunteerTeamTable initForCoordinator(Volunteer coordinator, Route route, MessageSource messageSource,
			DynamicLocaleList localeList) {
		VolunteerTeamTable table = new VolunteerTeamTable("volunteerTeamTable", "jsp.volunteer.teamsGameRoute.header",
				"jsp.volunteer.teams.simpleHeader", "/api/v1/coordinator/route/" + route.getId() + "/teamTable",
				messageSource, localeList, coordinator.getVolunteerGame().getName(),
				route.getCategory().getLocalizedName(coordinator.getVolunteerGame().getLocale()));

		table.initCoordinatorTable();
		return table;
	}

	public static VolunteerTeamTable initForCoordinator(Volunteer coordinator, Route route, String status,
			MessageSource messageSource, DynamicLocaleList localeList) {
		VolunteerTeamTable table = new VolunteerTeamTable("volunteerTeamTable",
				"jsp.volunteer.teamsGameRouteStatus.header", "jsp.volunteer.teams.simpleHeader",
				"/api/v1/coordinator/route/" + route.getId() + "/teamTable?status=" + status, messageSource, localeList,
				coordinator.getVolunteerGame().getName(),
				route.getCategory().getLocalizedName(coordinator.getVolunteerGame().getLocale()),
				TeamStatus.getDisplayName(status, messageSource, LocaleContextHolder.getLocale()));

		table.initCoordinatorTable();
		return table;
	}
	
	public static VolunteerTeamTable initRestResponse(MessageSource messageSource) {
		return new VolunteerTeamTable(messageSource);
	}

	public void add(Collection<Team> teams) {
		for (Team t : teams) {
			this.data.add(new TableRow(t));
		}
	}

	@Override
	public VolunteerTeamTable validate() {
		return this;
	}
}

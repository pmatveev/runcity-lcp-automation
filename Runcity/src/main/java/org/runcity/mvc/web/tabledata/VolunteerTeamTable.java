package org.runcity.mvc.web.tabledata;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.runcity.db.entity.Route;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.Team;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.enumeration.TeamStatus;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.annotation.JsonView;

public class VolunteerTeamTable extends AbstractTable {

	public class ForCP {
	}

	public class ForCategory {
	}

	@JsonView({ ForCP.class, ForCategory.class })
	private List<TableRow> data = new LinkedList<TableRow>();

	protected class TableRow {
		@JsonView({ ForCP.class, ForCategory.class })
		private Long id;

		@JsonView({ ForCP.class, ForCategory.class })
		private String number;

		@JsonView({ ForCP.class, ForCategory.class })
		private String status;

		@JsonView(ForCP.class)
		private String category;

		public TableRow(Team t) {
			this.id = t.getId();
			this.number = StringUtils.xss(t.getNumber());
			this.status = StringUtils.xss(TeamStatus.getDisplayName(t.getStatusData(), messageSource, locale));
			this.category = StringUtils.xss(t.getRoute().getCategory().getLocalizedName(t.getRoute().getGame().getLocale()));
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

		this.buttons.add(new ButtonDefinition("common.refresh", null, "btn pull-right", "refresh", null));

		this.expandFrame = "/secure/iframe/team/{0}:id";
	}

	private void initCoordinatorTable() {
		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("number", "team.number").setSort("asc", 1));
		this.columns.add(new ColumnDefinition("status", "team.status").setSort("desc", 0));

		this.buttons.add(new ButtonDefinition("common.refresh", null, "btn pull-right", "refresh", null));

		this.expandFrame = "/secure/iframe/team/{0}:id";
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

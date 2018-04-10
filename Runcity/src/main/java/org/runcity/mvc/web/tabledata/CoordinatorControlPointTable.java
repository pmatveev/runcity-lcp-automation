package org.runcity.mvc.web.tabledata;

import java.util.LinkedList;
import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.enumeration.ControlPointMode;
import org.runcity.db.service.ControlPointService;
import org.runcity.db.service.TeamService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.VolunteerCreateEditByGameCPForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonView;

public class CoordinatorControlPointTable extends AbstractTable {
	@JsonView(Views.Public.class)
	private List<TableRow> data = new LinkedList<TableRow>();

	protected class TableRow {
		@JsonView(Views.Public.class)
		private Long id;

		@JsonView(Views.Public.class)
		private String idt;

		@JsonView(Views.Public.class)
		private String name;

		@JsonView(Views.Public.class)
		private String address; 

		@JsonView(Views.Public.class)
		private String mode;

		@JsonView(Views.Public.class)
		private String modeDisplay;

		@JsonView(Views.Public.class)
		private String volunteers;

		@JsonView(Views.Public.class)
		private Long teams;

		public TableRow(ControlPoint c, Long volunteers, Long active, Long teams) {
			this.id = c.getId();
			this.idt = StringUtils.xss(c.getIdtWithChildren());
			this.name = StringUtils.xss(c.getName());
			this.address = StringUtils.xss(c.getLocalizedAddress(locale.toString()));
			this.mode = ControlPointMode.getStoredValue(c.getMode());
			this.modeDisplay = ControlPointMode.getDisplayBadge(c.getMode(), messageSource, locale);
			this.volunteers = StringUtils.xss(active + " / " + volunteers);
			
			if (active == 0 && volunteers > 0) {
				this.volunteers = "<span class='label label-danger'>" + this.volunteers + "</span>";
			} else if (active == volunteers && active > 0) {
				this.volunteers = "<span class='label label-success'>" + this.volunteers + "</span>";
			} else {
				this.volunteers = "<span class='label label-warning'>" + this.volunteers + "</span>";
			}
			
			this.teams = teams;
		}
	}
	
	public CoordinatorControlPointTable(MessageSource messageSource, DynamicLocaleList localeList, Game g) {
		super("coordControlPointTable", "controlPoint.tableHeader", "controlPoint.simpleTableHeader", 
				"/api/v1/coordControlPointsTable?gameId=" + g.getId(), messageSource, localeList, StringUtils.xss(g.getName()));

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("idt", "controlPoint.idt").setSort("asc", 0));
		this.columns.add(new ColumnDefinition("name", "controlPoint.name"));
		this.columns.add(new ColumnDefinition("address", "controlPoint.address"));
		this.columns.add(new ColumnDefinition("mode", null).setHidden(true));
		this.columns.add(new ColumnDefinition("modeDisplay", "controlPoint.mode"));
		this.columns.add(new ColumnDefinition("volunteers", "controlPoint.volunteers"));
		this.columns.add(new ColumnDefinition("teams", "controlPoint.teamsLeft"));

		this.buttons.add(new ButtonDefinition("coordinator.createVolunteer", null, "btn", "createform:volunteerCreateEditByGameCPForm", null));
		this.buttons.add(new ButtonDefinition("coordinator.cpOnline", null, "btn", "ajax:POST:/api/v1/cpOnline/:id", null).setJsCondition("row.mode == 'N'"));
		this.buttons.add(new ButtonDefinition("coordinator.cpOffline", null, "btn", "ajax:POST:/api/v1/cpOffline/:id", null).setJsCondition("row.mode == 'Y'"));
		this.buttons.add(new ButtonDefinition("common.refresh", null, "btn pull-right", "refresh", null));
		
		VolunteerCreateEditByGameCPForm form = new VolunteerCreateEditByGameCPForm(localeList);
		form.setGameId(g.getId());
		form.setDateFrom(g.getDateFrom());
		form.setDateTo(g.getDateTo());
		this.relatedForms.add(form);

		this.expandFrame = "/secure/iframe/coordination/controlPoint/{0}:id";
	}
	
	public void fetchByGame(ControlPointService service, TeamService teamService, Game game) {
		List<ControlPoint> controlPoints = service.selectLiveByGame(game, ControlPoint.SelectMode.WITH_CHILDREN_AND_ITEMS);
		for (ControlPoint c : controlPoints) {
			Long teams = 0L;
			
			for (RouteItem ri : c.getRouteItems()) {
				Long stat = teamService.selectActiveNumberByRouteItem(ri);
				teams += stat;
			}

			for (ControlPoint ch : c.getChildren()) {
				for (RouteItem ri : ch.getRouteItems()) {
					Long stat = teamService.selectActiveNumberByRouteItem(ri);
					teams += stat;
				}
			}
			
			data.add(new TableRow(c, service.countVolunteers(c), service.countActiveVolunteers(c), teams));
		}
	}	
	
	public List<TableRow> getData() {
		return data;
	}
	
	@Override
	public CoordinatorControlPointTable validate() {
		return this;
	}
}

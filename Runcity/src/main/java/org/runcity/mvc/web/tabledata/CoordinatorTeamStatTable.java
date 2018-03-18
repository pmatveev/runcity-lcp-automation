package org.runcity.mvc.web.tabledata;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.enumeration.TeamStatus;
import org.runcity.db.service.TeamService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.TeamDisqualifyByCoordinatorForm;
import org.runcity.mvc.web.formdata.TeamFinishByCoordinatorForm;
import org.runcity.mvc.web.formdata.TeamNotStartedByCoordinatorForm;
import org.runcity.mvc.web.formdata.TeamRetireByCoordinatorForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonView;

public class CoordinatorTeamStatTable extends AbstractTable {
	@JsonView(Views.Public.class)
	private List<TableRow> data = new LinkedList<TableRow>();
	
	private Long maxStage;

	protected class TableRow {
		@JsonView(Views.Public.class)
		private Long id;

		@JsonView(Views.Public.class)
		private String badge;

		@JsonView(Views.Public.class)
		private String category;

		@JsonView(Views.Public.class)
		private Map<String, String> stat;

		@JsonView(Views.Public.class)
		private String total;
		
		public TableRow(Route r, Map<String, Long> stat) {
			this.id = r.getId();
			this.badge = r.getCategory().getBadge();
			this.category = StringUtils.xss(r.getCategory().getLocalizedName(r.getGame().getLocale()));
			this.stat = new HashMap<String, String>();
			
			for (long i = 1; i <= maxStage; i++) {
				this.stat.put(i + "", "");
			}

			long total = 0;
			for (String key : stat.keySet()) {
				this.stat.put(key, stat.get(key) + "");
				total += stat.get(key);
			}
			this.total = total + "";
		}
	}
	
	private void addTeamStatusColumn(TeamStatus status) {
		this.columns.add(new ColumnDefinition("stat." + TeamStatus.getStoredValue(status), TeamStatus.getDisplayName(status)));		
	}
	
	public CoordinatorTeamStatTable(MessageSource messageSource, DynamicLocaleList localeList, Long maxStage, Game game) {
		super("coordTeamStatTable", "teamStatistics.tableHeader", "teamStatistics.simpleTableHeader", 
				"/api/v1/coordTeamStatTable?gameId=" + game.getId(), messageSource, localeList, game.getName());
		
		this.maxStage = maxStage == null ? 1 : maxStage;
	}
	
	public CoordinatorTeamStatTable(MessageSource messageSource, DynamicLocaleList localeList, Long maxStage, Game game, Volunteer coordinator) {
		this(messageSource, localeList, maxStage, game);

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("category", "teamStatistics.category").setSort("asc", 0));
		this.columns.add(new ColumnDefinition("badge", "teamStatistics.badge"));
		this.columns.add(new ColumnDefinition("total", "teamStatistics.total"));

		addTeamStatusColumn(TeamStatus.NOT_STARTED);
		for (long i = 1; i <= this.maxStage; i++) {
			this.columns.add(new ColumnDefinition("stat." + i, "teamStatistics.legGroup", "teamStatistics.leg", i));
		}
		addTeamStatusColumn(TeamStatus.FINISHED);
		addTeamStatusColumn(TeamStatus.RETIRED);
		addTeamStatusColumn(TeamStatus.DISQUALIFIED);
		
		this.buttons.add(new ButtonDefinition("coordinator.teamNotStart", null, "btn", "createform:teamNotStartedByCoordinatorForm", null));
		this.buttons.add(new ButtonDefinition("coordinator.teamFinish", null, "btn", "createform:teamFinishByCoordinatorForm", null));
		this.buttons.add(new ButtonDefinition("coordinator.teamRetire", null, "btn", "createform:teamRetireByCoordinatorForm", null));
		this.buttons.add(new ButtonDefinition("coordinator.teamDisqualify", null, "btn", "createform:teamDisqualifyByCoordinatorForm", null));
		this.buttons.add(new ButtonDefinition("common.refresh", null, "btn pull-right", "refresh", null));
		
		TeamNotStartedByCoordinatorForm notStartForm = new TeamNotStartedByCoordinatorForm(localeList);
		notStartForm.setVolunteerId(coordinator.getId());
		
		TeamFinishByCoordinatorForm finishForm = new TeamFinishByCoordinatorForm(localeList);
		finishForm.setVolunteerId(coordinator.getId());
		
		TeamRetireByCoordinatorForm retiredForm = new TeamRetireByCoordinatorForm(localeList);
		retiredForm.setVolunteerId(coordinator.getId());
		
		TeamDisqualifyByCoordinatorForm disqualifiedForm = new TeamDisqualifyByCoordinatorForm(localeList);
		disqualifiedForm.setVolunteerId(coordinator.getId());
		
		this.relatedForms.add(notStartForm);
		this.relatedForms.add(finishForm);
		this.relatedForms.add(retiredForm);
		this.relatedForms.add(disqualifiedForm);
	}

	public void fetchByGame(TeamService service, Game game) {
		Map<Route, Map<String, Long>> stat = service.selectStatsByGame(game);
		
		for (Route r : stat.keySet()) {
			data.add(new TableRow(r, stat.get(r)));
		}
	}
	
	@Override
	public AbstractTable validate() {
		return this;
	}
}

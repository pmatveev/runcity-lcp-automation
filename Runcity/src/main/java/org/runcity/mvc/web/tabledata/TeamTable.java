package org.runcity.mvc.web.tabledata;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.runcity.db.entity.Route;
import org.runcity.db.entity.Team;
import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.TeamCreateEditByRouteForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

public class TeamTable extends AbstractTable {

	@JsonView(Views.Public.class)
	private List<TableRow> data = new LinkedList<TableRow>();

	protected class TableRow {
		@JsonView(Views.Public.class)
		private Long id;

		@JsonView(Views.Public.class)
		private String number;

		@JsonView(Views.Public.class)
		private String name;

		@JsonView(Views.Public.class)
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
		private Date start;

		@JsonView(Views.Public.class)
		private String contact;

		@JsonView(Views.Public.class)
		private String addData;

		public TableRow(Team t) {
			this.id = t.getId();
			this.number = StringUtils.xss(t.getNumber());
			this.name = StringUtils.xss(t.getName());
			this.start = t.getStart();
			this.contact = StringUtils.xss(t.getContact());
			this.addData = StringUtils.xss(t.getAddData());
		}
	}

	public TeamTable(MessageSource messageSource, DynamicLocaleList localeList, Route r) {
		super("teamTable", "team.tableHeaderByRoute", "team.simpleTableHeaderByRoute",
				"/api/v1/teamTableByRoute?routeId=" + r.getId(), messageSource, localeList, StringUtils.xss(r.getControlPointName()));
		
		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("number", "team.number").setSort("asc", 0));
		this.columns.add(new ColumnDefinition("name", "team.name"));
		this.columns.add(new ColumnDefinition("start", "team.start").setDateTimeFormat());
		this.columns.add(new ColumnDefinition("contact", "team.contact"));
		
		this.extensions.add(new ColumnDefinition("addData", "team.addData"));
		this.expandFrame = ":id";
		
		this.buttons.add(new ButtonDefinition("actions.create", null, "btn", "createform:teamCreateEditByRouteForm", null));
		this.buttons.add(new ButtonDefinition("actions.edit", null, "btn", "form:teamCreateEditByRouteForm:id", "selectedSingle"));
		this.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn", "ajax:DELETE:/api/v1/teamDelete/:id", "selected")); 
		
		TeamCreateEditByRouteForm form = new TeamCreateEditByRouteForm(localeList);
		form.prepare(r);
		this.relatedForms.add(form);
	}
	
	public void add(Collection<Team> teams) {
		for (Team t : teams) {
			this.data.add(new TableRow(t));
		}
	}

	@Override
	public TeamTable validate() {
		return this;
	}
}

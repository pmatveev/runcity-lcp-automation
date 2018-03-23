package org.runcity.mvc.web.tabledata;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Event;
import org.runcity.db.entity.enumeration.EventStatus;
import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

public class TeamEventTable extends AbstractTable {
	public class ForCP {
	}

	public class ForTeam {
	}

	@JsonView({ ForCP.class, ForTeam.class })
	private List<TableRow> data = new LinkedList<TableRow>();

	protected class TableRow {
		@JsonView({ ForCP.class, ForTeam.class })
		private Long id;

		@JsonView({ ForCP.class, ForTeam.class })
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIMESTAMP_FORMAT)
		private Date time;

		@JsonView(ForCP.class)
		private String teamNumber;

		@JsonView({ ForCP.class, ForTeam.class })
		private String status;

		public TableRow(Event e) {
			this.id = e.getId();
			this.time = e.getDateFrom();
			this.teamNumber = StringUtils.xss(e.getTeam().getNumber());
			this.status = StringUtils.xss(EventStatus.getDisplayName(e.getStatus(), messageSource, locale));
		}
	}

	private TeamEventTable(MessageSource messageSource) {
		super(null, null, null, null, messageSource, null, null);
	}
	
	public static TeamEventTable initRestResponse(MessageSource messageSource) {
		return new TeamEventTable(messageSource);
	}

	public TeamEventTable(ControlPoint controlPoint, MessageSource messageSource, DynamicLocaleList localeList) {
		super("teamEventTable", "teamEvent.headerByCP", "teamEvent.simpleHeader",
				"/api/v1/controlPoint/" + controlPoint.getId() + "/history", messageSource, localeList,
				controlPoint.getName());

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("time", "event.time").setTimeStampFormat().setSort("desc", 0));
		this.columns.add(new ColumnDefinition("teamNumber", "event.teamNumber"));
		this.columns.add(new ColumnDefinition("status", "event.status"));

		this.buttons.add(new ButtonDefinition("common.refresh", null, "btn pull-right", "refresh", null));
		
		this.expandFrame = "/secure/iframe/teamEvent/{0}:id";
	}
	
	public void add(Collection<Event> events) {
		for (Event e : events) {
			this.data.add(new TableRow(e));
		}
	}

	@Override
	public AbstractTable validate() {
		return this;
	}
}

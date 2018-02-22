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

	private static String getId(Class<?> type) {
		if (ByConsumerControlPoint.class.equals(type)) {
			return "volunteerTableByConsumer";
		}
		if (ByConsumerGame.class.equals(type)) {
			return "coordinatorTableByConsumer";
		}
		if (ByGameControlPoint.class.equals(type)) {
			return "volunteerTableByGame";
		}
		if (ByGame.class.equals(type)) {
			return "coordinatorTableByGame";
		}
		return "volunteerTable";
	}

	private static String getHeader(Class<?> type) {
		if (ByGame.class.equals(type)) {
			return "volunteer.tableHeaderByGame";
		}
		if (ByGameControlPoint.class.equals(type)) {
			return "volunteer.tableHeaderByGameControlPoint";
		}
		if (ByControlPoint.class.equals(type)) {
			return "volunteer.tableHeaderByControlPoint";
		}
		if (ByConsumerGame.class.equals(type)) {
			return "volunteer.tableHeaderByConsumerGame";
		}
		if (ByConsumerControlPoint.class.equals(type)) {
			return "volunteer.tableHeaderByConsumerControlPoint";
		}
		throw new RuntimeException("Unsupported type for VolunteerTable");
	}

	private static String getSimpleHeader(Class<?> type) {
		if (ByGame.class.equals(type)) {
			return "volunteer.simpleTableHeaderByGame";
		}
		if (ByGameControlPoint.class.equals(type)) {
			return "volunteer.simpleTableHeaderByGameControlPoint";
		}
		if (ByControlPoint.class.equals(type)) {
			return "volunteer.simpleTableHeaderByControlPoint";
		}
		if (ByConsumerGame.class.equals(type)) {
			return "volunteer.simpleTableHeaderByConsumerGame";
		}
		if (ByConsumerControlPoint.class.equals(type)) {
			return "volunteer.simpleTableHeaderByConsumerControlPoint";
		}
		throw new RuntimeException("Unsupported type for VolunteerTable");
	}

	public void initFields(Class<?> type) {
		this.columns.add(new ColumnDefinition("id", null).setHidden(true));

		if (ByGameControlPoint.class.equals(type)) {
			this.columns.add(new ColumnDefinition("controlPoint", "volunteer.controlPoint"));
		}
		
		if (ByConsumerGame.class.equals(type) || ByConsumerControlPoint.class.equals(type)) {
			this.columns.add(new ColumnDefinition("game", "volunteer.game"));
			
			if (ByConsumerControlPoint.class.equals(type)) {
				this.columns.add(new ColumnDefinition("controlPoint", "volunteer.controlPoint"));
			}
			this.columns.add(new ColumnDefinition("dateFrom", "volunteer.dateFrom").setDateTimeFormat().setSort("desc", 0));
		} else {
			this.columns.add(new ColumnDefinition("name", "volunteer.name").setSort("asc", 0));
			this.columns.add(new ColumnDefinition("dateFrom", "volunteer.dateFrom").setDateTimeFormat());
		}

		this.columns.add(new ColumnDefinition("dateTo", "volunteer.dateTo").setDateTimeFormat());
	}

	public VolunteerTable(MessageSource messageSource, DynamicLocaleList localeList, Game g, Class<?> type) {
		super(getId(type), getHeader(type), getSimpleHeader(type), null, messageSource, localeList, g.getName());

		if (ByGame.class.equals(type)) {
			this.ajaxData = "/api/v1/coordinatorTableByGame?gameId=" + g.getId();
		} else if (ByGameControlPoint.class.equals(type)) {
			this.ajaxData = "/api/v1/volunteerTableByGame?gameId=" + g.getId();
		} else {
			throw new RuntimeException("Unsupported type for VolunteerTable");
		}

		initFields(type);
	}

	public VolunteerTable(MessageSource messageSource, DynamicLocaleList localeList, ControlPoint c) {
		super(getId(ByControlPoint.class), getHeader(ByControlPoint.class), getSimpleHeader(ByControlPoint.class),
				"/api/v1/volunteerTableByCP?controlPointId=" + c.getId(), messageSource, localeList, c.getName());
		initFields(ByControlPoint.class);
	}

	public VolunteerTable(MessageSource messageSource, DynamicLocaleList localeList, Consumer c, Class<?> type) {
		super(getId(type), getHeader(type), getSimpleHeader(type), null, messageSource, localeList,
				c.getCredentials());
		
		if (ByConsumerControlPoint.class.equals(type)) {
			this.ajaxData = "/api/v1/volunteerTableByConsumer?consumerId=" + c.getId();
		} else if (ByConsumerGame.class.equals(type)) {
			this.ajaxData = "/api/v1/coordinatorTableByConsumer?consumerId=" + c.getId();			
		} else {
			throw new RuntimeException("Unsupported type for VolunteerTable");			
		}
		
		initFields(type);
	}

	public void add(Collection<Volunteer> volenteers) {
		for (Volunteer v : volenteers) {
			data.add(new TableRow(v, messageSource, locale));
		}
	}
}

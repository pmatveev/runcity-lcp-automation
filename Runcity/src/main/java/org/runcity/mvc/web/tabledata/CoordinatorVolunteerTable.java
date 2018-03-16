package org.runcity.mvc.web.tabledata;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Volunteer;
import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.web.formdata.VolunteerCreateEditByCPForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

public class CoordinatorVolunteerTable extends AbstractTable {
	public static class ByControlPoint {
	}

	@JsonView({ ByControlPoint.class })
	private List<TableRow> data = new LinkedList<TableRow>();

	protected class TableRow {
		@JsonView({ ByControlPoint.class })
		private Long id;

		@JsonView({ ByControlPoint.class })
		private String name;

		@JsonView({ ByControlPoint.class })
		private String status;

		@JsonView({ ByControlPoint.class })
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
		private Date dateFrom;

		@JsonView({ ByControlPoint.class })
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SpringRootConfig.DATE_TIME_FORMAT)
		private Date dateTo;

		public TableRow(Volunteer v) {
			this.id = v.getId();
			this.name = StringUtils.xss(v.getConsumer().getCredentials());
			if (v.getActive()) {
				this.status = "<span class='label label-success'>" + messageSource.getMessage("volunteer.active", null, locale) + "</span>";
			} else {
				this.status = "<span class='label label-danger'>" + messageSource.getMessage("volunteer.inactive", null, locale) + "</span>";
			}
			this.dateFrom = v.getDateFrom();
			this.dateTo = v.getDateTo();
		}
	}

	public CoordinatorVolunteerTable(MessageSource messageSource, DynamicLocaleList localeList, ControlPoint c) {
		super("coordVolunteerTableByCP", "volunteer.tableHeaderByControlPoint", "volunteer.simpleTableHeaderByControlPoint",
				"/api/v1/coordVolunteerTableByCP?controlPointId=" + c.getId(), messageSource, localeList, c.getName());

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("name", "volunteer.name").setSort("asc", 0));
		this.columns.add(new ColumnDefinition("dateFrom", "volunteer.dateFrom").setDateTimeFormat());
		this.columns.add(new ColumnDefinition("dateTo", "volunteer.dateTo").setDateTimeFormat());
		this.columns.add(new ColumnDefinition("status", "volunteer.status"));

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
			data.add(new TableRow(v));
		}
	}
	
	@Override
	public CoordinatorVolunteerTable validate() {
		return this;
	}
}

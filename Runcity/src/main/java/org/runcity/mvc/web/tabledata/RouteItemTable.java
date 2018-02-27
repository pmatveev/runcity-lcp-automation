package org.runcity.mvc.web.tabledata;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.runcity.db.entity.Route;
import org.runcity.db.entity.RouteItem;
import org.runcity.mvc.rest.util.RestResponseClass;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.RouteItemCreateEditForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonView;

public class RouteItemTable extends AbstractTable {
	@JsonView(Views.Public.class)
	private List<TableRow> data = new LinkedList<TableRow>();
	
	private int start = 0;
	private int finish = 0;
	private int maxStage = -1;
	private Map<Integer, Integer> legEnd = new HashMap<Integer, Integer>();
	
	protected class TableRow {
		private final RouteItemTable table = RouteItemTable.this;
		
		@JsonView(Views.Public.class)
		private Long id;
		
		@JsonView(Views.Public.class)
		private Integer sort;
		
		@JsonView(Views.Public.class)
		private Integer legNum;

		@JsonView(Views.Public.class)
		private String type;
		
		@JsonView(Views.Public.class)
		private String idt;
		
		@JsonView(Views.Public.class)
		private String name;
		
		@JsonView(Views.Public.class)
		private String address;
		
		public TableRow(RouteItem ri) {
			this.id = ri.getId();
			this.legNum = ri.getLegNumber();
			if (ri.getControlPoint() != null) {
				switch (ri.getControlPoint().getType()) {
				case START:
					table.start++;
					this.sort = 0;
					this.legNum = null;
					break;
				case BONUS:
					this.sort = ri.getLegNumber() == null ? 0 : ri.getLegNumber() * 10;
					if (ri.getLegNumber() != null) {
						table.maxStage = Math.max(table.maxStage, ri.getLegNumber());
					}
					break;
				case REGULAR:
					this.sort = ri.getLegNumber() == null ? 0 : ri.getLegNumber() * 10 + 1;
					if (ri.getLegNumber() == null || ri.getLegNumber() < 1) {
						table.setResponseClass(RestResponseClass.WARNING);
						table.addCommonMsg("routeItem.validation.invalidLegNumber", new Object[] { ri.getControlPoint().getNameDisplay(messageSource, locale) });
					} else {
						table.maxStage = Math.max(table.maxStage, ri.getLegNumber());
					}
					break;
				case STAGE_END:
					this.sort = ri.getLegNumber() == null ? 0 : ri.getLegNumber() * 10 + 9;
					if (ri.getLegNumber() == null || ri.getLegNumber() < 1) {
						table.setResponseClass(RestResponseClass.WARNING);
						table.addCommonMsg("routeItem.validation.invalidLegNumber", new Object[] { ri.getControlPoint().getNameDisplay(messageSource, locale) });
					} else {
						table.maxStage = Math.max(table.maxStage, ri.getLegNumber());
						table.legEnd.put(ri.getLegNumber(), table.legEnd.getOrDefault(ri.getLegNumber(), 0) + 1);
					}
					break;
				case FINISH:
					table.finish++;
					this.sort = 9999;
					this.legNum = null;
					break;
				}
			}
			this.idt = StringUtils.xss(ri.getControlPoint().getIdt());
			this.type = StringUtils.xss(ri.getControlPoint().getType().getDisplayName(messageSource, locale));
			this.name = StringUtils.xss(ri.getControlPoint().getName());
			this.address = StringUtils.xss(ri.getControlPoint().getLocalizedAddress(locale.toString()));
		}

		public Long getId() {
			return id;
		}

		public Integer getSort() {
			return sort;
		}

		public Integer getLegNum() {
			return legNum;
		}

		public String getIdt() {
			return idt;
		}

		public String getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		public String getAddress() {
			return address;
		}
	}
	
	public RouteItemTable(MessageSource messageSource, DynamicLocaleList localeList, Route route) {
		super("routeItemTable", "routeItem.tableHeader", "routeItem.tableHeader", "/api/v1/routeItemTable?routeId=" + route.getId(), messageSource, localeList);

		this.columns.add(new ColumnDefinition("id", null).setHidden(true));
		this.columns.add(new ColumnDefinition("sort", null).setHidden(true).setSort("asc", 0));
		this.columns.add(new ColumnDefinition("legNum", "routeItem.leg"));
		this.columns.add(new ColumnDefinition("idt", "controlPoint.idt").setSort("asc", 1));
		this.columns.add(new ColumnDefinition("type", "controlPoint.type"));
		this.columns.add(new ColumnDefinition("name", "controlPoint.name"));
		this.columns.add(new ColumnDefinition("address", "controlPoint.address"));
		
		this.buttons.add(new ButtonDefinition("actions.create", null, "btn", "createform:routeItemCreateEditForm", null));
		this.buttons.add(new ButtonDefinition("actions.edit", null, "btn", "form:routeItemCreateEditForm:id", "selectedSingle"));
		this.buttons.add(new ButtonDefinition("actions.delete", "confirmation.delete", "btn", "ajax:DELETE:/api/v1/routeItemDelete/:id", "selected")); 
		
		RouteItemCreateEditForm form = new RouteItemCreateEditForm(localeList);
		form.setRouteId(route.getId());
		this.relatedForms.add(form);
	}
	
	public void fill(Route gc) {
		for (RouteItem ri : gc.getRouteItems()) {
			data.add(new TableRow(ri));
		}
	}
	
	public List<TableRow> getData() {
		return data;
	}
	
	@Override
	public RouteItemTable validate() {
		if (start < 1) {
			setResponseClass(RestResponseClass.WARNING);
			addCommonMsg("routeItem.validation.missingStart");
		} else if (start > 1) {
			setResponseClass(RestResponseClass.WARNING);
			addCommonMsg("routeItem.validation.multiStart");
		}
		
		if (finish < 1) {
			setResponseClass(RestResponseClass.WARNING);
			addCommonMsg("routeItem.validation.missingFinish");
		} else if (finish > 1) {
			setResponseClass(RestResponseClass.WARNING);
			addCommonMsg("routeItem.validation.multiFinish");
		}
		
		for (int i = 1; i <= maxStage; i++) {
			int num = legEnd.getOrDefault(i, 0) + (i == maxStage ? finish : 0);
			if (num < 1) {
				setResponseClass(RestResponseClass.WARNING);
				addCommonMsg("routeItem.validation.missingLegEnd", new Object[] { i });
			} else if (num > 1) {
				setResponseClass(RestResponseClass.WARNING);
				addCommonMsg("routeItem.validation.multiLegEnd", new Object[] { i });
			}
		}
		
		return this;
	}
}

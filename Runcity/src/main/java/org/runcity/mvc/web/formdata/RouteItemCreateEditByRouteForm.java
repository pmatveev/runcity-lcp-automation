package org.runcity.mvc.web.formdata;

import org.apache.log4j.Logger;
import org.runcity.db.entity.RouteItem;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormDddwControlPointColumn;
import org.runcity.mvc.web.util.FormIdColumn;
import org.runcity.mvc.web.util.FormIdRouteColumn;
import org.runcity.mvc.web.util.FormNumberColumn;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonView;

public class RouteItemCreateEditByRouteForm extends AbstractForm {
	private static final Logger logger = Logger.getLogger(RouteItemCreateEditByRouteForm.class);

	@JsonView(Views.Public.class)
	private FormIdColumn id;

	@JsonView(Views.Public.class)
	private FormIdRouteColumn routeId;

	@JsonView(Views.Public.class)
	private FormNumberColumn leg;

	@JsonView(Views.Public.class)
	private FormDddwControlPointColumn controlPoint;
	
	public RouteItemCreateEditByRouteForm() {
		this(null);
	}
	
	public RouteItemCreateEditByRouteForm(DynamicLocaleList localeList) {
		super("routeItemCreateEditByRouteForm", "/api/v1/routeItemCreateEdit/{0}", null, "/api/v1/routeItemCreateEdit", localeList);
		logger.trace("Creating form " + getFormName());
		setTitle("routeItem.header");
		
		this.id = new FormIdColumn(this, new ColumnDefinition("id", "id"));
		this.routeId = new FormIdRouteColumn(this, new ColumnDefinition("routeId", "routeid"));
		this.leg = new FormNumberColumn(this, new ColumnDefinition("leg", "routeItem.leg"));
		this.leg.setMin(0);
		this.leg.setMax(99);
		this.controlPoint = FormDddwControlPointColumn.getByRouteUnused(this, new ColumnDefinition("controlPoint", "routeItem.controlPoint"), id, routeId);
		this.controlPoint.setForceRefresh(true);
		this.controlPoint.setRequired(true);
	}
	
	public RouteItemCreateEditByRouteForm(Long id, Long routeId, Integer leg, Long controlPointId, DynamicLocaleList localeList) {
		this(localeList);
		setId(id);
		setRouteId(routeId);
		setLeg(leg);
		setControlPoint(controlPointId);
	}
	
	public RouteItemCreateEditByRouteForm(RouteItem ri, DynamicLocaleList localeList) {
		this(ri.getId(), ri.getRoute().getId(), ri.getLegNumber(), ri.getControlPoint().getId(), localeList);
	}

	public Long getId() {
		return id.getValue();
	}

	public void setId(Long id) {
		this.id.setValue(id);
	}

	public Long getRouteId() {
		return routeId.getValue();
	}

	public void setRouteId(Long routeId) {
		this.routeId.setValue(routeId);
	}

	public Integer getLeg() {
		return leg.getValue();
	}

	public void setLeg(Integer leg) {
		this.leg.setValue(leg);
	}

	public Long getControlPoint() {
		return controlPoint.getValue();
	}

	public void setControlPoint(Long controlPoint) {
		this.controlPoint.setValue(controlPoint);
	}
	
	public FormIdColumn getIdColumn() {
		return id;
	}

	public FormIdRouteColumn getRouteIdColumn() {
		return routeId;
	}

	public FormNumberColumn getLegColumn() {
		return leg;
	}
	
	public FormDddwControlPointColumn getControlPointColumn() {
		return controlPoint;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		logger.debug("Validating " + getFormName());
		id.validate(context, errors);
		routeId.validate(context, errors);
		leg.validate(context, errors);
		controlPoint.validate(context, errors);
	}
	
	public RouteItem getRouteItem() {
		return new RouteItem(getId(), getLeg(), routeId.getRoute(), controlPoint.getControlPoint());
	}
}

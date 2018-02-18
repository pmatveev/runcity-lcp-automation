package org.runcity.db.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "route_item",  
	uniqueConstraints = @UniqueConstraint(columnNames = {"route__id", "control_point__id"}))
public class RouteItem {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;
	
	@Column(name = "leg_num", columnDefinition = "int", nullable = true)
	private Integer legNumber;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "route__id", nullable = false)
	private Route route;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "control_point__id", nullable = false)
	private ControlPoint controlPoint;
	
	public RouteItem() {
	}
	
	public RouteItem(Long id, Integer legNumber, Route route, ControlPoint controlPoint) {
		setId(id);
		setLegNumber(legNumber);
		setRoute(route);
		setControlPoint(controlPoint);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getLegNumber() {
		return legNumber;
	}

	public void setLegNumber(Integer legNumber) {
		this.legNumber = legNumber;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public ControlPoint getControlPoint() {
		return controlPoint;
	}

	public void setControlPoint(ControlPoint controlPoint) {
		this.controlPoint = controlPoint;
	}
}

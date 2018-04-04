package org.runcity.db.entity;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "route_item",  
	uniqueConstraints = @UniqueConstraint(columnNames = {"route__id", "control_point__id"}))
public class RouteItem {
	public static final int MAX_SORT = 9999;
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;
	
	@Column(name = "leg_num", columnDefinition = "int", nullable = true)
	private Integer legNumber;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "route__id", nullable = false)
	private Route route;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
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
	
	public int getSafeLegIndex() {
		if (legNumber != null) {
			return legNumber;
		}
		switch (getControlPoint().getType()) {
		case BONUS:
			return MAX_SORT;
		case FINISH:
			return MAX_SORT + 1;
		default:
			return 0;
		}
	}
	
	public int getSortIndex() {
		switch (getControlPoint().getType()) {
		case BONUS:
			return legNumber == null ? 0 : legNumber * 10;
		case FINISH:
			return MAX_SORT * 10;
		case REGULAR:
			return legNumber == null ? 0 : legNumber * 10 + 1;
		case STAGE_END:
			return legNumber == null ? 0 : legNumber * 10 + 9;
		case START:
			return 0;
		}
		return 0;
	}
}

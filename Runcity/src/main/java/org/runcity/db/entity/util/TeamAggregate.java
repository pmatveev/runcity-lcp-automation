package org.runcity.db.entity.util;

import org.runcity.db.entity.Route;

public class TeamAggregate {
	private Route route;
	private String status;
	private Long number;

	public TeamAggregate(Route route, String status, Long number) {
		this.route = route;
		this.status = status;
		this.number = number;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

}

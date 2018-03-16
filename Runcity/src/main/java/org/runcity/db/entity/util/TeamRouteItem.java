package org.runcity.db.entity.util;

import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.Team;

public class TeamRouteItem {
	private Team team;
	private RouteItem routeItem;

	public TeamRouteItem(Team team, RouteItem routeItem) {
		this.team = team;
		this.routeItem = routeItem;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public RouteItem getRouteItem() {
		return routeItem;
	}

	public void setRouteItem(RouteItem routeItem) {
		this.routeItem = routeItem;
	};

}

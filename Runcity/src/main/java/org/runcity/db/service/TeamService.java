package org.runcity.db.service;

import java.util.List;
import java.util.Map;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.Team;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.Team.SelectMode;
import org.runcity.db.entity.enumeration.TeamStatus;
import org.runcity.db.entity.util.TeamRouteItem;
import org.runcity.exception.DBException;
import org.runcity.util.ActionResponseBody;

public interface TeamService {
	public Team selectById(Long id, Team.SelectMode selectMode);

	public Team selectByNumberGame(String number, Game game, Team.SelectMode selectMode);
	
	public TeamRouteItem selectByNumberCP(String number, ControlPoint controlPoint, Team.SelectMode selectMode);
	
	public Team addOrUpdate(Team t) throws DBException;

	public void delete(List<Long> id);
	
	public List<Team> selectTeamsByRoute(Long route, Team.SelectMode selectMode);
	
	public List<Team> selectTeamsByRoute(Route route, Team.SelectMode selectMode);
	
	public void processTeam(Team team, String allowedStatus, RouteItem ri, Volunteer volunteer, ActionResponseBody result) throws DBException;

	public void setTeamStatus(Team team, String allowedStatus, TeamStatus status, Volunteer volunteer, ActionResponseBody result) throws DBException;
	
	public Map<Route, Map<String, Long>> selectStatsByGame(Game game);
	
	public Long selectActiveNumberByRouteItem(RouteItem routeItem);
	
	public List<Team> selectPendingTeamsByCP(Long controlPoint, Team.SelectMode selectMode);
	
	public List<Team> selectPendingTeamsByRouteItem(Long routeItem, Team.SelectMode selectMode);
	
	public List<Team> selectPendingTeamsByRouteItem(RouteItem routeItem, Team.SelectMode selectMode);

	public List<Team> selectTeamsByRouteWithStatus(Route route, String status, SelectMode selectMode);
}
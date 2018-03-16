package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.Team;
import org.runcity.exception.DBException;

public interface TeamService {
	public Team selectById(Long id, Team.SelectMode selectMode);

	public Team selectByNumberGame(String number, Game game, Team.SelectMode selectMode);
	
	public Team selectByNumberCP(String number, ControlPoint controlPoint, Team.SelectMode selectMode);
	
	public Team addOrUpdate(Team t) throws DBException;

	public void delete(List<Long> id);
	
	public List<Team> selectTeams(Long route, Team.SelectMode selectMode);
	
	public List<Team> selectTeams(Route route, Team.SelectMode selectMode);
}

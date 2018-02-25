package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.Volunteer;
import org.runcity.exception.DBException;

public interface ControlPointService {	
	public List<ControlPoint> selectByGame(Long game);
	
	public List<ControlPoint> selectByGame(Game game);
	
	public List<ControlPoint> selectMainByGame(Long game);
	
	public List<ControlPoint> selectMainByGame(Game game);
	
	public List<ControlPoint> selectByRouteNotUsed(Long route);
	
	public List<ControlPoint> selectByRouteNotUsed(Route route);
	
	public List<ControlPoint> selectByParent(Long parent);
	
	public List<ControlPoint> selectByParent(ControlPoint parent);

	public ControlPoint selectById(Long id, boolean image);
	
	public ControlPoint addOrUpdate(ControlPoint c) throws DBException;

	public void delete(List<Long> id);

	public List<Volunteer> selectVolunteers(Long controlPoint);

	public List<Volunteer> selectVolunteers(ControlPoint controlPoint);
}

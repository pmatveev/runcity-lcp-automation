package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.ControlPoint.SelectMode;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.enumeration.ControlPointMode;
import org.runcity.exception.DBException;

public interface ControlPointService {	
	public List<ControlPoint> selectByGame(Long game, ControlPoint.SelectMode selectMode);
	
	public List<ControlPoint> selectByGame(Game game, ControlPoint.SelectMode selectMode);
	
	public List<ControlPoint> selectMainByGame(Long game, ControlPoint.SelectMode selectMode);
	
	public List<ControlPoint> selectMainByGame(Game game, ControlPoint.SelectMode selectMode);
	
	public List<ControlPoint> selectByRouteNotUsed(Long route, ControlPoint.SelectMode selectMode);
	
	public List<ControlPoint> selectByRouteNotUsed(Route route, ControlPoint.SelectMode selectMode);
	
	public List<ControlPoint> selectByParent(Long parent, ControlPoint.SelectMode selectMode);
	
	public List<ControlPoint> selectByParent(ControlPoint parent, ControlPoint.SelectMode selectMode);

	public List<ControlPoint> selectLiveByGame(Long game, ControlPoint.SelectMode selectMode);
	
	public List<ControlPoint> selectLiveByGame(Game game, ControlPoint.SelectMode selectMode);
		
	public ControlPoint selectById(Long id, ControlPoint.SelectMode selectMode);

	public ControlPoint addOrUpdate(ControlPoint c) throws DBException;

	public void delete(List<Long> id);
	
	public Long countVolunteers(Long controlPoint);
	
	public Long countVolunteers(ControlPoint controlPoint);
	
	public Long countActiveVolunteers(Long controlPoint);
	
	public Long countActiveVolunteers(ControlPoint controlPoint);
	
	public void setMode(List<Long> id, ControlPointMode mode);

	public Iterable<ControlPoint> selectById(List<Long> id, SelectMode selectMode);
}

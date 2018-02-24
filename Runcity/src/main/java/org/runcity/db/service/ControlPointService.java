package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.Volunteer;
import org.runcity.exception.DBException;
import org.springframework.security.access.annotation.Secured;

public interface ControlPointService {	
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectByGame(Long game);
	
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectByGame(Game game);
	
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectMainByGame(Long game);
	
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectMainByGame(Game game);
	
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectByRouteNotUsed(Long route);
	
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectByRouteNotUsed(Route route);
	
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectByParent(Long parent);
	
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectByParent(ControlPoint parent);

	@Secured("ROLE_ADMIN")
	public ControlPoint selectById(Long id, boolean image);
	
	@Secured("ROLE_ADMIN")
	public ControlPoint addOrUpdate(ControlPoint c) throws DBException;

	@Secured("ROLE_ADMIN")
	public void delete(List<Long> id);

	@Secured("ROLE_ADMIN")
	public List<Volunteer> selectVolunteers(Long controlPoint);

	@Secured("ROLE_ADMIN")
	public List<Volunteer> selectVolunteers(ControlPoint controlPoint);
}

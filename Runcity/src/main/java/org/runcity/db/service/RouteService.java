package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Route;
import org.runcity.db.entity.RouteItem;
import org.runcity.exception.DBException;

public interface RouteService {
	public Route selectById(Long id, Route.SelectMode selectMode);

	public void delete(List<Long> id);
	
	public RouteItem selectItemById(Long id);

	public RouteItem addOrUpdateItem(RouteItem ri) throws DBException;
	
	public void deleteItem(List<Long> id);
	
	public Long selectTeamNumber(Route route);
	
	public Long selectMaxLeg(Route route);
}

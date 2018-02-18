package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Route;
import org.runcity.db.entity.RouteItem;
import org.runcity.exception.DBException;
import org.springframework.security.access.annotation.Secured;

public interface RouteService {
	public Route selectById(Long id, boolean routeItem);

	@Secured("ROLE_ADMIN")
	public void delete(List<Long> id);
	
	public RouteItem selectItemById(Long id);

	@Secured("ROLE_ADMIN")
	public RouteItem addOrUpdateItem(RouteItem ri) throws DBException;
	
	@Secured("ROLE_ADMIN")
	public void deleteItem(List<Long> id);
}

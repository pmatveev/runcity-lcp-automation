package org.runcity.db.service.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.repository.RouteItemRepository;
import org.runcity.db.repository.RouteRepository;
import org.runcity.db.service.RouteService;
import org.runcity.exception.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = { DBException.class })
public class RouteServiceImpl implements RouteService {
	@Autowired
	private RouteRepository routeRepository;
	
	@Autowired
	private RouteItemRepository routeItemRepository;
	
	private void initialize(Route r, Route.SelectMode selectMode) {
		if (r == null) {
			return;
		}
		switch (selectMode) {
		case WITH_ITEMS:
			Hibernate.initialize(r.getRouteItems());
			break;
		case NONE:
			break;
		}
	}
	
/*	private void initialize(Collection<Route> routes, Route.SelectMode selectMode) {
		if (routes == null || selectMode == Route.SelectMode.NONE) {
			return;
		}
		for (Route r : routes) {
			initialize(r, selectMode);
		}
	}
*/	
	@Override
	public Route selectById(Long id, Route.SelectMode selectMode) {
		Route r = routeRepository.findOne(id);
		initialize(r, selectMode);
		return r;
	}

	private void delete(Long id) {
		routeRepository.delete(id);
	}
	
	@Override
	public void delete(List<Long> id) {
		for (Long i : id) {
			delete(i);
		}
	}
	
	@Override
	public RouteItem selectItemById(Long id) {
		return routeItemRepository.findOne(id);
	}

	@Override
	public RouteItem addOrUpdateItem(RouteItem ri) throws DBException {
		try {
			return routeItemRepository.save(ri);
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}
	
	private void deleteItem(Long id) {
		routeItemRepository.delete(id);
	}
	
	@Override
	public void deleteItem(List<Long> id) {
		for (Long i : id) {
			deleteItem(i);
		}
	}

	@Override
	public Long selectTeamNumber(Route r) {
		return routeRepository.selectTeamCount(r);
	}

	@Override
	public Long selectMaxLeg(Route route) {
		return routeRepository.selectMaxLeg(route);
	}
}

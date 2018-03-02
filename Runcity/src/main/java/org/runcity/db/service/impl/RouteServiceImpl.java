package org.runcity.db.service.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.Team;
import org.runcity.db.repository.RouteItemRepository;
import org.runcity.db.repository.RouteRepository;
import org.runcity.db.repository.TeamRepository;
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
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Override
	public Route selectById(Long id, boolean routeItem) {
		Route r = routeRepository.findOne(id);
		
		if (routeItem) {
			Hibernate.initialize(r.getRouteItems());
		}
		
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
		return routeRepository.selectTeamNumber(r);
	}

	@Override
	public List<Team> selectTeams(Long route) {
		return selectTeams(routeRepository.findOne(route));
	}

	@Override
	public List<Team> selectTeams(Route route) {
		return teamRepository.findByRoute(route);
	}
}

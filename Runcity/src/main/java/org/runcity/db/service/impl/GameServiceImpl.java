package org.runcity.db.service.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.repository.RouteRepository;
import org.runcity.db.repository.GameRepository;
import org.runcity.db.service.GameService;
import org.runcity.exception.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = { DBException.class })
public class GameServiceImpl implements GameService {
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private RouteRepository routeRepository;
	
	@Override
	public Game selectById(Long id, boolean categories) {
		Game g = gameRepository.findOne(id);
		if (categories) {
			Hibernate.initialize(g.getCategories());
		}
		return g;
	}

	@Override
	public List<Game> selectAll(boolean categories) {
		List<Game> games = gameRepository.findAll();
		if (categories) {
			for (Game g : games) {
				Hibernate.initialize(g.getCategories());
			}
		}
		return games;
	}

	@Override
	@Secured("ROLE_ADMIN")
	public Game addOrUpdate(Game g) throws DBException {
		try {
			if (g.getId() != null) {
				Game prev = selectById(g.getId(), true);
				prev.update(g);
				return gameRepository.save(prev);
			} else {
				return gameRepository.save(g);
			}
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}
	
	private void delete(Long id) {
		gameRepository.delete(id);
	}

	@Override
	@Secured("ROLE_ADMIN")
	public void delete(List<Long> id) {
		for (Long i : id) {
			delete(i);
		}
	}

	private void deleteCategory(Long id) {
		routeRepository.delete(id);
	}
	
	@Override
	@Secured("ROLE_ADMIN")
	public void deleteCategories(List<Long> id) {
		for (Long i : id) {
			deleteCategory(i);
		}
	}
	
	public Route selectRouteById(Long id, boolean routeItem) {
		Route gc = routeRepository.findOne(id);
		
		if (routeItem) {
			Hibernate.initialize(gc.getRouteItems());
		}
		
		return gc;
	}
}

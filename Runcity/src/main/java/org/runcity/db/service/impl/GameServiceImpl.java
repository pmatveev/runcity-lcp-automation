package org.runcity.db.service.impl;

import java.util.Collection;
import java.util.List;

import org.hibernate.Hibernate;
import org.runcity.db.entity.Game;
import org.runcity.db.repository.GameRepository;
import org.runcity.db.service.GameService;
import org.runcity.exception.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = { DBException.class })
public class GameServiceImpl implements GameService {
	@Autowired
	private GameRepository gameRepository;

	private void initialize(Game g, Game.SelectMode selectMode) {
		if (g == null) {
			return;
		}
		switch (selectMode) {
		case WITH_CATEGORIES:
			Hibernate.initialize(g.getCategories());
			break;
		case NONE:
			break;
		}		
	}
	
	private void initialize(Collection<Game> games, Game.SelectMode selectMode) {
		if (games == null || selectMode == Game.SelectMode.NONE) {
			return;
		}
		for (Game g : games) {
			initialize(g, selectMode);
		}
	}
	
	@Override
	public Game selectById(Long id, Game.SelectMode selectMode) {
		Game g = gameRepository.findOne(id);
		initialize(g, selectMode);		
		return g;
	}

	@Override
	public List<Game> selectAll(Game.SelectMode selectMode) {
		List<Game> games = gameRepository.findAll();
		initialize(games, selectMode);
		return games;
	}

	@Override
	public Game addOrUpdate(Game g) throws DBException {
		try {
			if (g.getId() != null) {
				Game prev = selectById(g.getId(), Game.SelectMode.WITH_CATEGORIES);
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
	public void delete(List<Long> id) {
		for (Long i : id) {
			delete(i);
		}
	}
}

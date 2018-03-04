package org.runcity.db.service.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.repository.GameRepository;
import org.runcity.db.repository.VolunteerRepository;
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
	
	@Autowired
	private VolunteerRepository volunteerRepository;
	
	private void initialize(Game g, Game.SelectMode selectMode) {
		switch (selectMode) {
		case WITH_CATEGORIES:
			Hibernate.initialize(g.getCategories());
			break;
		default:
			break;
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
		for (Game g : games) {
			initialize(g, selectMode);
		}
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

	@Override
	public List<Volunteer> selectCoordinators(Long game) {
		return selectCoordinators(gameRepository.findOne(game));
	}

	@Override
	public List<Volunteer> selectCoordinators(Game game) {
		return volunteerRepository.findByGame(game);
	}

	@Override
	public List<Volunteer> selectVolunteers(Long game) {
		return selectVolunteers(gameRepository.findOne(game));
	}

	@Override
	public List<Volunteer> selectVolunteers(Game game) {
		return volunteerRepository.findCPByGame(game);
	}
}

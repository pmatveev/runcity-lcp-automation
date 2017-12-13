package org.runcity.db.service.impl;

import java.util.List;

import org.runcity.db.entity.Game;
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
	
	@Override
	public Game selectById(Long id) {
		return gameRepository.findOne(id);
	}

	@Override
	public List<Game> selectAll() {
		return gameRepository.findAll();
	}

	@Override
	@Secured("ROLE_ADMIN")
	public Game addOrUpdate(Game g) throws DBException {
		try {
			if (g.getId() != null) {
				Game prev = selectById(g.getId());
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
}

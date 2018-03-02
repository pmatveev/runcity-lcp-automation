package org.runcity.db.service.impl;

import java.util.List;

import org.runcity.db.entity.Game;
import org.runcity.db.entity.Team;
import org.runcity.db.repository.TeamRepository;
import org.runcity.db.service.TeamService;
import org.runcity.exception.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = { DBException.class })
public class TeamServiceImpl implements TeamService {
	@Autowired
	private TeamRepository teamRepository;

	@Override
	public Team selectById(Long id) {
		return teamRepository.findOne(id);
	}

	@Override
	public Team selectByNumberGame(String number, Game g) {
		return teamRepository.selectByGameAndNumber(number, g);
	}
	
	@Override
	public Team addOrUpdate(Team team) throws DBException {
		try {
			return teamRepository.save(team);
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	private void delete(Long id) {
		teamRepository.delete(id);
	}
	
	@Override
	public void delete(List<Long> id) {
		for (Long i : id) {
			delete(i);
		}
	}
}

package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Game;
import org.runcity.db.entity.Team;
import org.runcity.exception.DBException;

public interface TeamService {
	public Team selectById(Long id);
	
	public Team selectByNumberGame(String number, Game g);
	
	public Team addOrUpdate(Team t) throws DBException;

	public void delete(List<Long> id);
}

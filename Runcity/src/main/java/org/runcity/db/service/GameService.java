package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Game;
import org.runcity.db.entity.Volunteer;
import org.runcity.exception.DBException;

public interface GameService {
	public Game selectById(Long id, Game.SelectMode selectMode);

	public List<Game> selectAll(Game.SelectMode selectMode);
	
	public Game addOrUpdate(Game g) throws DBException;
	
	public void delete(List<Long> id);

	public List<Volunteer> selectCoordinators(Long game);

	public List<Volunteer> selectCoordinators(Game game);

	public List<Volunteer> selectVolunteers(Long game);

	public List<Volunteer> selectVolunteers(Game game);
}

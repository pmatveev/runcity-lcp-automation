package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Game;
import org.runcity.db.entity.Volunteer;
import org.runcity.exception.DBException;
import org.springframework.security.access.annotation.Secured;

public interface GameService {
	public Game selectById(Long id, boolean categories);

	public List<Game> selectAll(boolean categories);
	
	@Secured("ROLE_ADMIN")
	public Game addOrUpdate(Game g) throws DBException;
	
	@Secured("ROLE_ADMIN")
	public void delete(List<Long> id);

	@Secured("ROLE_ADMIN")
	public List<Volunteer> selectCoordinators(Long game);

	@Secured("ROLE_ADMIN")
	public List<Volunteer> selectCoordinators(Game game);

	@Secured("ROLE_ADMIN")
	public List<Volunteer> selectVolunteers(Long game);

	@Secured("ROLE_ADMIN")
	public List<Volunteer> selectVolunteers(Game game);
}

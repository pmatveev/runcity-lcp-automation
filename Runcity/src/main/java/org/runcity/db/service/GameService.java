package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Game;
import org.runcity.exception.DBException;
import org.springframework.security.access.annotation.Secured;

public interface GameService {
	public Game selectById(Long id);

	public List<Game> selectAll();
	
	@Secured("ROLE_ADMIN")
	public Game addOrUpdate(Game g) throws DBException;
	
	@Secured("ROLE_ADMIN")
	public void delete(List<Long> id);

}

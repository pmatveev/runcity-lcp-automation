package org.runcity.db.repository;

import java.util.List;

import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TeamRepository extends JpaRepository<Team, Long> {
	public List<Team> findByRoute(Route r);
	
	@Query("select t from Team t where t.number = :number and t.route.game = :game")
	public Team selectByGameAndNumber(@Param("number") String number, @Param("game") Game g);
}
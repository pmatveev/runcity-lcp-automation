package org.runcity.db.repository;

import org.runcity.db.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GameRepository extends JpaRepository<Game, Long> {
	@Query("select MAX(ri.legNumber) + 1L from RouteItem ri, ControlPoint cp where cp = ri.controlPoint and cp.type = 'E' and cp.game = :game")
	public Long selectMaxLeg(@Param("game") Game g);
}

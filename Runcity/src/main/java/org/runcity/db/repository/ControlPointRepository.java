package org.runcity.db.repository;


import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ControlPointRepository extends JpaRepository<ControlPoint, Long> {
	public List<ControlPoint> findByGame(Game game);
}

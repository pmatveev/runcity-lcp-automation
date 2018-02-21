package org.runcity.db.repository;

import java.util.List;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
	public List<Volunteer> findByControlPoint(ControlPoint controlPoint);
	
	public List<Volunteer> findByConsumer(Consumer consumer);
	
	public List<Volunteer> findByGame(Game game);
}

package org.runcity.db.repository;

import java.util.Date;
import java.util.List;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
	public List<Volunteer> findByControlPoint(ControlPoint controlPoint);

	@Query("select v from Volunteer v where v.consumer = :consumer and v.controlPoint is not null")
	public List<Volunteer> findCPByConsumer(@Param("consumer") Consumer consumer);

	@Query("select v from Volunteer v where v.consumer = :consumer and v.game is not null")
	public List<Volunteer> findGameByConsumer(@Param("consumer") Consumer consumer);
	
	public List<Volunteer> findByGame(Game game);
	
	@Query("select v from Volunteer v, ControlPoint cp where v.controlPoint = cp and cp.game = :game")
	public List<Volunteer> findCPByGame(@Param("game") Game game);

	@Query("select v from Volunteer v, Consumer c, ControlPoint cp, Game g "
			+ "where c = v.consumer "
			+ "and c.username = :user "
			+ "and cp = v.controlPoint "
			+ "and g = cp.game "
			+ "and g.dateTo > :date")
	public List<Volunteer> findUpcomingByUsernameCP(@Param("user") String username, @Param("date") Date dateTo);
	
	@Query("select v from Volunteer v, Consumer c, Game g "
			+ "where c = v.consumer "
			+ "and c.username = :user "
			+ "and g = v.game "
			+ "and g.dateTo > :date")
	public List<Volunteer> findUpcomingByUsernameGame(@Param("user") String username, @Param("date") Date dateTo);
	
	@Query("select v from Volunteer v, Consumer c where v.controlPoint = :cp and c = v.consumer and c.username = :user")
	public Volunteer findByCPandUsername(@Param("cp") ControlPoint cp, @Param("user") String username);
	
	@Query("select v from Volunteer v, Consumer c, Event e "
			+ "where c = v.consumer "
			+ "and c.username = :user "
			+ "and e.volunteer = v "
			+ "and e.status = 'P' and e.type = 'V'")
	public Volunteer findCurrentByUsername(@Param("user") String username);
}

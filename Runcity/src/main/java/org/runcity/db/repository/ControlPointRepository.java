package org.runcity.db.repository;


import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ControlPointRepository extends JpaRepository<ControlPoint, Long> {
	public List<ControlPoint> findByGame(Game game);
	
	public List<ControlPoint> findByParent(ControlPoint parent);
	
	@Query("select c from ControlPoint c where c.game = :game and c.parent = null")
	public List<ControlPoint> findMainByGame(@Param("game") Game game);
	
	@Query("select c from ControlPoint c where c.game = :game and not exists (select ri from RouteItem ri where ri.route = :route and ri.controlPoint = c)")
	public List<ControlPoint> findByRouteNotUsed(@Param("game") Game game, @Param("route") Route route);
	
	@Query("select c from ControlPoint c where c.game = :game and exists (select v from Volunteer v where v.controlPoint = c)")
	public List<ControlPoint> findLiveByGame(@Param("game") Game game);
	
	@Query("select count(v) from Volunteer v where v.controlPoint = :cp")
	public Long countVolunteers(@Param("cp") ControlPoint controlPoint);
	
	@Query("select count(distinct v) from Volunteer v, Event e where v.controlPoint = :cp and e.volunteer = v and e.status = 'P' and e.type = 'V'")
	public Long countActiveVolunteers(@Param("cp") ControlPoint controlPoint);
}

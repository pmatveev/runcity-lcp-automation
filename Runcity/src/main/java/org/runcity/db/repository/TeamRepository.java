package org.runcity.db.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.LockModeType;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.Team;
import org.runcity.db.entity.util.TeamAggregate;
import org.runcity.db.entity.util.TeamRouteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TeamRepository extends JpaRepository<Team, Long> {
	public List<Team> findByRoute(Route r);
	
	@Query("select t from Team t where t.number = :number and t.route.game = :game")
	public Team selectByGameAndNumber(@Param("number") String number, @Param("game") Game game);
	
	@Query("select new org.runcity.db.entity.util.TeamRouteItem(t, ri) from Team t, RouteItem ri where t.number = :number and t.route = ri.route and ri.controlPoint = :cp")
	public TeamRouteItem selectByCPAndNumber(@Param("number") String number, @Param("cp") ControlPoint controlPoint);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select t from Team t where t = :team")
	public Team selectForUpdate(@Param("team") Team team);
	
	@Query("select new org.runcity.db.entity.util.TeamAggregate(r, t.status, count(t)) from Route r, Team t where r.game = :game and t.route = r group by r, t.status")
	public List<TeamAggregate> selectStatsByGame(@Param("game") Game game);
	
	@Query(value = "select count(t.id) from team t where t.route__id = :route and t.status > 0 and t.status <= :leg", nativeQuery = true)
	public BigInteger selectActiveNumberByRouteItem(@Param("route") Route route, @Param("leg") Integer leg);
	
	@Query(value = "select count(t.id) from team t where t.route__id = :route and t.status > 0", nativeQuery = true)
	public BigInteger selectActiveNumberByRoute(@Param("route") Route route);
}

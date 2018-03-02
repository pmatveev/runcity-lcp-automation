package org.runcity.db.repository;

import org.runcity.db.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RouteRepository extends JpaRepository<Route, Long> {
	@Query("select COUNT(t.id) from Team t where t.route = :route")
	public Long selectTeamNumber(@Param("route") Route r);
}

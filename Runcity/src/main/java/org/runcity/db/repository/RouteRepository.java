package org.runcity.db.repository;

import org.runcity.db.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RouteRepository extends JpaRepository<Route, Long> {
}

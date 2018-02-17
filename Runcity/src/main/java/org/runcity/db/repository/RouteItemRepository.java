package org.runcity.db.repository;

import org.runcity.db.entity.RouteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RouteItemRepository extends JpaRepository<RouteItem, Long> {
}

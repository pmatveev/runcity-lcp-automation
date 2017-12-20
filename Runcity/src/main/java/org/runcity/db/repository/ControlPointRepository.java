package org.runcity.db.repository;


import org.runcity.db.entity.ControlPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ControlPointRepository extends JpaRepository<ControlPoint, Long> {
}

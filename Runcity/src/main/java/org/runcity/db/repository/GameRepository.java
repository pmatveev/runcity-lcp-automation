package org.runcity.db.repository;

import org.runcity.db.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GameRepository extends JpaRepository<Game, Long> {
}

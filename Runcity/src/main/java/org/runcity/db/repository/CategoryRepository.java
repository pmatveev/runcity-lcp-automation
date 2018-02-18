package org.runcity.db.repository;


import java.util.List;

import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {
	@Query("select c from Category c where not exists (select r.category from Route r where r.game = :game and r.category = c)")
	public List<Category> selectUnused(@Param("game") Game g);
}

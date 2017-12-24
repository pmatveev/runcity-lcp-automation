package org.runcity.db.repository;

import org.runcity.db.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {
}

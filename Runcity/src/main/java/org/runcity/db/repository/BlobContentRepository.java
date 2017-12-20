package org.runcity.db.repository;


import org.runcity.db.entity.BlobContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BlobContentRepository extends JpaRepository<BlobContent, Long> {
}

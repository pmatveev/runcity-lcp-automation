package org.runcity.db.repository;

import org.runcity.db.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
	public Consumer findByUsername(String username);

	public Consumer findByEmail(String email);
}

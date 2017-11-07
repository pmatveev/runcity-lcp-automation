package org.runcity.db.repository;

import org.runcity.db.entity.PersistentLogins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface PersistedLoginsRepository extends JpaRepository<PersistentLogins, Long> {
    @Modifying
    @Transactional
    @Query("update PersistentLogins set username = ?1 where username = ?2")
    public void updateUsername(String oldUsername, String newUsername);
}

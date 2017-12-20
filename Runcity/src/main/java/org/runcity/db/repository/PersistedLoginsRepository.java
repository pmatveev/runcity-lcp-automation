package org.runcity.db.repository;

import org.runcity.db.entity.PersistentLogins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PersistedLoginsRepository extends JpaRepository<PersistentLogins, Long> {
    @Modifying
    @Transactional
    @Query("update PersistentLogins set username = ?2 where username = ?1")
    public void updateUsername(String oldUsername, String newUsername);

    @Modifying
    @Transactional
    @Query("delete from PersistentLogins where username = ?1")
    public void deleteConsumer(String username);
}

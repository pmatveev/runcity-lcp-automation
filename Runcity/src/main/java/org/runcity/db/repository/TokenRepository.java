package org.runcity.db.repository;

import java.util.Date;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TokenRepository extends JpaRepository<Token, Long> {
	@Modifying
	@Query("update Token set dateTo = :currDate where consumer = :consumer and dateTo > :currDate")
	public void invalidateToken(@Param("consumer") Consumer consumer, @Param("currDate") Date currDate);

	@Query("select t from Token t where consumer = :consumer and dateTo > :currDate")
	public Token selectToken(@Param("consumer") Consumer consumer, @Param("currDate") Date currDate);
}

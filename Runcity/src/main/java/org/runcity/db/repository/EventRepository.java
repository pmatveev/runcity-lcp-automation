package org.runcity.db.repository;

import java.util.Date;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.Event;
import org.runcity.db.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EventRepository extends JpaRepository<Event, Long> {
	@Query("select count(e) from Event e where e.volunteer = :volunteer and e.type = 'V' and e.status = 'P'")
	public long isActiveVolunteer(@Param("volunteer") Volunteer volunteer);
	
    @Modifying
    @Transactional
    @Query("update Event e set e.status = 'C', e.dateTo = :date "
    		+ "where e.volunteer in (select v from Volunteer v where v.consumer = :user) "
    		+ "and e.status = 'P' and e.type = 'V'")
    public void closeActive(@Param("user") Consumer consumer, @Param("date") Date date);
}

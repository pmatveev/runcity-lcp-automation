package org.runcity.db.repository;

import java.util.Date;
import java.util.List;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Event;
import org.runcity.db.entity.Team;
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
    @Query("update Event e set e.status = 'C', e.closedBy = :volunteer, e.dateTo = :date "
    		+ "where e.volunteer in (select v from Volunteer v where v.consumer = :user) "
    		+ "and e.status = 'P' and e.type = 'V'")
    public void closeActive(@Param("volunteer") Volunteer closedBy, @Param("user") Consumer consumer, @Param("date") Date date);

	@Query("select e from Event e, Volunteer v where e.team is not null and e.volunteer = v and v.controlPoint = :cp")
    public List<Event> selectTeamEvents(@Param("cp") ControlPoint controlPoint);

	@Query("select e from Event e where e.team = :team")
    public List<Event> selectTeamEvents(@Param("team") Team team);

	@Query("select max(e.id) from Event e where e.status = 'P' and e.team = :team")
    public Long selectLastActiveTeamEvent(@Param("team") Team team);
}

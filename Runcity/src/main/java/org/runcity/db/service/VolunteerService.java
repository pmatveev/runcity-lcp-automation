package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Volunteer;
import org.runcity.exception.DBException;
import org.springframework.security.access.annotation.Secured;

public interface VolunteerService {
	public Volunteer selectById(Long id);

	@Secured("ROLE_ADMIN")
	public Volunteer addOrUpdate(Volunteer v) throws DBException;
	
	@Secured("ROLE_ADMIN")
	public void delete(List<Long> id);
}

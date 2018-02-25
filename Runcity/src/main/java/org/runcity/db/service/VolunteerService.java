package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Volunteer;
import org.runcity.exception.DBException;

public interface VolunteerService {
	public Volunteer selectById(Long id);

	public Volunteer addOrUpdate(Volunteer v) throws DBException;
	
	public void delete(List<Long> id);
}

package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Volunteer;
import org.runcity.exception.DBException;

public interface VolunteerService {
	public Volunteer selectById(Long id, Volunteer.SelectMode selectMode);

	public Volunteer addOrUpdate(Volunteer v) throws DBException;
	
	public void delete(List<Long> id);
	
	public List<Volunteer> getUpcomingVolunteers(String username);
	
	public List<Volunteer> getUpcomingCoordinations(String username);
	
	public Volunteer selectByControlPointAndUsername(ControlPoint cp, String username);
}

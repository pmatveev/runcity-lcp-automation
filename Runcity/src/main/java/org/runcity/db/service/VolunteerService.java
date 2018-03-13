package org.runcity.db.service;

import java.util.List;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Volunteer;
import org.runcity.exception.DBException;

public interface VolunteerService {
	public Volunteer selectById(Long id, Volunteer.SelectMode selectMode);

	public Volunteer addOrUpdate(Volunteer v) throws DBException;
	
	public void delete(List<Long> id);
	
	public List<Volunteer> getUpcomingVolunteers(String username, Volunteer.SelectMode selectMode);
	
	public List<Volunteer> getUpcomingCoordinations(String username, Volunteer.SelectMode selectMode);
	
	public Volunteer selectByControlPointAndUsername(ControlPoint cp, String username, Volunteer.SelectMode selectMode);
	
	public Volunteer selectCoordinatorByUsername(Game g, String username, Volunteer.SelectMode selectMode);
	
	public boolean isCoordinator(Game g, String username);
	
	public void setCurrent(Volunteer v, boolean isActive) throws DBException;
	
	public Volunteer getCurrentByUsername(String username);
	
	public List<Volunteer> selectByControlPoint(Long controlPoint, Volunteer.SelectMode selectMode);

	public List<Volunteer> selectByControlPoint(ControlPoint controlPoint, Volunteer.SelectMode selectMode);

	public List<Volunteer> selectCoordinatorsByGame(Long game, Volunteer.SelectMode selectMode);

	public List<Volunteer> selectCoordinatorsByGame(Game game, Volunteer.SelectMode selectMode);

	public List<Volunteer> selectVolunteersByGame(Long game, Volunteer.SelectMode selectMode);

	public List<Volunteer> selectVolunteersByGame(Game game, Volunteer.SelectMode selectMode);

	public List<Volunteer> selectVolunteersByConsumer(Long consumer, Volunteer.SelectMode selectMode);

	public List<Volunteer> selectVolunteersByConsumer(Consumer consumer, Volunteer.SelectMode selectMode);

	public List<Volunteer> selectCoordinatorsByConsumer(Long consumer, Volunteer.SelectMode selectMode);

	public List<Volunteer> selectCoordinatorsByConsumer(Consumer consumer, Volunteer.SelectMode selectMode);
}

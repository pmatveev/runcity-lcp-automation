package org.runcity.db.service.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Event;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.Volunteer.SelectMode;
import org.runcity.db.entity.enumeration.EventStatus;
import org.runcity.db.entity.enumeration.EventType;
import org.runcity.db.repository.ConsumerRepository;
import org.runcity.db.repository.ControlPointRepository;
import org.runcity.db.repository.EventRepository;
import org.runcity.db.repository.GameRepository;
import org.runcity.db.repository.VolunteerRepository;
import org.runcity.db.service.VolunteerService;
import org.runcity.exception.DBException;
import org.runcity.secure.SecureUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@Transactional(rollbackFor = { DBException.class })
public class VolunteerServiceImpl implements VolunteerService {
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private ConsumerRepository consumerRepository;
	
	@Autowired
	private ControlPointRepository controlPointRepository;
	
	@Autowired
	private VolunteerRepository volunteerRepository;
	
	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private SessionRegistry sessionRegistry;

	private void initialize(Volunteer v, Volunteer.SelectMode selectMode) {
		if (v == null) {
			return;
		}
		switch (selectMode) {
		case WITH_ACTIVE :
			v.setActive(eventRepository.isActiveVolunteer(v) > 0);
			break;
		case WITH_ACTIVE_AND_CP_CHILDREN:
			v.setActive(eventRepository.isActiveVolunteer(v) > 0);
			Hibernate.initialize(v.getControlPoint().getChildren());
			break;
		case NONE:
			break;
		default:
			break;
		}
	}
	
	private void initialize(Collection<Volunteer> volunteers, Volunteer.SelectMode selectMode) {
		if (volunteers == null || selectMode == Volunteer.SelectMode.NONE) {
			return;
		}
		for (Volunteer v : volunteers) {
			initialize(v, selectMode);
		}
	}

	@Override
	public Volunteer selectById(Long id, Volunteer.SelectMode selectMode) {
		Volunteer result = volunteerRepository.findOne(id);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public Volunteer addOrUpdate(Volunteer v) throws DBException {
		try {
			return volunteerRepository.save(v);
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	private void delete(Long id) {
		volunteerRepository.delete(id);
	}

	@Override
	public void delete(List<Long> id) {
		for (Long i : id) {
			delete(i);
		}
	}

	private Comparator<Volunteer> volunteerSorter = new Comparator<Volunteer>() {
		@Override
		public int compare(Volunteer o1, Volunteer o2) {
			if (o1.getVolunteerGame().getUtcDateTo() == null) {
				return -1;
			}
			return o1.getVolunteerGame().getUtcDateTo().compareTo(o2.getVolunteerGame().getUtcDateTo());
		}
	};

	@Override
	public List<Volunteer> getUpcomingVolunteers(String username, Volunteer.SelectMode selectMode) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -2);
		List<Volunteer> result = volunteerRepository.findUpcomingByUsernameCP(username, c.getTime());
		Collections.sort(result, volunteerSorter);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public List<Volunteer> getUpcomingCoordinations(String username, Volunteer.SelectMode selectMode) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -2);
		List<Volunteer> result = volunteerRepository.findUpcomingByUsernameGame(username, c.getTime());
		Collections.sort(result, volunteerSorter);
		initialize(result, selectMode);
		return result;
	}


	@Override
	public Volunteer selectByControlPointAndUsername(Long controlPointId, String username, boolean requireActive, SelectMode selectMode) {
		return selectByControlPointAndUsername(controlPointRepository.findOne(controlPointId), username, requireActive, selectMode);
	}

	@Override
	public Volunteer selectByControlPointAndUsername(ControlPoint cp, String username, boolean requireActive, Volunteer.SelectMode selectMode) {
		Volunteer result = volunteerRepository.findByCPandUsername(cp, username);
		
		initialize(result, selectMode);
		if (result != null && requireActive) {
			if (result.getActive() == null) {
				result.setActive(eventRepository.isActiveVolunteer(result) > 0);
			}
			if (Boolean.FALSE.equals(result.getActive())) {
				result = null;
			}
		}
		
		return result;
	}

	@Override
	public void setCurrent(Volunteer v, boolean isActive) throws DBException {
		try {
			Date now = v.now();
			eventRepository.closeActive(v, v.getConsumer(), now);
			
			if (isActive) {
				Event e = new Event();
				e.setDateFrom(now);
				e.setStatus(EventStatus.POSTED);
				e.setType(EventType.VOLUNTEER_AT_CP);
				e.setVolunteer(v);
				
				e = eventRepository.save(e);
				
				if (e == null) {
					throw new RuntimeException("Cannot insert event");
				}
			}
			
			String username = v.getConsumer().getUsername();
			Volunteer current = isActive ? v : null;
			for (Object p : sessionRegistry.getAllPrincipals()) {
				if (p instanceof SecureUserDetails) {
					SecureUserDetails d = (SecureUserDetails) p;
					if (ObjectUtils.nullSafeEquals(username, d.getUsername())) {
						d.setCurrent(current);
					}
				}
			}
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	@Override
	public Volunteer getCurrentByUsername(String username) {
		return volunteerRepository.findCurrentByUsername(username);
	}
	
	@Override
	public boolean isVolunteerForGame(String username, Game game) {
		return volunteerRepository.countVolunteers(username, game) > 0;
	}
	
	@Override
	public boolean isCoordinatorForGame(String username, Game game) {
		return volunteerRepository.countCoordinators(username, game) > 0;
	}

	@Override
	public Volunteer selectCoordinatorByUsername(Game g, String username, SelectMode selectMode) {
		Volunteer result = volunteerRepository.findByGameAndUsername(g, username);
		initialize(result, selectMode);
		return result;
	}
	
	@Override
	public List<Volunteer> selectByControlPoint(Long controlPoint, Volunteer.SelectMode selectMode) {
		return selectByControlPoint(controlPointRepository.findOne(controlPoint), selectMode);
	}

	@Override
	public List<Volunteer> selectByControlPoint(ControlPoint controlPoint, Volunteer.SelectMode selectMode) {
		ControlPoint cp = controlPoint.getParent() == null ? controlPoint : controlPoint.getParent();
		List<Volunteer> result = volunteerRepository.findByControlPoint(cp);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public List<Volunteer> selectCoordinatorsByGame(Long game, Volunteer.SelectMode selectMode) {
		return selectCoordinatorsByGame(gameRepository.findOne(game), selectMode);
	}

	@Override
	public List<Volunteer> selectCoordinatorsByGame(Game game, Volunteer.SelectMode selectMode) {
		List<Volunteer> result = volunteerRepository.findByGame(game);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public List<Volunteer> selectVolunteersByGame(Long game, Volunteer.SelectMode selectMode) {
		return selectVolunteersByGame(gameRepository.findOne(game), selectMode);
	}

	@Override
	public List<Volunteer> selectVolunteersByGame(Game game, Volunteer.SelectMode selectMode) {
		List<Volunteer> result =  volunteerRepository.findCPByGame(game);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public List<Volunteer> selectVolunteersByConsumer(Long consumer, Volunteer.SelectMode selectMode) {
		return selectVolunteersByConsumer(consumerRepository.findOne(consumer), selectMode);
	}

	@Override
	public List<Volunteer> selectVolunteersByConsumer(Consumer consumer, Volunteer.SelectMode selectMode) {
		List<Volunteer> result = volunteerRepository.findCPByConsumer(consumer);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public List<Volunteer> selectCoordinatorsByConsumer(Long consumer, Volunteer.SelectMode selectMode) {
		return selectVolunteersByConsumer(consumerRepository.findOne(consumer), selectMode);
	}

	@Override
	public List<Volunteer> selectCoordinatorsByConsumer(Consumer consumer, Volunteer.SelectMode selectMode) {
		List<Volunteer> result = volunteerRepository.findGameByConsumer(consumer);
		initialize(result, selectMode);
		return result;
	}
}

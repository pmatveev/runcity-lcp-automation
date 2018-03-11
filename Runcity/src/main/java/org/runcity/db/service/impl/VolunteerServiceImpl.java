package org.runcity.db.service.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Event;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.enumeration.EventStatus;
import org.runcity.db.entity.enumeration.EventType;
import org.runcity.db.repository.EventRepository;
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
		default:
			break;
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
		for (Volunteer v : result) {
			initialize(v, selectMode);
		}
		return result;
	}

	@Override
	public List<Volunteer> getUpcomingCoordinations(String username, Volunteer.SelectMode selectMode) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -2);
		List<Volunteer> result = volunteerRepository.findUpcomingByUsernameGame(username, c.getTime());
		Collections.sort(result, volunteerSorter);
		for (Volunteer v : result) {
			initialize(v, selectMode);
		}
		return result;
	}

	@Override
	public Volunteer selectByControlPointAndUsername(ControlPoint cp, String username, Volunteer.SelectMode selectMode) {
		Volunteer result = volunteerRepository.findByCPandUsername(cp, username);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public void setCurrent(Volunteer v, boolean isActive) throws DBException {
		try {
			Date now = v.now();
			eventRepository.closeActive(v.getConsumer(), now);
			
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
}

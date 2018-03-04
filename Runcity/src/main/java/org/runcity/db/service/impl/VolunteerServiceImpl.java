package org.runcity.db.service.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.repository.VolunteerRepository;
import org.runcity.db.service.VolunteerService;
import org.runcity.exception.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = { DBException.class })
public class VolunteerServiceImpl implements VolunteerService {
	@Autowired
	private VolunteerRepository volunteerRepository;

	private void initialize(Volunteer v, Volunteer.SelectMode selectMode) {
		switch (selectMode) {
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
	public List<Volunteer> getUpcomingVolunteers(String username) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -2);
		List<Volunteer> result = volunteerRepository.findUpcomingByUsernameCP(username, c.getTime());
		Collections.sort(result, volunteerSorter);
		return result;
	}

	@Override
	public List<Volunteer> getUpcomingCoordinations(String username) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -2);
		List<Volunteer> result = volunteerRepository.findUpcomingByUsernameGame(username, c.getTime());
		Collections.sort(result, volunteerSorter);
		return result;
	}

	@Override
	public Volunteer selectByControlPointAndUsername(ControlPoint cp, String username) {
		return volunteerRepository.findByCPandUsername(cp, username);
	}
}

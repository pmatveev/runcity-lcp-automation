package org.runcity.db.service.impl;

import java.util.List;

import org.runcity.db.entity.Volunteer;
import org.runcity.db.repository.VolunteerRepository;
import org.runcity.db.service.VolunteerService;
import org.runcity.exception.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = { DBException.class })
public class VolunteerServiceImpl implements VolunteerService {
	@Autowired
	private VolunteerRepository volunteerRepository;

	@Override
	public Volunteer selectById(Long id) {
		return volunteerRepository.findOne(id);
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
	@Secured("ROLE_ADMIN")
	public void delete(List<Long> id) {
		for (Long i : id) {
			delete(i);
		}
	}
}

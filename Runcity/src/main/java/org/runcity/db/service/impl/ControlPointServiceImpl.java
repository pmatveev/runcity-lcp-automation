package org.runcity.db.service.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.repository.BlobContentRepository;
import org.runcity.db.repository.ControlPointRepository;
import org.runcity.db.repository.GameRepository;
import org.runcity.db.repository.RouteRepository;
import org.runcity.db.repository.VolunteerRepository;
import org.runcity.db.service.BlobContentService;
import org.runcity.db.service.ControlPointService;
import org.runcity.exception.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = { DBException.class })
public class ControlPointServiceImpl implements ControlPointService {
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private ControlPointRepository controlPointRepository;
	
	@Autowired
	private RouteRepository routeRepository;
	
	@Autowired
	private BlobContentRepository blobContentRepository;
	
	@Autowired 
	private BlobContentService blobContentService;
	
	@Autowired
	private VolunteerRepository volunteerRepository;

	private void initialize(ControlPoint c, ControlPoint.SelectMode selectMode) {
		if (c == null) {
			return;
		}
		switch (selectMode) {
		case WITH_IMAGE:
			if (c.getImage() != null) {
				c.setImageData(blobContentRepository.findOne(c.getImage()).getContent());
			}
			break;
		case FOR_VOLUNTEER:
			Hibernate.initialize(c.getChildren());
			Hibernate.initialize(c.getRouteItems());
			for (ControlPoint ch : c.getChildren()) {
				Hibernate.initialize(ch.getRouteItems());
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public List<ControlPoint> selectByGame(Game game) {
		return controlPointRepository.findByGame(game);
	}

	@Override
	public List<ControlPoint> selectByGame(Long game) {
		return selectByGame(gameRepository.findOne(game));
	}

	@Override
	public List<ControlPoint> selectMainByGame(Game game) {
		return controlPointRepository.findMainByGame(game);
	}

	@Override
	public List<ControlPoint> selectMainByGame(Long game) {
		return selectMainByGame(gameRepository.findOne(game));
	}

	@Override
	public List<ControlPoint> selectByRouteNotUsed(Route route) {
		return controlPointRepository.findByRouteNotUsed(route.getGame(), route);
	}

	@Override
	public List<ControlPoint> selectByRouteNotUsed(Long route) {
		return selectByRouteNotUsed(routeRepository.findOne(route));
	}

	@Override
	public List<ControlPoint> selectByParent(Long parent) {
		return selectByParent(controlPointRepository.findOne(parent));
	}

	@Override
	public List<ControlPoint> selectByParent(ControlPoint parent) {
		return controlPointRepository.findByParent(parent);
	}

	@Override
	public ControlPoint selectById(Long id, ControlPoint.SelectMode selectMode) {
		ControlPoint c = controlPointRepository.findOne(id);
		initialize(c, selectMode);
		
		return c;
	}

	@Override
	public ControlPoint addOrUpdate(ControlPoint c) throws DBException {
		try {
			if (c.getId() != null) {
				ControlPoint prev = selectById(c.getId(), ControlPoint.SelectMode.NONE);
				
				prev.update(c);
				prev.setImage(blobContentService.handleBlobContent(prev.getImage(), c.getImageData()));
				
				return controlPointRepository.save(prev);
			} else {
				ControlPoint n = c.cloneForAdd();
				n.setImage(blobContentService.handleBlobContent(null, c.getImageData()));
				n = controlPointRepository.save(n);
				n.update(c);
				return controlPointRepository.save(n);
			}
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	private void delete(Long id) {
		controlPointRepository.delete(id);
	}

	@Override
	public void delete(List<Long> id) {
		for (Long i : id) {
			delete(i);
		}
	}

	@Override
	public List<Volunteer> selectVolunteers(Long controlPoint) {
		return selectVolunteers(controlPointRepository.findOne(controlPoint));
	}

	@Override
	public List<Volunteer> selectVolunteers(ControlPoint controlPoint) {
		ControlPoint cp = controlPoint.getParent() == null ? controlPoint : controlPoint.getParent();
		return volunteerRepository.findByControlPoint(cp);
	}
}

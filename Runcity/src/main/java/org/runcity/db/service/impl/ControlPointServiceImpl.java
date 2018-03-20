package org.runcity.db.service.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.enumeration.ControlPointMode;
import org.runcity.db.repository.BlobContentRepository;
import org.runcity.db.repository.ControlPointRepository;
import org.runcity.db.repository.GameRepository;
import org.runcity.db.repository.RouteRepository;
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
		case WITH_CHILDREN_AND_ITEMS:
			Hibernate.initialize(c.getChildren());
			Hibernate.initialize(c.getRouteItems());
			for (ControlPoint ch : c.getChildren()) {
				Hibernate.initialize(ch.getRouteItems());
			}
			break;
		case WITH_CHILDREN:
			Hibernate.initialize(c.getChildren());
		case NONE:
			break;
		}
	}
	
	private void initialize(Iterable<ControlPoint> controlPoints, ControlPoint.SelectMode selectMode) {
		if (controlPoints == null || selectMode == ControlPoint.SelectMode.NONE) {
			return;
		}
		for (ControlPoint cp : controlPoints) {
			initialize(cp, selectMode);
		}
	}
	
	@Override
	public List<ControlPoint> selectByGame(Game game, ControlPoint.SelectMode selectMode) {
		List<ControlPoint> result = controlPointRepository.findByGame(game);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public List<ControlPoint> selectByGame(Long game, ControlPoint.SelectMode selectMode) {
		return selectByGame(gameRepository.findOne(game), selectMode);
	}

	@Override
	public List<ControlPoint> selectMainByGame(Game game, ControlPoint.SelectMode selectMode) {
		List<ControlPoint> result = controlPointRepository.findMainByGame(game);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public List<ControlPoint> selectMainByGame(Long game, ControlPoint.SelectMode selectMode) {
		return selectMainByGame(gameRepository.findOne(game), selectMode);
	}

	@Override
	public List<ControlPoint> selectByRouteNotUsed(Route route, ControlPoint.SelectMode selectMode) {
		List<ControlPoint> result = controlPointRepository.findByRouteNotUsed(route.getGame(), route);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public List<ControlPoint> selectByRouteNotUsed(Long route, ControlPoint.SelectMode selectMode) {
		return selectByRouteNotUsed(routeRepository.findOne(route), selectMode);
	}

	@Override
	public List<ControlPoint> selectByParent(Long parent, ControlPoint.SelectMode selectMode) {
		return selectByParent(controlPointRepository.findOne(parent), selectMode);
	}

	@Override
	public List<ControlPoint> selectByParent(ControlPoint parent, ControlPoint.SelectMode selectMode) {
		List<ControlPoint> result = controlPointRepository.findByParent(parent);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public List<ControlPoint> selectLiveByGame(Long game, ControlPoint.SelectMode selectMode) {
		return selectLiveByGame(gameRepository.findOne(game), selectMode);
	}

	@Override
	public List<ControlPoint> selectLiveByGame(Game game, ControlPoint.SelectMode selectMode) {
		List<ControlPoint> result = controlPointRepository.findLiveByGame(game);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public ControlPoint selectById(Long id, ControlPoint.SelectMode selectMode) {
		ControlPoint c = controlPointRepository.findOne(id);
		initialize(c, selectMode);
		return c;
	}

	@Override
	public Iterable<ControlPoint> selectById(List<Long> id, ControlPoint.SelectMode selectMode) {
		Iterable<ControlPoint> c = controlPointRepository.findAll(id);
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
	public Long countVolunteers(Long controlPoint) {
		return countVolunteers(controlPointRepository.findOne(controlPoint));
	}

	@Override
	public Long countVolunteers(ControlPoint controlPoint) {
		return controlPointRepository.countVolunteers(controlPoint);
	}

	@Override
	public Long countActiveVolunteers(Long controlPoint) {
		return countActiveVolunteers(controlPointRepository.findOne(controlPoint));
	}

	@Override
	public Long countActiveVolunteers(ControlPoint controlPoint) {
		return controlPointRepository.countActiveVolunteers(controlPoint);
	}

	@Override
	public void setMode(List<Long> id, ControlPointMode mode) {
		controlPointRepository.updateMode(id, ControlPointMode.getStoredValue(mode));
	}
}

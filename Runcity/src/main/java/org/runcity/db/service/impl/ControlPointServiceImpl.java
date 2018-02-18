package org.runcity.db.service.impl;

import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.repository.BlobContentRepository;
import org.runcity.db.repository.ControlPointRepository;
import org.runcity.db.repository.GameRepository;
import org.runcity.db.repository.RouteRepository;
import org.runcity.db.service.BlobContentService;
import org.runcity.db.service.ControlPointService;
import org.runcity.exception.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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

	@Override
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectByGame(Game game) {
		return controlPointRepository.findByGame(game);
	}

	@Override
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectByGame(Long game) {
		return selectByGame(gameRepository.findOne(game));
	}

	@Override
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectMainByGame(Game game) {
		return controlPointRepository.findMainByGame(game);
	}

	@Override
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectMainByGame(Long game) {
		return selectMainByGame(gameRepository.findOne(game));
	}

	@Override
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectByRouteNotUsed(Route route) {
		return controlPointRepository.findByRouteNotUsed(route.getGame(), route);
	}

	@Override
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectByRouteNotUsed(Long route) {
		return selectByRouteNotUsed(routeRepository.findOne(route));
	}

	@Override
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectByParent(Long parent) {
		return selectByParent(controlPointRepository.findOne(parent));
	}

	@Override
	@Secured("ROLE_ADMIN")
	public List<ControlPoint> selectByParent(ControlPoint parent) {
		return controlPointRepository.findByParent(parent);
	}

	@Override
	@Secured("ROLE_ADMIN")
	public ControlPoint selectById(Long id, boolean image) {
		ControlPoint c = controlPointRepository.findOne(id);
		
		if (image && c.getImage() != null) {
			c.setImageData(blobContentRepository.findOne(c.getImage()).getContent());
		}
		
		return c;
	}
	
	@Override
	@Secured("ROLE_ADMIN")
	public ControlPoint addOrUpdate(ControlPoint c) throws DBException {
		try {
			if (c.getId() != null) {
				ControlPoint prev = selectById(c.getId(), false);
				
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
//		ControlPoint c = selectById(id, false);
		controlPointRepository.delete(id);
/*		if (c.getImage() != null) {
			blobContentRepository.delete(c.getImage());
		}*/
	}

	@Override
	@Secured("ROLE_ADMIN")
	public void delete(List<Long> id) {
		for (Long i : id) {
			delete(i);
		}
	}
}

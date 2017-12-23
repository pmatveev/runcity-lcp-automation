package org.runcity.db.service.impl;

import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.repository.BlobContentRepository;
import org.runcity.db.repository.ControlPointRepository;
import org.runcity.db.repository.GameRepository;
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
	private BlobContentRepository blobContentRepository;

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
	public ControlPoint selectById(Long id, boolean image) {
		ControlPoint c = controlPointRepository.findOne(id);
		
		if (image && c.getImage() != null) {
			c.setImageData(blobContentRepository.findOne(c.getImage()));
		}
		
		return c;
	}
}

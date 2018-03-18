package org.runcity.db.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Event;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.Team;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.enumeration.EventStatus;
import org.runcity.db.entity.enumeration.EventType;
import org.runcity.db.entity.enumeration.TeamStatus;
import org.runcity.db.entity.util.TeamAggregate;
import org.runcity.db.entity.util.TeamRouteItem;
import org.runcity.db.repository.EventRepository;
import org.runcity.db.repository.RouteRepository;
import org.runcity.db.repository.TeamRepository;
import org.runcity.db.service.TeamService;
import org.runcity.exception.DBException;
import org.runcity.util.ResponseBody;
import org.runcity.util.ResponseClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@Transactional(rollbackFor = { DBException.class })
public class TeamServiceImpl implements TeamService {
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private RouteRepository routeRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	private void initialize(Team t, Team.SelectMode selectMode) {
		if (t == null) {
			return;
		}
		switch (selectMode) {
		case NONE:
			break;
		}
	}
	
	private void initialize(Collection<Team> teams, Team.SelectMode selectMode) {
		if (teams == null || selectMode == Team.SelectMode.NONE) {
			return;
		}
		for (Team t : teams) {
			initialize(t, selectMode);
		}
	}

	@Override
	public Team selectById(Long id, Team.SelectMode selectMode) {
		Team result = teamRepository.findOne(id);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public Team selectByNumberGame(String number, Game game, Team.SelectMode selectMode) {
		Team result = teamRepository.selectByGameAndNumber(number, game);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public TeamRouteItem selectByNumberCP(String number, ControlPoint controlPoint, Team.SelectMode selectMode) {
		TeamRouteItem result = teamRepository.selectByCPAndNumber(number, controlPoint);
		if (result != null) {
			initialize(result.getTeam(), selectMode);
		}
		return result;
	}
	
	@Override
	public Team addOrUpdate(Team team) throws DBException {
		try {
			if (team.getId() != null) {
				Team prev = selectById(team.getId(), Team.SelectMode.NONE);
				prev.update(team);
				return teamRepository.save(prev);
			} else {
				return teamRepository.save(team);
			}
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	private void delete(Long id) {
		teamRepository.delete(id);
	}
	
	@Override
	public void delete(List<Long> id) {
		for (Long i : id) {
			delete(i);
		}
	}

	@Override
	public List<Team> selectTeams(Long route, Team.SelectMode selectMode) {
		List<Team> result = selectTeams(routeRepository.findOne(route), selectMode);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public List<Team> selectTeams(Route route, Team.SelectMode selectMode) {
		List<Team> result = teamRepository.findByRoute(route);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public void processTeam(Team team, RouteItem ri, Volunteer volunteer, ResponseBody result) throws DBException {
		TeamStatus status;
		switch (ri.getControlPoint().getType()) {
		case STAGE_END:
			status = TeamStatus.ACTIVE;
			break;
		case FINISH:
			status = TeamStatus.FINISHED;
			break;
		default:
			status = null;
		}
		processTeam(team, status, ri.getLegNumber(), volunteer, EventType.TEAM_CP, result);
	}

	@Override
	public void setTeamStatus(Team team, TeamStatus status, Volunteer volunteer, ResponseBody result) throws DBException {
		processTeam(team, status, null, volunteer, EventType.TEAM_COORD, result);
	}

	private boolean validateNewStatus(Team team, TeamStatus status, Integer leg, ResponseBody result, MessageSource messageSource, Locale locale) {
		switch (status) {
		case ACTIVE:
		case FINISHED:
		case RETIRED:
			if (team.getStatus() != TeamStatus.ACTIVE) {
				result.setResponseClass(ResponseClass.ERROR);
				result.addCommonMsg("teamProcessing.invalidStatus", TeamStatus.getDisplayName(team.getStatus(), messageSource, locale));
				return false;			
			}
			if (leg != null && !leg.equals(team.getLeg())) {
				result.setResponseClass(ResponseClass.ERROR);
				result.addCommonMsg("teamProcessing.invalidLeg", team.getLeg());
				return false;	
			}
			break;
		case DISQUALIFIED:
			if (team.getStatus() == TeamStatus.DISQUALIFIED) {
				result.setResponseClass(ResponseClass.ERROR);
				result.addCommonMsg("teamProcessing.invalidStatus", TeamStatus.getDisplayName(team.getStatus(), messageSource, locale));
				return false;			
			}	
			break;
		case NOT_STARTED:
			if (team.getStatus() != TeamStatus.ACTIVE) {
				result.setResponseClass(ResponseClass.ERROR);
				result.addCommonMsg("teamProcessing.invalidStatus", TeamStatus.getDisplayName(team.getStatus(), messageSource, locale));
				return false;			
			}	
			if (!(new Integer(TeamStatus.getStoredValue(TeamStatus.ACTIVE)).equals(team.getLeg()))) {
				result.setResponseClass(ResponseClass.ERROR);
				result.addCommonMsg("teamProcessing.invalidLeg", team.getLeg());
				return false;	
			}
			break;
		}
		
		return true;
	}
	
	private void processTeam(Team team, TeamStatus status, Integer leg, Volunteer volunteer, EventType eventType, ResponseBody result) throws DBException {
		MessageSource messageSource = result.getMessageSource();
		Locale locale = result.getCurrentLocale();
		
		Team lock = teamRepository.selectForUpdate(team);
		
		if (!ObjectUtils.nullSafeEquals(team.getStatusData(), lock.getStatusData())) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.concurrencyError");
			return;
		}
		
		if (!validateNewStatus(team, status, leg, result, messageSource, locale)) {
			return;
		}
		
		String fromStatus = lock.getStatusData();
		
		switch (status) {
		case ACTIVE:
			lock.setLeg(leg + 1);
			break;
		default:
			if (status != null) {
				lock.setStatus(status);
			}
			break;
		}
		
		String toStatus = lock.getStatusData();
		
		Event pass = new Event(null, eventType, EventStatus.POSTED, volunteer.now(), null, volunteer, team, fromStatus, toStatus);		
		pass = eventRepository.save(pass);
		lock = teamRepository.save(lock);
		if (pass == null || team == null) {
			throw new DBException();
		}
	}

	@Override
	public Map<Route, Map<String, Long>> selectStatsByGame(Game game) {
		Map<Route, Map<String, Long>> result = new HashMap<Route, Map<String, Long>>();
		
		List<TeamAggregate> aggr = teamRepository.selectStatsByGame(game);
		for (TeamAggregate ta : aggr) {
			Map<String, Long> map = result.get(ta.getRoute());
			
			if (map == null) {
				map = new HashMap<String, Long>();
				
				for (TeamStatus ts : TeamStatus.values()) {
					if (ts != TeamStatus.ACTIVE) {
						map.put(TeamStatus.getStoredValue(ts), 0L);
					}
				}
				
				Long max = routeRepository.selectMaxLeg(ta.getRoute());
				for (long i = 1; i <= (max == null ? 1 : max); i++) {
					map.put(i + "", 0L);
				}
				result.put(ta.getRoute(), map);
			}
			
			map.put(ta.getStatus(), ta.getNumber());
		}
		
		return result;
	}
}

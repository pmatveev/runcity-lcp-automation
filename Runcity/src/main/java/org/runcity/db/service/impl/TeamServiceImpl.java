package org.runcity.db.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Hibernate;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Event;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.Team;
import org.runcity.db.entity.Team.SelectMode;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.enumeration.ControlPointMode;
import org.runcity.db.entity.enumeration.ControlPointType;
import org.runcity.db.entity.enumeration.EventStatus;
import org.runcity.db.entity.enumeration.EventType;
import org.runcity.db.entity.enumeration.TeamStatus;
import org.runcity.db.entity.util.TeamAggregate;
import org.runcity.db.entity.util.TeamRouteItem;
import org.runcity.db.repository.EventRepository;
import org.runcity.db.repository.GameRepository;
import org.runcity.db.repository.RouteItemRepository;
import org.runcity.db.repository.RouteRepository;
import org.runcity.db.repository.TeamRepository;
import org.runcity.db.service.ControlPointService;
import org.runcity.db.service.TeamService;
import org.runcity.exception.DBException;
import org.runcity.util.ActionResponseBody;
import org.runcity.util.ResponseClass;
import org.runcity.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@Transactional(rollbackFor = { DBException.class })
public class TeamServiceImpl implements TeamService {
	@Autowired
	private ControlPointService controlPointService;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private RouteRepository routeRepository;

	@Autowired
	private RouteItemRepository routeItemRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private GameRepository gameRepository;

	private void initialize(Team t, Team.SelectMode selectMode) {
		if (t == null) {
			return;
		}
		switch (selectMode) {
		case NONE:
			break;
		case WITH_ITEMS:
			Hibernate.initialize(t.getRoute().getRouteItems());
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

	private void initialize(Event e, Event.SelectMode selectMode) {
		if (e == null) {
			return;
		}
		switch (selectMode) {
		case NONE:
			break;
		case WITH_DELETE:
			switch (e.getType()) {
			case TEAM_ACTIVE:
				e.setCanDelete(e.getStatus() == EventStatus.POSTED);
				break;
			case TEAM_COORD:
			case TEAM_COORD_EXCEPTION:
			case TEAM_CP:
			case TEAM_CP_EXCEPTION:
				Long lastId = eventRepository.selectLastActiveStatusChangingEvent(e.getTeam());
				e.setCanDelete(ObjectUtils.nullSafeEquals(lastId, e.getId()));
				break;
			case VOLUNTEER_AT_CP:
				e.setCanDelete(false);
				break;
			}
		}
	}

	private void initialize(Collection<Event> events, Event.SelectMode selectMode) {
		if (events == null || selectMode == Event.SelectMode.NONE) {
			return;
		}
		for (Event e : events) {
			initialize(e, selectMode);
		}
	}

	@Override
	public Team selectById(Long id, Team.SelectMode selectMode) {
		Team result = teamRepository.findOne(id);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public Team selectByNumberGame(String number, Long game, Team.SelectMode selectMode) {
		return selectByNumberGame(number, gameRepository.findOne(game), selectMode);
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
	public List<Team> selectTeamsByRoute(Long route, Team.SelectMode selectMode) {
		List<Team> result = selectTeamsByRoute(routeRepository.findOne(route), selectMode);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public List<Team> selectTeamsByRoute(Route route, Team.SelectMode selectMode) {
		List<Team> result = teamRepository.findByRoute(route);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public void processTeam(Team team, String allowedStatus, RouteItem ri, Volunteer volunteer,
			ActionResponseBody result) throws DBException {
		TeamStatus status = null;
		Integer newLeg = null;
		switch (ri.getControlPoint().getType()) {
		case BONUS:
		case REGULAR:
		case START:
			status = TeamStatus.ACTIVE;
			break;
		case STAGE_END:
			status = TeamStatus.ACTIVE;
			newLeg = ri.getLegNumber() + 1;
			break;
		case FINISH:
			status = TeamStatus.FINISHED;
			break;
		}

		if (allowedStatus != null) {
			if (status == TeamStatus.ACTIVE && newLeg == null) {
				newLeg = ri.getLegNumber() == null ? 1 : ri.getLegNumber();
			}
		}

		processTeam(team, allowedStatus, status, ri.getLegNumber(), newLeg, volunteer, EventType.TEAM_CP, result);
	}

	@Override
	public void setTeamStatus(Team team, String allowedStatus, TeamStatus status, Integer leg, Volunteer volunteer,
			ActionResponseBody result) throws DBException {
		processTeam(team, allowedStatus, status, null, leg, volunteer, EventType.TEAM_COORD, result);
	}

	private boolean checkOfflineCp(Team team, Integer leg) {
		Integer current = team.getLeg();

		if (current == null || current >= leg) {
			return false;
		}

		int numOf = leg - current;
		// !! assume there is correct route configuration

		Hibernate.initialize(team.getRoute().getRouteItems());
		for (RouteItem ri : team.getRoute().getRouteItems()) {
			if (ri.getLegNumber() != null && ri.getControlPoint().getType() == ControlPointType.STAGE_END
					&& ri.getControlPoint().getMode() == ControlPointMode.OFFLINE) {
				if (current <= ri.getLegNumber() && leg > ri.getLegNumber()) {
					numOf--;
				}
			}
		}

		return numOf <= 0;
	}

	private boolean validateNewStatus(Team team, TeamStatus status, Integer leg, ActionResponseBody result,
			MessageSource messageSource, Locale locale) {
		switch (status) {
		case ACTIVE:
		case FINISHED:
		case RETIRED:
			if (team.getStatus() != TeamStatus.ACTIVE) {
				result.confirm(team.getStatusData(), "teamProcessing.validation.invalidStatus",
						TeamStatus.getDisplayName(team.getStatus(), messageSource, locale));
				return false;
			}
			if (leg != null && !leg.equals(team.getLeg())) {
				if (!checkOfflineCp(team, leg)) {
					result.confirm(team.getStatusData(), "teamProcessing.validation.invalidLeg", team.getLeg());
					return false;
				}
			}
			break;
		case DISQUALIFIED:
			if (team.getStatus() == TeamStatus.DISQUALIFIED) {
				result.confirm(team.getStatusData(), "teamProcessing.validation.invalidStatus",
						TeamStatus.getDisplayName(team.getStatus(), messageSource, locale));
				return false;
			}
			break;
		case NOT_STARTED:
			if (team.getStatus() != TeamStatus.ACTIVE) {
				result.confirm(team.getStatusData(), "teamProcessing.validation.invalidStatus",
						TeamStatus.getDisplayName(team.getStatus(), messageSource, locale));
				return false;
			}
			if (!(new Integer(TeamStatus.getStoredValue(TeamStatus.ACTIVE)).equals(team.getLeg()))) {
				result.confirm(team.getStatusData(), "teamProcessing.validation.invalidLeg", team.getLeg());
				return false;
			}
			break;
		}

		return true;
	}

	private void processTeam(Team team, String allowedStatus, TeamStatus status, Integer currLeg, Integer newLeg,
			Volunteer volunteer, EventType eventType, ActionResponseBody result) throws DBException {
		MessageSource messageSource = result.getMessageSource();
		Locale locale = result.getCurrentLocale();

		try {
			Team lock = teamRepository.selectForUpdate(team);

			if (!ObjectUtils.nullSafeEquals(team.getStatusData(), lock.getStatusData())) {
				result.setResponseClass(ResponseClass.ERROR);
				result.addCommonMsg("common.concurrencyError");
				return;
			}

			boolean force = false;
			if (!StringUtils.isEmpty(allowedStatus)) {
				if (ObjectUtils.nullSafeEquals(allowedStatus, lock.getStatusData())) {
					force = true;
				}
			}

			String fromStatus = lock.getStatusData();

			if (status != null) {
				if (!force) {
					if (!validateNewStatus(lock, status, currLeg, result, messageSource, locale)) {
						return;
					}
				}

				switch (status) {
				case ACTIVE:
					if (newLeg != null) {
						lock.setLeg(newLeg);
					}
					break;
				default:
					lock.setStatus(status);
					break;
				}
			}

			String toStatus = lock.getStatusData();

			Event pass = new Event(null, force ? eventType.getException() : eventType, EventStatus.POSTED,
					volunteer.now(), null, volunteer, team, fromStatus, toStatus);
			pass = eventRepository.save(pass);
			lock = teamRepository.save(lock);
			if (pass == null || team == null) {
				throw new RuntimeException("Team processing failed");
			}
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	@Override
	public void verifyTeam(Team team, Volunteer volunteer) throws DBException {
		try {
			Event event = new Event(null, EventType.TEAM_ACTIVE, EventStatus.POSTED, volunteer.now(), null, volunteer,
					team, null, null);
			event = eventRepository.save(event);

			if (event == null) {
				throw new RuntimeException("Cannot save event");
			}
		} catch (Throwable t) {
			throw new DBException(t);
		}
	}

	@Override
	public void rollbackTeamEvent(Event event, Volunteer performer, ActionResponseBody result) throws DBException {
		Team lock = teamRepository.selectForUpdate(event.getTeam());
		Long lastEvent = eventRepository.selectLastActiveStatusChangingEvent(lock);

		try {
			if (event.getType() != EventType.TEAM_ACTIVE && !ObjectUtils.nullSafeEquals(event.getId(), lastEvent)) {
				result.setResponseClass(ResponseClass.ERROR);
				result.addCommonMsg("event.validation.cantDeleteNotLast");
				return;
			}

			Event current = eventRepository.findOne(event.getId());
			if (current == null || current.getStatus() != EventStatus.POSTED) {
				result.setResponseClass(ResponseClass.ERROR);
				result.addCommonMsg("event.validation.inActive");
				return;
			}

			current.setStatus(EventStatus.REVERSED);
			current.setDateTo(performer.now());
			current.setClosedBy(performer);
			if (current.getFromTeamStatus() != null) {
				current.getTeam().setStatusData(current.getFromTeamStatus());
			}
			current = eventRepository.save(current);

			if (current == null) {
				throw new DBException("Event rollback failed");
			}
		} catch (Throwable t) {
			throw new DBException(t);
		}

		return;
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

	@Override
	public Long selectActiveNumberByRouteItem(RouteItem routeItem) {
		switch (routeItem.getControlPoint().getType()) {
		case FINISH:
			return teamRepository.selectActiveNumberByRoute(routeItem.getRoute()).longValue();
		case STAGE_END:
			return teamRepository.selectActiveNumberByRouteItem(routeItem.getRoute(), routeItem.getLegNumber())
					.longValue();
		default:
			return teamRepository.selectActiveNumberByRouteCP(routeItem.getRoute(),
					routeItem.getLegNumber() == null ? RouteItem.MAX_SORT : routeItem.getLegNumber(),
					routeItem.getControlPoint()).longValue();
		}
	}

	@Override
	public List<Team> selectPendingTeamsByCP(Long controlPoint, SelectMode selectMode) {
		ControlPoint main = controlPointService.selectById(controlPoint,
				ControlPoint.SelectMode.WITH_CHILDREN_AND_ITEMS);

		List<Team> result = new ArrayList<Team>();
		for (RouteItem ri : main.getRouteItems()) {
			result.addAll(selectPendingTeamsByRouteItem(ri, selectMode));
		}

		for (ControlPoint ch : main.getChildren()) {
			for (RouteItem ri : ch.getRouteItems()) {
				result.addAll(selectPendingTeamsByRouteItem(ri, selectMode));
			}
		}

		return result;
	}

	@Override
	public List<Team> selectPendingTeamsByRouteItem(Long routeItem, SelectMode selectMode) {
		return selectPendingTeamsByRouteItem(routeItemRepository.findOne(routeItem), selectMode);
	}

	@Override
	public List<Team> selectPendingTeamsByRouteItem(RouteItem routeItem, SelectMode selectMode) {
		List<Team> result;

		switch (routeItem.getControlPoint().getType()) {
		case FINISH:
			result = teamRepository.selectPendingByRoute(routeItem.getRoute());
			break;
		case STAGE_END:
			result = teamRepository.selectPendingByRouteLeg(routeItem.getRoute(), routeItem.getLegNumber());
			break;
		default:
			result = teamRepository.selectPendingByRouteCP(routeItem.getRoute(),
					routeItem.getLegNumber() == null ? RouteItem.MAX_SORT : routeItem.getLegNumber(),
					routeItem.getControlPoint().getMain());
		}

		initialize(result, selectMode);
		return result;
	}

	@Override
	public List<Team> selectTeamsByRouteWithStatus(Route route, String status, SelectMode selectMode) {
		List<Team> result;
		if (status == null) {
			result = teamRepository.findByRoute(route);
		} else {
			result = teamRepository.findByRouteAndStatus(route, status);
		}
		initialize(result, selectMode);
		return result;
	}

	@Override
	public Event selectTeamEvent(Long id, Event.SelectMode selectMode) {
		Event e = eventRepository.findOne(id);

		if (e != null && e.getTeam() == null) {
			e = null;
		}

		initialize(e, selectMode);

		return e;
	}

	@Override
	public List<Event> selectTeamEvents(ControlPoint controlPoint, Event.SelectMode selectMode) {
		List<Event> result = eventRepository.selectTeamEvents(controlPoint);
		initialize(result, selectMode);
		return result;
	}

	@Override
	public List<Event> selectTeamEvents(Team team, Event.SelectMode selectMode) {
		List<Event> result = eventRepository.selectTeamEvents(team);
		initialize(result, selectMode);
		return result;
	}
}

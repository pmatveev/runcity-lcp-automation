package org.runcity.db.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.runcity.db.entity.enumeration.EventStatus;
import org.runcity.db.entity.enumeration.EventType;
import org.springframework.util.ObjectUtils;

@Entity
@Table(name = "event")
public class Event {
	public enum SelectMode {
		NONE, WITH_DELETE;
	}
	
	@Transient
	private boolean datesUpdated = false;

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;

	@Column(name = "type", length = 1, nullable = false)
	private String type;

	@Column(name = "status", length = 1, nullable = false)
	private String status;

	@Transient
	private TimeZone tz;

	@Column(name = "date_from", columnDefinition = "datetime", nullable = false)
	private Date dateFrom;

	@Transient
	private Date utcDateFrom;

	@Column(name = "date_to", columnDefinition = "datetime", nullable = true)
	private Date dateTo;

	@Transient
	private Date utcDateTo;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "volunteer__id", nullable = false)
	private Volunteer volunteer;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "closed_by", nullable = true)
	private Volunteer closedBy;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "team__id", nullable = true)
	private Team team;

	@Column(name = "status_from", length = 1, nullable = true)
	private String fromTeamStatus;

	@Column(name = "status_to", length = 1, nullable = true)
	private String toTeamStatus;

	@Transient
	private Boolean canDelete;

	public Event() {
	}

	public Event(Long id, EventType type, EventStatus status, Date dateFrom, Date dateTo, Volunteer volunteer,
			Team team, String formTeamStatus, String toTeamStatus) {
		setId(id);
		setType(type);
		setStatus(status);
		setDateFrom(dateFrom);
		setDateTo(dateTo);
		setVolunteer(volunteer);
		setTeam(team);
		setFromTeamStatus(formTeamStatus);
		setToTeamStatus(toTeamStatus);
		datesUpdated = false;
	}

	private void updateDates() {
		if (!datesUpdated || !ObjectUtils.nullSafeEquals(getGame().getTz(), tz)) {
			tz = getGame().getTz();
			Calendar c = Calendar.getInstance();
			c.setTime(dateFrom);
			c.add(Calendar.MILLISECOND, -tz.getRawOffset());
			utcDateFrom = c.getTime();
			c.setTime(dateTo);
			c.add(Calendar.MILLISECOND, -tz.getRawOffset());
			utcDateTo = c.getTime();
			datesUpdated = true;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EventType getType() {
		return EventType.getByStoredValue(type);
	}

	public void setType(EventType type) {
		this.type = EventType.getStoredValue(type);
	}

	public EventStatus getStatus() {
		return EventStatus.getByStoredValue(status);
	}

	public void setStatus(EventStatus status) {
		this.status = EventStatus.getStoredValue(status);
	}

	public TimeZone getTz() {
		updateDates();
		return tz;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public Date getUtcDateFrom() {
		updateDates();
		return utcDateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
		datesUpdated = false;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public Date getUtcDateTo() {
		updateDates();
		return utcDateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
		datesUpdated = false;
	}

	public Volunteer getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(Volunteer volunteer) {
		this.volunteer = volunteer;
	}

	public Volunteer getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(Volunteer closedBy) {
		this.closedBy = closedBy;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
	
	public String getFromTeamStatus() {
		return fromTeamStatus;
	}

	public void setFromTeamStatus(String fromTeamStatus) {
		this.fromTeamStatus = fromTeamStatus;
	}

	public String getToTeamStatus() {
		return toTeamStatus;
	}

	public void setToTeamStatus(String toTeamStatus) {
		this.toTeamStatus = toTeamStatus;
	}

	public Game getGame() {
		return volunteer.getVolunteerGame();
	}

	public Boolean getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(Boolean canDelete) {
		this.canDelete = canDelete;
	}
}

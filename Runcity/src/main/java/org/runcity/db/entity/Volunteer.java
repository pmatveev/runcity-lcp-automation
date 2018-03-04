package org.runcity.db.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.runcity.db.entity.enumeration.VolunteerType;
import org.springframework.util.ObjectUtils;

@Entity
@Table(name = "volunteer")
public class Volunteer {
	public enum SelectMode {
		NONE;
	}
	
	@Transient
	private boolean datesUpdated = false;
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "consumer__id", nullable = false)
	private Consumer consumer;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "control_point__id", nullable = true)
	private ControlPoint controlPoint;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "game__id", nullable = true)
	private Game game;
	
	@Transient
	private TimeZone tz;

	@Column(name = "date_from", columnDefinition = "datetime", nullable = false)
	private Date dateFrom;
	
	@Transient
	private Date utcDateFrom;

	@Column(name = "date_to", columnDefinition = "datetime", nullable = false)
	private Date dateTo;
	
	@Transient
	private Date utcDateTo;
	
	public Volunteer() {
	}

	public Volunteer(Long id, Consumer consumer, ControlPoint controlPoint, Game game, Date dateFrom, Date dateTo) {
		setId(id);
		setConsumer(consumer);
		setControlPoint(controlPoint);
		setGame(game);
		setDateFrom(dateFrom);
		setDateTo(dateTo);
		datesUpdated = false;
	}
	
	private void updateDates() {
		if (!datesUpdated || !ObjectUtils.nullSafeEquals(getVolunteerGame().getTz(), tz)) {
			tz = getVolunteerGame().getTz();
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

	public Consumer getConsumer() {
		return consumer;
	}

	public void setConsumer(Consumer consumer) {
		this.consumer = consumer;
	}

	public ControlPoint getControlPoint() {
		return controlPoint;
	}

	public void setControlPoint(ControlPoint controlPoint) {
		this.controlPoint = controlPoint;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
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
		datesUpdated = false;
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}
	
	public Date getUtcDateTo() {
		updateDates();
		return utcDateTo;
	}

	public void setDateTo(Date dateTo) {
		datesUpdated = false;
		this.dateTo = dateTo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Volunteer other = (Volunteer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public Game getVolunteerGame() {
		return this.controlPoint == null ? this.game : this.controlPoint.getGame();
	}
	
	public VolunteerType getVolunteerType() {
		return this.controlPoint == null ? VolunteerType.COORDINATOR : VolunteerType.VOLUNTEER;
	}
}

package org.runcity.db.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.runcity.util.CollectionUtils;

@Entity
@Table(name = "game")
public class Game {
	public enum SelectMode {
		NONE, WITH_CATEGORIES;
	}
	
	@Transient
	private boolean datesUpdated = false;
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;

	@Column(name = "locale", length = 32, nullable = false)
	private String locale;

	@Column(name = "name", length = 32, nullable = false)
	private String name;

	@Column(name = "city", length = 32, nullable = false)
	private String city;

	@Column(name = "country", length = 32, nullable = false)
	private String country;

	@Column(name = "timezone", length = 32, nullable = false)
	private String timezone;
	
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

	@Column(name = "delay", columnDefinition = "int", nullable = true)
	private Integer delay;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "game", orphanRemoval = true)
	private Set<Route> categories;

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "game")
	private Set<ControlPoint> controlPoints;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "game", orphanRemoval = true)
	private List<Volunteer> volunteers;

	public Game() {
		this.categories = new HashSet<Route>();
	}

	public Game(Long id, String locale, String name, String city, String country, String timezone, Date dateFrom,
			Date dateTo, Integer delay, Set<Route> categories) {
		setId(id);
		setLocale(locale);
		setName(name);
		setCity(city);
		setCountry(country);
		setTimezone(timezone);
		setDateFrom(dateFrom);
		setDateTo(dateTo);
		setDelay(delay);
		if (categories != null) {
			this.categories = categories;
		}
		datesUpdated = false;
	}

	public void update(Game g) {
		this.locale = g.locale;
		this.name = g.name;
		this.city = g.city;
		this.country = g.country;
		this.timezone = g.timezone;
		this.dateFrom = g.dateFrom;
		this.dateTo = g.dateTo;
		this.delay = g.delay;
		CollectionUtils.applyChanges(categories, g.categories);
		datesUpdated = false;
	}
	
	private void updateDates() {
		if (!datesUpdated) {
			tz = TimeZone.getTimeZone(timezone);
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

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTimezone() {
		return timezone;
	}
	
	public TimeZone getTz() {
		updateDates();
		return tz;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
		datesUpdated = false;
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

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public Set<Route> getCategories() {
		return categories;
	}

	public List<String> getCategoryNames(String locale) {
		List<String> str = new ArrayList<String>();
		for (Route c : categories) {
			str.add(c.getCategory().getLocalizedName(locale));
		}
		return str;
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
		Game other = (Game) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void addCategory(Category category) {
		this.categories.add(new Route(null, this, category));
	}

	public String getDisplay() {
		return name + " (" + city + ", " + country + ")";
	}
}

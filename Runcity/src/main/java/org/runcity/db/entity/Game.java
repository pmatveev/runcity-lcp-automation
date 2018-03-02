package org.runcity.db.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.runcity.util.CollectionUtils;

@Entity
@Table(name = "game")
public class Game {
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

	@Column(name = "date_from", columnDefinition = "datetime", nullable = false)
	private Date dateFrom;

	@Column(name = "date_to", columnDefinition = "datetime", nullable = false)
	private Date dateTo;

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

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
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

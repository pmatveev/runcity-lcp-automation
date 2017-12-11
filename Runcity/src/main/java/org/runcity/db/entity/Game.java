package org.runcity.db.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.runcity.db.entity.util.DBEntity;

@Entity
@Table(name = "game")
public class Game implements DBEntity {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;

	@Column(name = "locale", length = 32, nullable = false)
	private String locale;
	
	@Column(name = "name", length = 32, unique = true, nullable = false)
	private String name;

	@Column(name = "city", length = 32, unique = true, nullable = false)
	private String city;

	@Column(name = "country", length = 32, unique = true, nullable = false)
	private String country;

	@Column(name = "game_date", columnDefinition = "datetime", nullable = false)
	private Date date;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "game_category", joinColumns = {
			@JoinColumn(name = "game__id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "category__id", nullable = false, updatable = false) })
	private Set<Category> categories;
	
	public Game() {
		this.categories = new HashSet<Category>();
	}
	
	public Game(Long id, String locale, String name, String city, String country, Date date, Set<Category> categories) {
		setId(id);
		setLocale(locale);
		setName(name);
		setCity(city);
		setCountry(country);
		setDate(date);
		if (categories != null) {
			this.categories = categories;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Set<Category> getCategories() {
		return categories;
	}
	
	public List<Long> getCategoryIds() {
		List<Long> str = new ArrayList<Long>();
		for (Category c : categories) {
			str.add(c.getId());
		}
		return str;
	}

	public List<String> getCategoryNames(String locale) {
		List<String> str = new ArrayList<String>();
		for (Category c : categories) {
			str.add(c.getNameDisplay(locale));
		}
		return str;
	}
}

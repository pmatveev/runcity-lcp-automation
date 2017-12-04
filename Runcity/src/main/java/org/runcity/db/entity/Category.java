package org.runcity.db.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "category")
public class Category {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "ref_record", referencedColumnName = "id")   
    @Filter(name="category")
	private List<Translation> names;

	@Column(name = "bgcolor", length = 6, nullable = false)
	private String bgcolor;

	@Column(name = "color", length = 6, nullable = false)
	private String color;

	@Column(name = "prefix", length = 6)
	private String prefix;

	public Category() {
		this.names = new ArrayList<Translation>();
	}

	public Category(Long id, List<Translation> names, String bgcolor, String color, String prefix) {
		this();
		this.id = id;
		this.bgcolor = bgcolor;
		this.color = color;
		this.prefix = prefix;
		if (names != null) {
			this.names = names;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Translation> getNames() {
		return names;
	}

	public Map<String, String> getStringNames() {
		Map<String, String> str = new HashMap<String, String>();
		for (Translation t : names) {
			str.put(t.getLocale(), t.getContent());
		}
		return str;
	}
	
	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}

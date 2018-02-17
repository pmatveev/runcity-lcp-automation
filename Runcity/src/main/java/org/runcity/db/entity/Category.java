package org.runcity.db.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.runcity.db.entity.util.TranslatedEntity;
import org.runcity.util.CollectionUtils;
import org.runcity.util.StringUtils;

@Entity
@Table(name = "category")
public class Category extends TranslatedEntity<Category> {
	private static final String DEFAULT_POSTFIX = "XX";
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "ref_record", referencedColumnName = "id")
	@Where(clause = "ref_table='category' and ref_column='name'")
	private List<Translation> names;

	@Column(name = "bgcolor", length = 6, nullable = false)
	private String bgcolor;

	@Column(name = "color", length = 6, nullable = false)
	private String color;

	@Column(name = "prefix", length = 6)
	private String prefix;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "ref_record", referencedColumnName = "id")
	@Where(clause = "ref_table='category' and ref_column='description'")
	private List<Translation> descriptions;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category", orphanRemoval = false)
	private Set<Route> games;

	public Category() {
		this.names = new ArrayList<Translation>();
		this.descriptions = new ArrayList<Translation>();
	}

	public Category(Long id, List<Translation> names, String bgcolor, String color, String prefix, List<Translation> descriptions) {
		this();
		setId(id);
		setBgcolor(bgcolor);
		setColor(color);
		setPrefix(prefix);
		if (names != null) {
			this.names = names;
		}
		if (descriptions != null) {
			this.descriptions = descriptions;
		}
	}

	public void update(Category c) {
		this.bgcolor = c.bgcolor;
		this.color = c.color;
		this.prefix = c.prefix;

		CollectionUtils.applyChanges(names, c.names);
		updateRef(names, getId());

		CollectionUtils.applyChanges(descriptions, c.descriptions);
		updateRef(descriptions, getId());
	}

	@Override
	public Category cloneForAdd() {
		return new Category(id, null, bgcolor, color, prefix, null);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
		for (Translation t : names) {
			t.setRefRecord(id);
		}
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
	
	public List<Translation> getDescriptions() {
		return descriptions;
	}

	public Map<String, String> getStringDescriptions() {
		Map<String, String> str = new HashMap<String, String>();
		for (Translation t : descriptions) {
			str.put(t.getLocale(), t.getContent());
		}
		return str;
	}
	
	public Set<Route> getGames() {
		return games;
	}

	public void addName(Translation t) {
		names.add(t);
	}

	public void addName(String locale, String name) {
		addName(new Translation(null, "category", "name", getId(), locale, name));
	}

	public String getLocalizedName(String locale) {
		return Translation.getDisplay(getNames(), locale);
	}

	public void addDescription(Translation t) {
		descriptions.add(t);
	}

	public void addDescription(String locale, String name) {
		addDescription(new Translation(null, "category", "description", getId(), locale, name));
	}

	public String getLocalizedDescription(String locale) {
		return Translation.getDisplay(getDescriptions(), locale);
	}

	public String getBadge() {
		return "<span class='label' style='background-color: #" + StringUtils.xss(bgcolor) + "; color: #"
				+ StringUtils.xss(color) + "'>" + StringUtils.xss(prefix + DEFAULT_POSTFIX) + "</span>";
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
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

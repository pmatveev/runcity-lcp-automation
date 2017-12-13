package org.runcity.db.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.GenericGenerator;
import org.runcity.db.entity.util.DBEntity;
import org.runcity.db.entity.util.TranslatedEntity;
import org.runcity.util.CollectionUtils;
import org.runcity.util.ObjectUtils;
import org.runcity.util.StringUtils;

@Entity
@Table(name = "category")
public class Category extends TranslatedEntity<Category> implements DBEntity {
	private static final String DEFAULT_POSTFIX = "XX";
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "ref_record", referencedColumnName = "id")
	@Filter(name = "category")
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
		setId(id);
		setBgcolor(bgcolor);
		setColor(color);
		setPrefix(prefix);
		if (names != null) {
			this.names = names;
		}
	}

	public void update(Category c) {
		this.bgcolor = c.bgcolor;
		this.color = c.color;
		this.prefix = c.prefix;

		CollectionUtils.applyChanges(names, c.names);
		updateRef(names, getId());
	}

	@Override
	public Category cloneForAdd() {
		return new Category(id, null, bgcolor, color, prefix);
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

	public void addName(Translation t) {
		names.add(t);
	}

	public void addName(String locale, String name) {
		addName(new Translation(null, "category", "name", getId(), locale, name));
	}

	public String getNameDisplay(String locale) {
		return Translation.getDisplay(getNames(), locale);
	}

	public String getBadge() {
		return "<span class='label' style='background-color: #" + StringUtils.xss(bgcolor) + "; color: #"
				+ StringUtils.xss(color) + "'>" + StringUtils.xss(prefix + DEFAULT_POSTFIX) + "</span>";
	}
	
	@Override
	public int hashCode() {
		return (id + "").hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Category)) {
			return false;
		}
		
		Category c = (Category) o;
		return ObjectUtils.equals(this, c);
	}
}

package org.runcity.db.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.runcity.db.entity.enumeration.ControlPointType;
import org.runcity.db.entity.util.TranslatedEntity;
import org.runcity.util.CollectionUtils;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

@Entity
@Table(name = "control_point")
@SQLDelete(sql = "delete cp, bc from control_point cp left outer join blob_content bc on bc.id = cp.image where cp.id = ?")
public class ControlPoint extends TranslatedEntity<ControlPoint> {
	public enum SelectMode {
		NONE, WITH_IMAGE, FOR_VOLUNTEER;
	}
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "game__id", nullable = false)
	private Game game;

	@Column(name = "type", length = 1, nullable = false)
	private String type;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "control_point__id", nullable = true)
	private ControlPoint parent;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent", orphanRemoval = true)
	private List<ControlPoint> children;

	@Column(name = "idt", length = 16, nullable = false)
	private String idt;

	@Column(name = "name", length = 255, nullable = true)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "ref_record", referencedColumnName = "id")
	@Where(clause = "ref_table='control_point' and ref_column='address'")
	private List<Translation> addresses;

	@Column(name = "description", length = 4000, nullable = false)
	private String description;

	@Column(name = "image", columnDefinition = "int", length = 18, nullable = true)
	private Long image;
	
	@Transient
	private byte[] imageData;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "controlPoint", orphanRemoval = true)
	private Set<RouteItem> routeItems;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "controlPoint", orphanRemoval = true)
	private List<Volunteer> volunteers;
	
	public ControlPoint() {
		this.addresses = new ArrayList<Translation>();
	}

	public ControlPoint(Long id, Game game, ControlPointType type, ControlPoint parent, String idt, String name,
			List<Translation> addresses, String description, Long image) {
		this();
		setId(id);
		setGame(game);
		setType(type);
		setParent(parent);
		setIdt(idt);
		setName(name);
		if (addresses != null) {
			this.addresses = addresses;
		}
		setDescription(description);
		setImage(image);
	}

	public void update(ControlPoint c) {
		this.game = c.game;
		this.type = c.type;
		this.parent = c.parent;
		this.idt = c.idt;
		this.name = c.name;
		this.description = c.description;
				
		CollectionUtils.applyChanges(addresses, c.addresses);
		updateRef(addresses, getId());
	}

	@Override
	public ControlPoint cloneForAdd() {
		return new ControlPoint(id, game, getType(), parent, idt, name, null, description, image);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	public ControlPointType getType() {
		return ControlPointType.getByStoredValue(type);
	}
	
	public void setType(ControlPointType type) {
		this.type = ControlPointType.getStoredValue(type);
	}

	public ControlPoint getParent() {
		return parent;
	}

	public void setParent(ControlPoint parent) {
		this.parent = parent;
	}
	
	public List<ControlPoint> getChildren() {
		return children;
	}

	public String getIdt() {
		return idt;
	}

	public void setIdt(String idt) {
		this.idt = idt;
	}

	public String getName() {
		return parent == null ? name : parent.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Translation> getAddresses() {
		return parent == null ? addresses : parent.addresses;
	}
	
	public String getLocalizedAddress(String locale) {
		return Translation.getDisplay(getAddresses(), locale);
	}
	
	public Map<String, String> getStringAddresses() {
		Map<String, String> str = new HashMap<String, String>();
		for (Translation t : addresses) {
			str.put(t.getLocale(), t.getContent());
		}
		return str;
	}
	
	public void addAddress(Translation t) {
		addresses.add(t);
	}

	public void addAddress(String locale, String name) {
		addAddress(new Translation(null, "control_point", "address", getId(), locale, name));
	}

	public Long getImage() {
		return image;
	}

	public void setImage(Long image) {
		this.image = image;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	
	public Set<RouteItem> getRouteItems() {
		return routeItems;
	}
	
	public List<Volunteer> getVolunteers() {
		return volunteers;
	}
	
	public String getNameDisplay(MessageSource messageSource, Locale l) {
		return StringUtils.xss(idt + " " + name);
	}
	
	public String getNameDisplayWithChildren() {
		ControlPoint cp = parent == null ? this : parent;
		StringBuilder sb = new StringBuilder(cp.idt);
		
		if (cp.children.size() > 0) {
			Collections.sort(cp.children, new Comparator<ControlPoint>() {
				@Override
				public int compare(ControlPoint o1, ControlPoint o2) {
					return o1.getIdt().compareTo(o2.getIdt());
				}
			});
			
			sb.append(" (");
			for (ControlPoint ch : cp.children) {
				sb.append(ch.getIdt());
				sb.append(", ");
			}
			sb.delete(sb.length() - 2, sb.length());
			sb.append(")");
		}
		
		sb.append(" ");
		sb.append(cp.name);
		
		return StringUtils.xss(sb.toString());
	}
	
	public String getNameDisplayWithType(MessageSource messageSource, Locale l) {
		return StringUtils.xss(idt + " (" + getType().getDisplayName(messageSource, l) + ") " + name);
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
		ControlPoint other = (ControlPoint) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

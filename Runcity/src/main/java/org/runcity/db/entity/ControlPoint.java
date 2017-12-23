package org.runcity.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.runcity.db.entity.util.DBEntity;
import org.runcity.db.entity.util.TranslatedEntity;
import org.runcity.util.CollectionUtils;
import org.runcity.util.StringUtils;

@Entity
@Table(name = "control_point")
public class ControlPoint extends TranslatedEntity<ControlPoint> implements DBEntity {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "game__id", nullable = false)
	private Game game;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "control_point__id", nullable = true)
	private ControlPoint parent;

	@Column(name = "idt", length = 16, nullable = false)
	private String idt;

	@Column(name = "name", length = 255, nullable = false)
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
	private BlobContent imageData;
	
	public ControlPoint() {
		this.addresses = new ArrayList<Translation>();
	}

	public ControlPoint(Long id, Game game, ControlPoint parent, String idt, String name,
			List<Translation> addresses, String description, Long image) {
		this();
		this.id = id;
		this.game = game;
		this.parent = parent;
		this.idt = idt;
		this.name = name;
		if (addresses != null) {
			this.addresses = addresses;
		}
		this.description = description;
		this.image = image;
	}

	public void update(ControlPoint c) {
		this.game = c.game;
		this.parent = c.parent;
		this.idt = c.idt;
		this.name = c.name;
		this.description = c.description;
				
		CollectionUtils.applyChanges(addresses, c.addresses);
		updateRef(addresses, getId());
		
		this.image = c.image;
	}

	@Override
	public ControlPoint cloneForAdd() {
		return new ControlPoint(id, game, parent, idt, name, null, description, image);
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

	public ControlPoint getParent() {
		return parent;
	}

	public void setParent(ControlPoint parent) {
		this.parent = parent;
	}

	public String getIdt() {
		return idt;
	}

	public void setIdt(String idt) {
		this.idt = idt;
	}

	public String getName() {
		return name;
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
		return addresses;
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

	public BlobContent getImageData() {
		return imageData;
	}

	public void setImageData(BlobContent imageData) {
		this.imageData = imageData;
	}
	
	public String getNameDisplay() {
		return StringUtils.xss(idt + " " + name);
	}
}

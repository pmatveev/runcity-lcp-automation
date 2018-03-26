package org.runcity.db.entity;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.runcity.db.entity.enumeration.TeamStatus;

@Entity
@Table(name = "team")
public class Team {
	public enum SelectMode {
		NONE, WITH_ITEMS;
	}
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "route__id", nullable = false)
	private Route route;

	@Column(name = "team_number", length = 32, nullable = false)
	private String number;

	@Column(name = "name", length = 255, nullable = false)
	private String name;

	@Column(name = "start_date", columnDefinition = "datetime", nullable = false)
	private Date start;

	@Column(name = "contact", length = 255, nullable = false)
	private String contact;

	@Column(name = "add_data", length = 4000, nullable = true)
	private String addData;

	@Column(name = "status", length = 2, nullable = false)
	private String status;

	public Team() {
	}

	public Team(Long id, Route route, String number, String name, Date start, String contact, String addData) {
		setId(id);
		setRoute(route);
		setNumber(number);
		setName(name);
		setStart(start);
		setContact(contact);
		setAddData(addData);
		if (id == null) {
			setStatus(TeamStatus.ACTIVE);
		}
	}
	
	public void update(Team t) {
		this.route = t.route;
		this.number = t.number;
		this.name = t.name;
		this.start = t.start;
		this.contact = t.contact;
		this.addData = t.addData;
		this.status = t.status == null ? this.status : t.status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddData() {
		return addData;
	}

	public void setAddData(String addData) {
		this.addData = addData;
	}
	
	public String getStatusData() {
		return status;
	}
	
	public TeamStatus getStatus() {
		return TeamStatus.getByStoredValue(status);
	}
	
	public Integer getLeg() {
		return getStatus() == TeamStatus.ACTIVE ? new Integer(status) : null;
	}
	
	public void setStatusData(String status) {
		this.status = status;
	}
	
	public void setStatus(TeamStatus status) {
		this.status = TeamStatus.getStoredValue(status);
	}
	
	public void setLeg(int leg) {
		this.status = leg + "";
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
		Team other = (Team) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

package org.runcity.db.entity;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "persistent_logins")
public class PersistentLogins {
	@Column(name = "username", length = 64, nullable = false)
	private String username;

	@Column(name = "series", length = 64, nullable = false)
	private String series;

	@Id
	@Column(name = "token", length = 64, nullable = false)
	private String token;

	@Column(name = "last_used", columnDefinition = "datetime", nullable = false)
	private Date lastUsed;

	public PersistentLogins() {
	}

	public PersistentLogins(String username, String series, String token, Date lastUsed) {
		super();
		this.username = username;
		this.series = series;
		this.token = token;
		this.lastUsed = lastUsed;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
	}
}

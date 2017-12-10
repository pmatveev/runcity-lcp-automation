package org.runcity.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.runcity.db.entity.util.DBEntity;
import org.runcity.util.CollectionUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "consumer")
public class Consumer implements DBEntity {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;

	@Column(name = "username", length = 32, unique = true, nullable = false)
	private String username;

	@Column(name = "is_active", columnDefinition = "BIT", length = 1, nullable = false)
	private Boolean active;

	@Column(name = "passhash", length = 256, nullable = false)
	private String passHash;

	@Column(name = "credentials", length = 32, nullable = false)
	private String credentials;

	@Column(name = "email", length = 255, unique = true, nullable = false)
	private String email;
	
	@Column(name = "locale", length = 16, nullable = true)
	private String locale;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "consumer", orphanRemoval = true)
	private List<ConsumerRole> roles;

	public Consumer() {
		this.roles = new ArrayList<ConsumerRole>();
	}

	public Consumer(Long id, String username, Boolean active, String password, String credentials, String email, String locale,
			List<ConsumerRole> roles) {
		this();
		setId(id);
		setUsername(username);
		setActive(active);

		if (password != null) {
			setPassHash(new BCryptPasswordEncoder(10).encode(password));
		}
		
		setCredentials(credentials);
		setEmail(email);
		setLocale(locale);
		if (roles != null) {
			this.roles = roles;
		}
	}

	public void update(Consumer c) {
		this.username = c.username;
		this.active = c.active;
		if (c.passHash != null) {
			this.passHash = c.passHash;
		}
		this.credentials = c.credentials;
		this.email = c.email;
		this.locale = c.locale;

		CollectionUtils.applyChanges(roles, c.roles);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getPassHash() {
		return passHash;
	}

	public void setPassHash(String passHash) {
		this.passHash = passHash;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public List<ConsumerRole> getRoles() {
		return roles;
	}

	public void addRole(ConsumerRole role) {
		roles.add(role);
	}

	public void addRole(String code) {
		addRole(new ConsumerRole(null, code, this));
	}

	public List<String> getRolesCodes() {
		List<String> str = new ArrayList<String>();
		for (ConsumerRole r : roles) {
			str.add(r.getCode());
		}
		return str;
	}
}

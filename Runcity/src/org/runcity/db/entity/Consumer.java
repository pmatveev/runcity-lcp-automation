package org.runcity.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.runcity.util.CollectionUtils;
import org.runcity.util.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "consumer")
public class Consumer {
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "consumer", orphanRemoval = true)
	private List<ConsumerRole> roles;

	public Consumer() {
	}

	public Consumer(Long id, String username, Boolean active, String password, String credentials, String email,
			List<ConsumerRole> roles) {
		this.id = id;
		this.username = username;
		this.active = active;
		if (password != null) {
			this.passHash = new BCryptPasswordEncoder(10).encode(password);
		}
		this.credentials = credentials;
		this.email = email;
		this.roles = roles;
		if (this.roles == null) {
			this.roles = new ArrayList<ConsumerRole>();
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

	public List<ConsumerRole> getRoles() {
		return roles;
	}

	public void addRole(ConsumerRole role) {
		roles.add(role);
	}

	public void addRole(String code) {
		addRole(new ConsumerRole(null, code, this));
	}

	public List<String> getStringRoles() {
		List<String> str = new ArrayList<String>();
		for (ConsumerRole r : roles) {
			str.add(r.getCode());
		}
		return str;
	}
	
	@Override
	public int hashCode() {
		return username.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Consumer)) {
			return false;
		}

		Consumer c = (Consumer) o;
		return StringUtils.isEqual(username, c.username);
	}
}

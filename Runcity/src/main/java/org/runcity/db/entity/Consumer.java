package org.runcity.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.runcity.db.entity.enumeration.SecureUserRole;
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

	@Column(name = "passhash", length = 256, nullable = true)
	private String passHash;

	@Column(name = "credentials", length = 32, nullable = false)
	private String credentials;

	@Column(name = "email", length = 255, unique = true, nullable = false)
	private String email;
	
	@Column(name = "locale", length = 16, nullable = true)
	private String locale;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "consumer", orphanRemoval = true)
	private List<ConsumerRole> roles;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "consumer", orphanRemoval = true)
	private List<Token> tokens;

	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "consumer", orphanRemoval = false)
	private List<Volunteer> volunteers;

	public Consumer() {
		this.roles = new ArrayList<ConsumerRole>();
	}

	public Consumer(Long id, String username, Boolean active, String password, String credentials, String email, String locale,
			List<ConsumerRole> roles) {
		this();
		setId(id);
		setUsername(username);
		setActive(active);

		if (!StringUtils.isEmpty(password)) {
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

	public void addRole(SecureUserRole role) {
		if (role != null) {
			addRole(new ConsumerRole(null, role, this));
		}
	}

	public void addRole(String role) {
		addRole(SecureUserRole.getByStoredValue(role));
	}

	public List<SecureUserRole> getRoleEnum() {
		List<SecureUserRole> res = new ArrayList<SecureUserRole>();
		for (ConsumerRole r : roles) {
			res.add(r.getRole());
		}
		return res;
	}
	
	public List<String> getRolesCodes() {
		List<String> str = new ArrayList<String>();
		for (ConsumerRole r : roles) {
			str.add(SecureUserRole.getStoredValue(r.getRole()));
		}
		return str;
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
		Consumer other = (Consumer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

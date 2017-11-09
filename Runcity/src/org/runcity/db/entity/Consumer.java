package org.runcity.db.entity;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.runcity.secure.SecureUserRole;
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
	private Boolean isActive;

	@Column(name = "passhash", length = 256, nullable = false)
	private String passHash;

	@Column(name = "credentials", length = 32, nullable = false)
	private String credentials;

	@Column(name = "email", length = 255, unique = true, nullable = false)
	private String email;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "consumer")
	private List<ConsumerRole> roles;

	public Consumer() {
	}

	public Consumer(Long id, String username, Boolean isActive, String password, String credentials, String email, List<ConsumerRole> roles) {
		this.id = id;
		this.username = username;
		this.isActive = isActive;
		this.passHash = new BCryptPasswordEncoder(10).encode(password);
		this.credentials = credentials;
		this.email = email;
		this.roles = roles;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
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
	
	public List<String> getRoleNames() {
		List<String> names = new LinkedList<String>();
		for (ConsumerRole r : roles) {
			SecureUserRole rr = SecureUserRole.getByCode(r.getCode());
			if (rr != null) {
				names.add(rr.getName());
			}
		}
		return names;
	}

	public void setRoles(List<ConsumerRole> roles) {
		this.roles = roles;
	}

}

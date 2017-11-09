package org.runcity.secure;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.runcity.db.entity.ConsumerRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class SecureUserDetails implements UserDetails {
	private Long id;
	private String username;
	private String password;
	private String credentials;
	private String email;
	private Set<GrantedAuthority> roles;

	public SecureUserDetails(Long id, String username, String password, String credentials, String email, List<ConsumerRole> roles) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.credentials = credentials;
		this.email = email;
		
		Set<GrantedAuthority> userRoles = new HashSet<GrantedAuthority>();
		for (ConsumerRole r : roles) {
			if (SecureUserRole.getByCode(r.getCode()) != null) {
				userRoles.add(new SimpleGrantedAuthority("ROLE_" + r.getCode()));
			}
		}
		this.roles = userRoles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getId() {
		return id;
	}
	
	public String getCredentials() {
		return credentials;
	}
	
	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
}

package org.runcity.secure;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class SecureUserDetails implements UserDetails {
	private String username;
	private String password;
	private String credentials;
	private Set<GrantedAuthority> roles;

	public SecureUserDetails(String username, String password, String credentials, Set<GrantedAuthority> roles) {
		this.username = username;
		this.password = password;
		this.credentials = credentials;
		this.roles = roles;
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

	public String getCredentials() {
		return credentials;
	}
}

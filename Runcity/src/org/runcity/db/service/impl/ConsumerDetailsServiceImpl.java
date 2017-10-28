package org.runcity.db.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.runcity.db.entity.Consumer;
import org.runcity.db.service.ConsumerService;
import org.runcity.secure.SecureUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ConsumerDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private ConsumerService consumerService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Consumer c = consumerService.selectByUsername(username);
		
		if (c == null) {
			throw new UsernameNotFoundException(username);
		}
		
		Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority("USER"));
		
		SecureUserDetails details = new SecureUserDetails(username, c.getPassHash(), c.getCredentials(), roles);
		return details;
	}

}

package org.runcity.db.service.impl;

import org.runcity.db.entity.Consumer;
import org.runcity.db.service.ConsumerService;
import org.runcity.secure.SecureUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
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

		SecureUserDetails details = new SecureUserDetails(c.getId(), c.getUsername(), c.isActive(), c.getPassHash(),
				c.getCredentials(), c.getEmail(), c.getRoles());
		return details;
	}

}

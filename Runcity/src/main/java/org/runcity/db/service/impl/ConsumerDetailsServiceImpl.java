package org.runcity.db.service.impl;

import org.runcity.db.entity.Consumer;
import org.runcity.db.service.ConsumerService;
import org.runcity.exception.DBException;
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
		Consumer c = consumerService.selectByUsername(username, true);

		if (c == null) {
			throw new UsernameNotFoundException(username);
		}

		// user can login - delete recovery tokens
		try {
			consumerService.invalidateRecoveryTokens(c);
		} catch (DBException e) {
		}
		
		SecureUserDetails details = new SecureUserDetails(c.getId(), c.getUsername(), c.isActive(), c.getPassHash(),
				c.getCredentials(), c.getEmail(), c.getLocale(), c.getRoles());
		return details;
	}

}

package org.runcity.db.service.impl;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.service.ConsumerService;
import org.runcity.db.service.VolunteerService;
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
	
	@Autowired
	private VolunteerService volunteerService;

	@Override
	public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
		Consumer c = consumerService.selectByEmail(identifier, Consumer.SelectMode.WITH_ROLES);

		if (c == null) {
			c = consumerService.selectByUsername(identifier, Consumer.SelectMode.WITH_ROLES);
		}

		if (c == null) {
			throw new UsernameNotFoundException(identifier);
		}
		
		// user can login - delete recovery tokens
		try {
			consumerService.invalidateRecoveryTokens(c);
		} catch (DBException e) {
		}
		
		Volunteer v = volunteerService.getCurrentByUsername(c.getUsername());
		
		SecureUserDetails details = new SecureUserDetails(c.getId(), c.getUsername(), c.isActive(), c.getPassHash(),
				c.getCredentials(), c.getEmail(), c.getLocale(), c.getRoleEnum(), v);
		return details;
	}

}

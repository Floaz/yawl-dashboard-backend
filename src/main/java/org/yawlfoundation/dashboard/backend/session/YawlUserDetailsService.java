/*
 * Copyright (c) 2004-2012 The YAWL Foundation. All rights reserved.
 * The YAWL Foundation is a collaboration of individuals and
 * organisations who are committed to improving workflow technology.
 *
 * This file is part of YAWL. YAWL is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation.
 *
 * YAWL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with YAWL. If not, see <http://www.gnu.org/licenses/>.
 */
package org.yawlfoundation.dashboard.backend.session;

import java.util.LinkedList;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.yawlfoundation.dashboard.backend.yawlclient.ResourceManager;
import org.yawlfoundation.dashboard.backend.yawlclient.model.Participant;



/**
 * The user details service that retrieves data from YAWL Resource Service.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class YawlUserDetailsService implements UserDetailsService {

	private ResourceManager resourceManager;


	public YawlUserDetailsService(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LinkedList<GrantedAuthority> authorities = new LinkedList<>();

		if(username.equals("admin")) {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			return new User("admin", "", authorities);
		}

		Participant participant = resourceManager.getParticipantByName(username);
		if(participant == null) {
			throw new UsernameNotFoundException("User \""+username+"\" not found!");
		}

		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		if(participant.isAdmin()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}

		// The next 3 lines prevent login for normal users.
		if(!participant.isAdmin()) {
			throw new LockedException("User \""+username+"\" found, but is not admin!");
		}

		return new User(participant.getUsername(), "", authorities);
	}

}

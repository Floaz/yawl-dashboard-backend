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

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.yawlfoundation.dashboard.backend.yawlclient.ResourceManager;



/**
 * The user details service that retrieves data from YAWL Resource Service.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class YawlAuthenticationProvider implements AuthenticationProvider {

	private final ResourceManager		resourceManager;

	private final UserDetailsService	userDetailsService;

	
	
	public YawlAuthenticationProvider(ResourceManager resourceManager, UserDetailsService userDetailsService) {
		this.resourceManager = resourceManager;
		this.userDetailsService = userDetailsService;
	}
	


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		UserDetails user = userDetailsService.loadUserByUsername(username);

		if(resourceManager.checkCredentials(username, password)) {
			Authentication auth = new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
			return auth;
		} else {
			return null;
		}
	}


	@Override
	public boolean supports(Class<?> type) {
		return true;
		//return UsernamePasswordAuthenticationToken.class.isAssignableFrom(type);
	}

}

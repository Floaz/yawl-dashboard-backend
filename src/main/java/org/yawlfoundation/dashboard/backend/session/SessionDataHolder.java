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

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;



/**
 * SessionDataController.
 *
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class SessionDataHolder {

	public String getUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getPrincipal().toString();
	}


	public List<String> getAuthorities(String role) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<String> authoritiesAsStrings = authentication.getAuthorities().stream()
																		   .map((t) -> t.getAuthority())
																		   .collect(Collectors.toList());
		return authoritiesAsStrings;
	}


	public UserData getUserData() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<String> authoritiesAsStrings = authentication.getAuthorities().stream()
																		   .map((t) -> t.getAuthority())
																		   .collect(Collectors.toList());
		return new UserData(authentication.getPrincipal().toString(), authoritiesAsStrings);
	}

}

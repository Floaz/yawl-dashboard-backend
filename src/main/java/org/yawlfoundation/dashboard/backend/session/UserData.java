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

import java.util.Collection;



/**
 * Data about a user.
 *
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class UserData {

	private String				username;

	private Collection<String>	authorities;


	public UserData(String username, Collection<String> authorities) {
		this.username = username;
		this.authorities = authorities;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public Collection<String> getAuthorities() {
		return authorities;
	}


	public void setAuthorities(Collection<String> authorities) {
		this.authorities = authorities;
	}

}

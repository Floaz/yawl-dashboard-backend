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
package org.yawlfoundation.dashboard.backend.yawlclient;

import java.util.List;
import org.yawlfoundation.dashboard.backend.yawlclient.model.Participant;
import org.yawlfoundation.dashboard.backend.yawlclient.model.Role;



/**
 * The service to manage participants and org data.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public interface ResourceManager {

	boolean		checkCredentials(String username, String password);

	List<String>	getAllParticipantNames();

	Participant	getParticipantById(String id);

	Participant	getParticipantByName(String name);

	String		addParticipant(String username, String password, String lastname, String firstname,
							   boolean admin, String description, String notes);

	void		removeParticipantByName(String name);

	void		removeParticipantById(String roleId);


	List<String>	getAllRoles();

	Role		getRoleById(String id);

	Role		getRoleByName(String name);

	String		addRole(String name, String description, String notes, String parentId);

	void		removeRoleByName(String name);

	void		removeRoleById(String roleId);

}

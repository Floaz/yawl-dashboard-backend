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
package org.yawlfoundation.dashboard.backend.endpoint.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.yawlfoundation.dashboard.backend.yawlclient.WorkItemManager;



/**
 * ParticipantsController.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
@RestController
@Secured("ROLE_ADMIN")
class SpecificationController {

	@Autowired
	private WorkItemManager workItemManager;



	@RequestMapping(value="/api/specification", method=RequestMethod.GET)
	public SpecificationListResource getAllParticipants() {
		return new SpecificationListResource(workItemManager.getAllLoadedSpecifications());
	}

}

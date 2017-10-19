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
package org.yawlfoundation.dashboard.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yawlfoundation.dashboard.backend.dashlet.DashletTypeRegistry;
import org.yawlfoundation.dashboard.backend.dashlet.notifications.NotificationsDashletType;
import org.yawlfoundation.dashboard.backend.dashlet.participanttable.ParticipantsTableDashletType;
import org.yawlfoundation.dashboard.backend.dashlet.runningcasestable.RunningCasesTableDashletType;



/**
 * The dashlet types configuration.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
@Configuration
class DashletTypesConfig {

	@Bean
	protected DashletTypeRegistry dashletTypeRegistry() {
		DashletTypeRegistry dashletTypeRegistry = new DashletTypeRegistry();
		dashletTypeRegistry.registerDashletType("participants-table", participantsTableDashletType());
		dashletTypeRegistry.registerDashletType("running-cases-table", runningCasesTableDashletType());
		dashletTypeRegistry.registerDashletType("notifications", notificationsDashletType());
		return dashletTypeRegistry;
	}

	@Bean
	protected ParticipantsTableDashletType participantsTableDashletType() {
		return new ParticipantsTableDashletType();
	}

	@Bean
	protected RunningCasesTableDashletType runningCasesTableDashletType() {
		return new RunningCasesTableDashletType();
	}

	@Bean
	protected NotificationsDashletType notificationsDashletType() {
		return new NotificationsDashletType();
	}


}

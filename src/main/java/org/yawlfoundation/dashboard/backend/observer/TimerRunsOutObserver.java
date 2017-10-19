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
package org.yawlfoundation.dashboard.backend.observer;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.yawlfoundation.dashboard.backend.observation.ObservationContext;
import org.yawlfoundation.dashboard.backend.yawlclient.WorkItemManager;
import org.yawlfoundation.yawl.engine.interfce.WorkItemRecord;



/**
 * This is a observation type.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class TimerRunsOutObserver extends WorkItemBasedObserver<TimerRunsOutObserverSettings> {

	private static final String SYMBOLIC_NAME = "timer-runs-out";
	private static final String DEFAULT_NOTIFICATION_TITLE = "Timer runs out";
	private static final Class<TimerRunsOutObserverSettings> SETTINGS_CLASS = TimerRunsOutObserverSettings.class;

	@Autowired
	private WorkItemManager workItemManager;



	public TimerRunsOutObserver() {
		super(SYMBOLIC_NAME, DEFAULT_NOTIFICATION_TITLE, SETTINGS_CLASS);
	}


	@Override
	protected Set<WorkItemRecord> getItems(ObservationContext context, WorkItemBasedObserverSettings settings) {
		LocalDateTime boundary = LocalDateTime.now().plusSeconds(settings.threshold);
		return workItemManager.getWorkItemsWithExpiringTimer(boundary);
	}

	@Override
	protected void getNodeData(ObjectNode dataNode, WorkItemRecord item) {
		super.getNodeData(dataNode, item);
		dataNode.put("expirationDate", Long.parseLong(item.getTimerExpiry()));
	}

}

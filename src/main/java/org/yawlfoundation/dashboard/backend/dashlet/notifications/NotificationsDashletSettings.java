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
package org.yawlfoundation.dashboard.backend.dashlet.notifications;

/**
 * The settings for the participant-table-dashlet.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class NotificationsDashletSettings {

	private Boolean		showCriticalNotifications = true;
	private Boolean		showWarningNotifications = true;
	private Boolean		showInfoNotifications = true;
	private Integer		dashletHeight = 512;


	public Boolean getShowCriticalNotifications() {
		return showCriticalNotifications;
	}


	public void setShowCriticalNotifications(Boolean showCriticalNotifications) {
		this.showCriticalNotifications = showCriticalNotifications;
	}


	public Boolean getShowWarningNotifications() {
		return showWarningNotifications;
	}


	public void setShowWarningNotifications(Boolean showWarningNotifications) {
		this.showWarningNotifications = showWarningNotifications;
	}


	public Boolean getShowInfoNotifications() {
		return showInfoNotifications;
	}


	public void setShowInfoNotifications(Boolean showInfoNotifications) {
		this.showInfoNotifications = showInfoNotifications;
	}


	public Integer getDashletHeight() {
		return dashletHeight;
	}


	public void setDashletHeight(Integer dashletHeight) {
		this.dashletHeight = dashletHeight;
	}
	
}

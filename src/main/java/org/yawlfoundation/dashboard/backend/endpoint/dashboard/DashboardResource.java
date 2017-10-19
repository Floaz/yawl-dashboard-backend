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
package org.yawlfoundation.dashboard.backend.endpoint.dashboard;

/**
 * A data model for a new dashlet.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class DashboardResource {

	private String id;
	private Integer orderNo;
	private String title;
	private String layoutType;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Integer getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getLayoutType() {
		return layoutType;
	}


	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}

}

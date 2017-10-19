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
package org.yawlfoundation.dashboard.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;



/**
 * Encapsulates list of participants.
 * @author Philipp Thomas
 */
@Entity
@Table(name="dashlet_instances")
public class DashletInstance {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "dashboard_id", nullable = false)
	private Dashboard dashboard;

	@Column(name = "dashlet_type")
	private String type;

	@Lob
	@Column(name = "dashlet_settings", length = 20971520)
	private byte[] settings;

	@Column(name = "dashlet_title")
	private String title;




	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public byte[] getSettings() {
		return settings;
	}


	public void setSettings(byte[] settings) {
		this.settings = settings;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Dashboard getDashboard() {
		return dashboard;
	}


	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}

}

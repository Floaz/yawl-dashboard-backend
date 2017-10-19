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

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;



/**
 * Model for observations.
 * @author Philipp Thomas
 */
@Entity
@Table(name="observations")
public class Observation {

	public static final byte STATUS_DISABLED = 0;
	public static final byte STATUS_ENABLED = 1;


	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id")
	private String			id;

	@Column(name = "status", nullable = false)
	private Byte			status;

	@Column(name = "observation_type", nullable = false)
	private String			type;

	@Column(name = "observation_title", nullable = false)
	private String			title;

	@Lob
	@Column(name = "observation_settings", length = 20971520)
	private byte[]			settings;

	@Column(name = "creator", nullable = true)
	private String			creator;

	@Column(name = "creation_date", nullable = false)
	private LocalDateTime	creationDate;

	@OneToMany(mappedBy = "observation", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	private List<Notification>	notifications;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Byte getStatus() {
		return status;
	}


	public void setStatus(Byte status) {
		this.status = status;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
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


	public String getCreator() {
		return creator;
	}


	public void setCreator(String creator) {
		this.creator = creator;
	}


	public LocalDateTime getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}


	public List<Notification> getNotifications() {
		return notifications;
	}


	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

}

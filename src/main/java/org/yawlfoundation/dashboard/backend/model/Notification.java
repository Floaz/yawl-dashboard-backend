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
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;



/**
 * Model for notifications.
 * @author Philipp Thomas
 */
@Entity
@Table(name="notifications")
public class Notification {

	public static final byte STATUS_ACTIVE = 0;
	public static final byte STATUS_MUTED = 1;
	public static final byte STATUS_DELETED = 2;

	public static final String PRIORITY_INFORMATION = "INFORMATION";
	public static final String PRIORITY_WARNING = "WARNING";
	public static final String PRIORITY_CRITICAL = "CRITICAL";


	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id")
	private String			id;

	@ManyToOne()
	@JoinColumn(name = "observation_id", nullable = false)
	private Observation		observation;

	@Column(name = "notification_status", nullable = false)
	private Byte			status;

	@Column(name = "notification_title", nullable = false)
	private String			title;

	@Column(name = "notification_priority", nullable = false)
	private String			priority;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "notification_data", length = 20971520)
	private byte[]			data;

	@Column(name = "delay_date", nullable = true)
	private LocalDateTime	delayDate;

	@Column(name = "creation_date", nullable = false)
	private LocalDateTime	creationDate;

	@OneToMany(mappedBy = "notification", cascade = CascadeType.REMOVE)
	private List<NotificationComment>	comments;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getPriority() {
		return priority;
	}


	public void setPriority(String priority) {
		this.priority = priority;
	}


	public Byte getStatus() {
		return status;
	}


	public void setStatus(Byte status) {
		this.status = status;
	}


	public Observation getObservation() {
		return observation;
	}


	public void setObservation(Observation observation) {
		this.observation = observation;
	}


	public byte[] getData() {
		return data;
	}


	public void setData(byte[] data) {
		this.data = data;
	}


	public LocalDateTime getDelayDate() {
		return delayDate;
	}


	public void setDelayDate(LocalDateTime delayDate) {
		this.delayDate = delayDate;
	}


	public LocalDateTime getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}


	public List<NotificationComment> getComments() {
		return comments;
	}


	public void setComments(List<NotificationComment> comments) {
		this.comments = comments;
	}

}

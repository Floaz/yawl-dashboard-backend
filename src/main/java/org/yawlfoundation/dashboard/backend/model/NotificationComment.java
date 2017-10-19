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
 * Model for notifications.
 * @author Philipp Thomas
 */
@Entity
@Table(name="notification_comments")
public class NotificationComment {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id")
	private String			id;

	@ManyToOne()
	@JoinColumn(name = "notification_id", nullable = false)
	private Notification	notification;

	@Lob
	@Column(name = "comment_text", length = 20971520, nullable = false)
	private String			commentText;

	@Column(name = "creator", nullable = true)
	private String			creator;

	@Column(name = "creation_date", nullable = false)
	private LocalDateTime	creationDate;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Notification getNotification() {
		return notification;
	}


	public void setNotification(Notification notification) {
		this.notification = notification;
	}


	public String getCommentText() {
		return commentText;
	}


	public void setCommentText(String commentText) {
		this.commentText = commentText;
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

}

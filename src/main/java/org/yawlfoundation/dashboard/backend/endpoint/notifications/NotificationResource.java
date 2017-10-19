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
package org.yawlfoundation.dashboard.backend.endpoint.notifications;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.LinkedList;
import java.util.List;



/**
 * A resource for notification data.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class NotificationResource {

	private String id;
	private Byte status;
	private Long creationDate;
	private Long delayDate;
	private String type;
	private String title;
	private String priority;
	private JsonNode data;
	private List<NotificationCommentResource> comments = new LinkedList<>();


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


	public Long getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}


	public Long getDelayDate() {
		return delayDate;
	}


	public void setDelayDate(Long delayDate) {
		this.delayDate = delayDate;
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


	public String getPriority() {
		return priority;
	}


	public void setPriority(String priority) {
		this.priority = priority;
	}


	public JsonNode getData() {
		return data;
	}


	public void setData(JsonNode data) {
		this.data = data;
	}


	public List<NotificationCommentResource> getComments() {
		return comments;
	}


	public void setComments(List<NotificationCommentResource> comments) {
		this.comments = comments;
	}

}

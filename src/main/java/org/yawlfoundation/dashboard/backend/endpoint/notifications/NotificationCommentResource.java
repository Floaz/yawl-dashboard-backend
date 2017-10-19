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



/**
 * NotificationCommentResource.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class NotificationCommentResource {

	private String id;
	private String comment;
	private String creator;
	private Long creationDate;


	public NotificationCommentResource(String id, String comment, String creator, Long creationDate) {
		this.id = id;
		this.comment = comment;
		this.creator = creator;
		this.creationDate = creationDate;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public String getCreator() {
		return creator;
	}


	public void setCreator(String creator) {
		this.creator = creator;
	}


	public Long getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}


}

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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.yawlfoundation.dashboard.backend.dao.NotificationCommentDao;
import org.yawlfoundation.dashboard.backend.dao.NotificationDao;
import org.yawlfoundation.dashboard.backend.model.Notification;
import org.yawlfoundation.dashboard.backend.model.NotificationComment;
import org.yawlfoundation.dashboard.backend.session.SessionDataHolder;



/**
 * NotificationsController.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
@RestController
@Secured("ROLE_ADMIN")
class NotificationsController {

	@Autowired
	private SessionDataHolder sessionDataHolder;

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private NotificationCommentDao notificationCommentDao;

	@Autowired
	private ObjectMapper objectMapper;



	@RequestMapping(value="/api/notification", method=RequestMethod.GET)
	@Transactional
	public NotificationListResource getAllNotifications() {
		List<NotificationResource> result = notificationDao.findAll()
												.stream()
												.sorted((t2, t1) -> t1.getCreationDate().compareTo(t2.getCreationDate()))
												.map(this::transformNotificationToResource)
												.collect(Collectors.toList());
		return new NotificationListResource(result);
	}


	@RequestMapping(value="/api/notification/{id}", method=RequestMethod.GET)
	@Transactional
	public NotificationResource getNotificationById(@PathVariable("id") String id) {
		Notification notification = notificationDao.findOne(id);
		if(notification == null) {
			throw new NotificationNotFoundException();
		}
		return transformNotificationToResource(notification);
	}


	@RequestMapping(value="/api/notification/{id}", method=RequestMethod.PUT)
	@Transactional
	public void editNotification(@PathVariable("id") String id, @RequestBody NotificationEditRequestData requestData) {
		Notification instance = notificationDao.findOne(id);
		if(instance == null) {
			throw new NotificationNotFoundException();
		}

		if(requestData.getStatus() != null) {
			if(!requestData.getStatus().equals(Notification.STATUS_ACTIVE)
				&& !requestData.getStatus().equals(Notification.STATUS_MUTED)) {
				throw new AttributesNotValidException();
			}
			instance.setStatus(requestData.getStatus());
		}

		if(requestData.getDelayUntil() != null) {
			if(requestData.getDelayUntil() == 0) {
				instance.setDelayDate(null);
			} else {
				instance.setDelayDate(Instant.ofEpochMilli(requestData.getDelayUntil()*1000)
											 .atZone(ZoneId.systemDefault())
											 .toLocalDateTime());
			}
		}

		notificationDao.save(instance);
	}


	@RequestMapping(value="/api/notification/{id}/comment", method=RequestMethod.POST)
	@Transactional
	public void addComment(@PathVariable("id") String id, @RequestBody CommentAddRequestData requestData) {
		Notification instance = notificationDao.findOne(id);
		if(instance == null) {
			throw new NotificationNotFoundException();
		}

		NotificationComment comment = new NotificationComment();
		comment.setCommentText(requestData.getComment());
		comment.setCreator(sessionDataHolder.getUsername());
		comment.setCreationDate(LocalDateTime.now());
		comment.setNotification(instance);
		instance.getComments().add(comment);

		notificationCommentDao.save(comment);
	}


	private NotificationResource transformNotificationToResource(Notification instance) {
		NotificationResource resource = new NotificationResource();
		resource.setId(instance.getId());
		resource.setStatus(instance.getStatus());
		resource.setPriority(instance.getPriority());
		resource.setType(instance.getObservation().getType());
		resource.setTitle(instance.getTitle());
		resource.setCreationDate(instance.getCreationDate().atZone(ZoneId.systemDefault()).toEpochSecond());
		if(instance.getDelayDate() != null) {
			resource.setDelayDate(instance.getDelayDate().atZone(ZoneId.systemDefault()).toEpochSecond());
		}

		try {
			resource.setData(objectMapper.readTree(instance.getData()));
		}
		catch(IOException ex) {
		}

		List<NotificationCommentResource> collection = instance.getComments()
				.stream()
				.sorted((t1, t2) -> t1.getCreationDate().compareTo(t2.getCreationDate()))
				.map((t) -> {
					return new NotificationCommentResource(
							t.getId(),
							t.getCommentText(),
							t.getCreator(),
							t.getCreationDate().atZone(ZoneId.systemDefault()).toEpochSecond());
				})
				.collect(Collectors.toList());
		resource.getComments().addAll(collection);


		return resource;
	}



	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such notification with this id")
	public class NotificationNotFoundException extends RuntimeException {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Attributes are not valid")
	public class AttributesNotValidException extends RuntimeException {
	}

}

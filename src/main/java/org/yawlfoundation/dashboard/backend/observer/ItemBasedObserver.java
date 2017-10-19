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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.yawlfoundation.dashboard.backend.dao.NotificationDao;
import org.yawlfoundation.dashboard.backend.model.Notification;
import org.yawlfoundation.dashboard.backend.notifications.NotificationStoreService;
import org.yawlfoundation.dashboard.backend.observation.AbstractObserver;
import org.yawlfoundation.dashboard.backend.observation.ObservationContext;



/**
 * This is a observation type.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public abstract class ItemBasedObserver<ItemType, ItemIdType, SettingsType extends ItemBasesObserverSettings> extends AbstractObserver {

	private static final Logger logger = LogManager.getLogger(ItemBasedObserver.class);


	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private NotificationStoreService notificationStoreService;

	@Autowired
	private ObjectMapper objectMapper;


	private final String defaultNotificationTitle;

	private final Class<? extends SettingsType> settingsClass;



	public ItemBasedObserver(String symbolicName, String defaultNotificationTitle, Class<? extends SettingsType> settingsClass) {
		super(symbolicName);
		this.defaultNotificationTitle = defaultNotificationTitle;
		this.settingsClass = settingsClass;
	}


	@Override
	public void runFullCheck(ObservationContext context) {
		SettingsType settings;
		try {
			settings = objectMapper.treeToValue(context.getObservationSettings(), settingsClass);
		} catch (JsonProcessingException ex) {
			throw new RuntimeException("Settings are not valid.");
		}

		Set<ItemType> items = getItems(context, settings);

		items = doFilter(context, settings, items);

		List<Notification> notifications = getNotificationsOfObservation(context);
		InspectionResult result = inspectExistingNotifications(notifications, items);
		deleteNotifications(result.toDelete);
		createNotifications(context, settings, result.toNotify);
	}


	@Transactional
	private List<Notification> getNotificationsOfObservation(ObservationContext context) {
		return notificationDao.findByObservation_Id(context.getObservationId());
	}


	private InspectionResult inspectExistingNotifications(List<Notification> notifications, Collection<ItemType> items) {
		InspectionResult result = new InspectionResult();
		result.toNotify.addAll(items);

		for(Notification notification : notifications) {
			try {
				JsonNode data = objectMapper.readValue(notification.getData(), JsonNode.class);

				ItemType item = findItem(data, items);

				if(item == null) {
					logger.info("Delete notification ID=\""+notification.getId()+"\", because not valid anymore.");
					result.toDelete.add(notification);
				} else {
					result.alreadyNotified.add(item);
					result.toNotify.remove(item);
				}
			}
			catch(Exception e) {
				logger.error("There is an error with the notification ID=\""+notification.getId()+"\". I delete it.", e);
				result.toDelete.add(notification);
			}
		}

		return result;
	}




	@Transactional
	private void deleteNotifications(Set<Notification> notifications) {
		for(Notification notification : notifications) {
			Notification toDelete = notificationDao.findOne(notification.getId());
			notificationDao.delete(toDelete);
		}
	}


	private void createNotifications(ObservationContext context, SettingsType settings, Collection<ItemType> items) {
		String title = defaultNotificationTitle;
		if(settings.notificationTitle.equals("SPECIFIED")) {
			title = settings.notificationSpecifiedTitle;
		}
		else if(settings.notificationTitle.equals("OBSERVATION_TITLE")) {
			title = context.getObservationTitle();
		}

		String priority = Notification.PRIORITY_CRITICAL;
		if(settings.priority.equals("INFO")) {
			priority = Notification.PRIORITY_INFORMATION;
		}
		else if(settings.notificationTitle.equals("WARN")) {
			priority = Notification.PRIORITY_WARNING;
		}

		for(ItemType item : items) {
			logger.debug("Add a notification...");
			ObjectNode data = objectMapper.getNodeFactory().objectNode();
			getNodeData(data, item);
			notificationStoreService.createNewNotification(context.getObservationId(), title, priority, data);
		}
	}


	protected abstract Set<ItemType> getItems(ObservationContext context, SettingsType settings);

	protected abstract Set<ItemType> doFilter(ObservationContext context, SettingsType settings, Set<ItemType> items);

	protected abstract ItemType findItem(JsonNode data, Collection<ItemType> items);

	protected abstract void getNodeData(ObjectNode dataNode, ItemType item);


	protected class InspectionResult {
		public Set<ItemType> toNotify = new HashSet<>();
		public Set<ItemType> alreadyNotified = new HashSet<>();
		public Set<Notification> toDelete = new HashSet<>();
	}

}

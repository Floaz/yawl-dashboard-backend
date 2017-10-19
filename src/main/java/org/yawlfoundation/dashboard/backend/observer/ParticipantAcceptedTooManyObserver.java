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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.yawlfoundation.dashboard.backend.observation.ObservationContext;
import org.yawlfoundation.dashboard.backend.yawlclient.WorkItemManager;
import org.yawlfoundation.dashboard.backend.yawlclient.model.Participant;
import org.yawlfoundation.dashboard.backend.yawlclient.model.WorkQueue;



/**
 * This is a observation type.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class ParticipantAcceptedTooManyObserver extends ItemBasedObserver<Participant, String, ParticipantAcceptedTooManyObserverSettings> {

	private static final String SYMBOLIC_NAME = "participant-accepted-too-many";
	private static final String DEFAULT_NOTIFICATION_TITLE = "Participant accepted too many";
	private static final Class<ParticipantAcceptedTooManyObserverSettings> SETTINGS_CLASS = ParticipantAcceptedTooManyObserverSettings.class;
	private static final String ITEM_ID_KEY = "participantId";

	@Autowired
	private WorkItemManager workItemManager;



	public ParticipantAcceptedTooManyObserver() {
		super(SYMBOLIC_NAME, DEFAULT_NOTIFICATION_TITLE, SETTINGS_CLASS);
	}


	@Override
	protected Set<Participant> getItems(ObservationContext context, ParticipantAcceptedTooManyObserverSettings settings) {
		return new HashSet<>(workItemManager.getAllParticipants());
	}


	@Override
	protected Set<Participant> doFilter(ObservationContext context, ParticipantAcceptedTooManyObserverSettings settings, Set<Participant> items) {
		Set<Participant> result = new HashSet<>();

		for(Participant participant : items) {
			boolean inFilterList = settings.filterParticipants.contains(participant.getUsername());

			if((settings.filterMode.equals("whitelist") && !inFilterList)
				|| (settings.filterMode.equals("blacklist") && inFilterList)) {
				continue;
			}

			int numberAccepted = workItemManager.getNumberQueuedWorkItemsById(participant.getId(), WorkQueue.ALLOCATED);
			numberAccepted += workItemManager.getNumberQueuedWorkItemsById(participant.getId(), WorkQueue.STARTED);

			if(numberAccepted >= settings.threshold) {
				result.add(participant);
			}
		}

		return result;
	}


	@Override
	protected Participant findItem(JsonNode data, Collection<Participant> items) {
		if(data.get(ITEM_ID_KEY).isNull() || !data.get(ITEM_ID_KEY).isTextual()) {
			throw new RuntimeException("There is no valid key "+ITEM_ID_KEY+".");
		}

		String itemId = data.get(ITEM_ID_KEY).textValue();
		return findInList(itemId, items);
	}


	private Participant findInList(String participantId, Collection<Participant> participants) {
		for(Participant participant : participants) {
			if(participant.getId().equals(participantId)) {
				return participant;
			}
		}
		return null;
	}


	@Override
	protected void getNodeData(ObjectNode dataNode, Participant item) {
		dataNode.put("participantId", item.getId());
		dataNode.put("participantUsername", item.getUsername());
		dataNode.put("participantName", item.getFirstname() + " " + item.getLastname());
	}

}

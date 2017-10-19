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
package org.yawlfoundation.dashboard.backend.dashlet.participanttable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.yawlfoundation.dashboard.backend.dao.DashletInstanceDao;
import org.yawlfoundation.dashboard.backend.dashlet.DashletSettingsService;
import org.yawlfoundation.dashboard.backend.dashlet.DashletType;
import org.yawlfoundation.dashboard.backend.dashlet.DashletTypeRegistry;
import org.yawlfoundation.dashboard.backend.model.DashletInstance;
import org.yawlfoundation.dashboard.backend.session.SessionDataHolder;
import org.yawlfoundation.dashboard.backend.yawlclient.ResourceManager;
import org.yawlfoundation.dashboard.backend.yawlclient.WorkItemManager;
import org.yawlfoundation.dashboard.backend.yawlclient.model.Participant;
import org.yawlfoundation.dashboard.backend.yawlclient.model.WorkQueue;
import org.yawlfoundation.yawl.engine.interfce.WorkItemRecord;



/**
 * SessionDataController.
 *
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
@RestController
@Secured("ROLE_ADMIN")
class ParticipantsTableDashletController {

	@Autowired
	private ResourceManager resourceManager;

	@Autowired
	private WorkItemManager workItemManager;

	@Autowired
	private DashletSettingsService dashletSettingsService;

	@Autowired
	private DashletTypeRegistry dashletTypeRegistry;

	@Autowired
	private DashletInstanceDao dashletInstanceDao;

	@Autowired
	private SessionDataHolder sessionDataHolder;



	@RequestMapping(value="/api/dashlet/participants-table/{dashletId}/data", method=RequestMethod.GET)
	public JsonNode getData(@PathVariable("dashletId") String dashletId) {
		DashletInstance instance = dashletInstanceDao.getOne(dashletId);
		if(instance == null) {
			throw new RuntimeException("This dashletId is not valid!");
		}

		if(!instance.getDashboard().getOwner().equals(sessionDataHolder.getUsername())) {
			throw new RuntimeException("This dashletId is not valid!");
		}

		DashletType dashletType = dashletTypeRegistry.getDashletType(instance.getType());
		if(dashletType == null) {
			throw new RuntimeException("The type of the dashlet is not supported!");
		}
		if(!(dashletType instanceof ParticipantsTableDashletType)) {
			throw new RuntimeException("This dashletId is not valid!");
		}

		ParticipantTableDashletSettings settings =
							dashletSettingsService.getSettingsById(dashletId, ParticipantTableDashletSettings.class);


		ArrayNode resultNode = JsonNodeFactory.instance.arrayNode();

		List<Participant> allParticipants = workItemManager.getAllParticipants();
		Set<Participant> selection = new HashSet<>();

		if(settings.getCustomSelection()) {
			List<Participant> filtered = allParticipants.stream()
					.filter((t) -> settings.getCustomSelectionParticipants().contains(t.getUsername()))
					.collect(Collectors.toList());
			selection.addAll(filtered);
			allParticipants.removeAll(filtered);

			for(Participant participant : selection) {
				resultNode.add(getDataForParticipant(participant.getId()));
			}
		}

		if(settings.getAutoSelection()) {
			allParticipants = allParticipants.stream()
					.filter((t) -> !settings.getAutoSelectionBlacklist().contains(t.getUsername()))
					.collect(Collectors.toList());

			if(settings.getAutoSelectionMode().equals("random")) {
				Collections.shuffle(allParticipants);
				resultNode.addAll(allParticipants.stream()
												.limit(settings.getAutoSelectionNumber())
												.map((t) -> getDataForParticipant(t.getId()))
												.collect(Collectors.toList()));
			}
			else {
				List<JsonNode> nodes = new ArrayList<>();
				for(Participant participant : allParticipants) {
					nodes.add(getDataForParticipant(participant.getId()));
				}

				if(!nodes.isEmpty()) {
					String sortColumn = settings.getAutoSelectionSortColumn();

					boolean columnIsTextType = nodes.get(0).get(sortColumn).isTextual();
					boolean descendingSort = settings.getAutoSelectionMode().equals("max");

					if(columnIsTextType) {
						nodes.sort((t1, t2) -> {
							int result = t1.get(sortColumn).textValue().compareTo(t2.get(sortColumn).textValue());
							if(descendingSort) result = Math.negateExact(result);
							return result;
						});
					} else {
						nodes.sort((t1, t2) -> {
							int result = t1.get(sortColumn).intValue()- t2.get(sortColumn).intValue();
							if(descendingSort) result = Math.negateExact(result);
							return result;
						});
					}

					resultNode.addAll(nodes.stream().limit(settings.getAutoSelectionNumber()).collect(Collectors.toList()));
				}
			}
		}

		ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
		rootNode.set("data", resultNode);
		return rootNode;
	}


	public JsonNode getDataForParticipant(String participantId) {
		Participant participant = resourceManager.getParticipantById(participantId);

		ObjectNode node = JsonNodeFactory.instance.objectNode();

		List<WorkItemRecord> allocated = workItemManager.getQueuedWorkItemsById(participantId, WorkQueue.ALLOCATED);
		List<WorkItemRecord> started = workItemManager.getQueuedWorkItemsById(participantId, WorkQueue.STARTED);

		node.put("username", participant.getUsername());
		node.put("firstname", participant.getFirstname());
		node.put("lastname", participant.getLastname());
		node.put("numberOffered", workItemManager.getNumberQueuedWorkItemsById(participantId, WorkQueue.OFFERED));
		node.put("numberAccepted", allocated.size());
		node.put("numberStarted", started.size());
		node.put("numberAcceptedAndStarted", allocated.size()+started.size());

		long allocatedIdleSum = allocated.stream()
				.map((t) -> Long.parseLong(t.getEnablementTimeMs())/1000)
				.mapToLong((t) -> (new Date().getTime()/1000) - t)
				.sum();
		node.put("idleTimeAccepted", allocatedIdleSum);

		long startedIdleSum = started.stream()
				.map((t) -> Long.parseLong(t.getStartTimeMs())/1000)
				.mapToLong((t) -> (new Date().getTime()/1000) - t)
				.sum();
		node.put("idleTimeStarted", startedIdleSum);

		node.put("sumIdleTime", allocatedIdleSum+startedIdleSum);

		return node;
	}

}

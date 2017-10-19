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
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.yawlfoundation.dashboard.backend.observation.ObservationContext;
import org.yawlfoundation.dashboard.backend.yawlclient.WorkItemManager;
import org.yawlfoundation.yawl.engine.interfce.WorkItemRecord;



/**
 * This is a observation type.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class UnofferedWorkItemsObserver extends ItemBasedObserver<WorkItemRecord, String, UnofferedWorkItemsObserverSettings> {

	private static final String SYMBOLIC_NAME = "unoffered-work-item";
	private static final String DEFAULT_NOTIFICATION_TITLE = "Unoffered work item in admin queue";
	private static final Class<UnofferedWorkItemsObserverSettings> SETTINGS_CLASS = UnofferedWorkItemsObserverSettings.class;
	private static final String ITEM_ID_KEY = "workItemId";

	@Autowired
	private WorkItemManager workItemManager;


	public UnofferedWorkItemsObserver() {
		super(SYMBOLIC_NAME, DEFAULT_NOTIFICATION_TITLE, SETTINGS_CLASS);
	}


	@Override
	protected Set<WorkItemRecord> getItems(ObservationContext context, UnofferedWorkItemsObserverSettings settings) {
		return workItemManager.getUnofferedWorkItems();
	}


	@Override
	protected Set<WorkItemRecord> doFilter(ObservationContext context, UnofferedWorkItemsObserverSettings settings, Set<WorkItemRecord> workItems) {
		Set<WorkItemRecord> result = new HashSet<>();

		if(settings.filterMode.equals("blacklist")) {
			result.addAll(workItems);
		}

		for(UnofferedWorkItemsObserverSettings.TaskFilter filter : settings.filterTasks) {
			Pattern pattern = Pattern.compile(filter.taskPattern);

			Iterator<WorkItemRecord> iterator = workItems.iterator();
			while(iterator.hasNext()) {
				WorkItemRecord record = iterator.next();
				if(record.getSpecIdentifier().equals(filter.specification.id)
					&& record.getSpecURI().equals(filter.specification.uri)
					&& record.getSpecVersion().equals(filter.specification.version)
					&& pattern.matcher(record.getTaskName()).matches()) {
					if(settings.filterMode.equals("whitelist")) {
						result.add(record);
					}
					else if(settings.filterMode.equals("blacklist")) {
						result.remove(record);
					}
				}
			}
		}

		return result;
	}


	@Override
	protected WorkItemRecord findItem(JsonNode data, Collection<WorkItemRecord> items) {
		if(data.get(ITEM_ID_KEY).isNull() || !data.get(ITEM_ID_KEY).isTextual()) {
			throw new RuntimeException("There is no valid key "+ITEM_ID_KEY+".");
		}

		String itemId = data.get(ITEM_ID_KEY).textValue();
		return findInList(itemId, items);
	}


	protected WorkItemRecord findInList(String itemId, Collection<WorkItemRecord> items) {
		for(WorkItemRecord record : items) {
			if(record.getID().equals(itemId)) {
				return record;
			}
		}
		return null;
	}


	@Override
	protected void getNodeData(ObjectNode dataNode, WorkItemRecord item) {
		dataNode.put("workItemId", item.getID());
		dataNode.put("caseId", item.getCaseID());
		dataNode.put("taskName", item.getTaskID());
		dataNode.put("specificationName", item.getSpecURI() + ' ' + item.getSpecVersion());

		ObjectNode specificationNode = JsonNodeFactory.instance.objectNode();
		specificationNode.put("id", item.getSpecIdentifier());
		specificationNode.put("uri", item.getSpecURI());
		specificationNode.put("version", item.getSpecVersion());
		dataNode.set("specification", specificationNode);
	}


}

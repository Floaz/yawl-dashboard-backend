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

import org.yawlfoundation.dashboard.backend.model.SpecificationId;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.yawlfoundation.dashboard.backend.observation.ObservationContext;
import org.yawlfoundation.dashboard.backend.yawlclient.WorkItemManager;
import org.yawlfoundation.dashboard.backend.yawlclient.model.Case;
import org.yawlfoundation.dashboard.backend.yawlclient.model.Specification;



/**
 * This is a observation type.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class CaseWithoutWorkItemsObserver extends ItemBasedObserver<Case, String, CaseWithoutWorkItemsObserverSettings> {

	private static final String SYMBOLIC_NAME = "case-without-work-items";
	private static final String DEFAULT_NOTIFICATION_TITLE = "Case without work items";
	private static final Class<CaseWithoutWorkItemsObserverSettings> SETTINGS_CLASS = CaseWithoutWorkItemsObserverSettings.class;
	private static final String ITEM_ID_KEY = "caseId";

	@Autowired
	private WorkItemManager workItemManager;



	public CaseWithoutWorkItemsObserver() {
		super(SYMBOLIC_NAME, DEFAULT_NOTIFICATION_TITLE, SETTINGS_CLASS);
	}


	@Override
	protected Set<Case> getItems(ObservationContext context, CaseWithoutWorkItemsObserverSettings settings) {
		return new HashSet<>(workItemManager.getAllRunningCases());
	}


	@Override
	protected Set<Case> doFilter(ObservationContext context, CaseWithoutWorkItemsObserverSettings settings, Set<Case> items) {
		Set<Integer> casesWithWorkItems = workItemManager.getAllCasesWithWorkItems();

		items = items.stream()
					.filter((t) -> !casesWithWorkItems.contains(t.getId()))
					.collect(Collectors.toSet());

		if(settings.filterMode.equals("blacklist")) {
			items = items.stream()
						.filter((t) -> !contains(settings.filterSpecifications, t.getSpecification()))
						.collect(Collectors.toSet());
		}
		else if(settings.filterMode.equals("whitelist")) {
			items = items.stream()
						.filter((t) -> contains(settings.filterSpecifications, t.getSpecification()))
						.collect(Collectors.toSet());
		}
		else {
			throw new RuntimeException("Illegal filter mode.");
		}

		return items;
	}


	@Override
	protected void getNodeData(ObjectNode dataNode, Case item) {
		dataNode.put("caseId", item.getId());
		dataNode.put("specificationName", item.getSpecification().getUri() + ' ' + item.getSpecification().getVersion());

		ObjectNode specificationNode = JsonNodeFactory.instance.objectNode();
		specificationNode.put("id", item.getSpecification().getId());
		specificationNode.put("uri", item.getSpecification().getUri());
		specificationNode.put("version", item.getSpecification().getSpecversion());
		dataNode.set("specification", specificationNode);
	}


	@Override
	protected Case findItem(JsonNode data, Collection<Case> items) {
		if(data.get("caseId").isNull() || !data.get("caseId").isInt()) {
			throw new RuntimeException("There is no valid caseId.");
		}

		int itemId = data.get(ITEM_ID_KEY).intValue();
		return findInList(itemId, items);
	}


	protected Case findInList(Integer itemId, Collection<Case> items) {
		for(Case record : items) {
			if(record.getId().equals(itemId)) {
				return record;
			}
		}
		return null;
	}


	protected static boolean contains(List<SpecificationId> specifications, Specification specification) {
		for(SpecificationId specificationId : specifications) {
			if(specification.getId().equals(specificationId.id)
				&& specification.getUri().equals(specificationId.uri)
				&& specification.getSpecversion().equals(specificationId.version)) {
				return true;
			}
		}
		return false;
	}

}

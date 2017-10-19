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
package org.yawlfoundation.dashboard.backend.dashlet.runningcasestable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
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
import org.yawlfoundation.dashboard.backend.model.SpecificationId;
import org.yawlfoundation.dashboard.backend.session.SessionDataHolder;
import org.yawlfoundation.dashboard.backend.yawlclient.WorkItemManager;
import org.yawlfoundation.dashboard.backend.yawlclient.model.Specification;



/**
 * SessionDataController.
 *
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
@RestController
@Secured("ROLE_ADMIN")
class RunningCasesTableDashletController {

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



	@RequestMapping(value="/api/dashlet/running-cases-table/{dashletId}/data", method=RequestMethod.GET)
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
		if(!(dashletType instanceof RunningCasesTableDashletType)) {
			throw new RuntimeException("This dashletId is not valid!");
		}

		RunningCasesTableDashletSettings settings =
							dashletSettingsService.getSettingsById(dashletId, RunningCasesTableDashletSettings.class);


		ArrayNode resultNode = JsonNodeFactory.instance.arrayNode();

		List<Specification> allSpecifications = workItemManager.getAllLoadedSpecifications();

		if(settings.getSelectionMode().equals("blacklist")) {
			allSpecifications = allSpecifications
									.stream()
									.filter((t) -> !contains(settings.getBlacklistedSpecifications(), t))
									.collect(Collectors.toList());
		} else {
			allSpecifications = allSpecifications
									.stream()
									.filter((t) -> contains(settings.getWhitelistedSpecifications(), t))
									.collect(Collectors.toList());
		}

		allSpecifications.forEach((specification) -> {
			resultNode.add(getDataForSpecification(specification));
		});

		ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
		rootNode.set("data", resultNode);
		return rootNode;
	}


	public JsonNode getDataForSpecification(Specification specification) {
		ObjectNode node = JsonNodeFactory.instance.objectNode();
		node.put("specificationName", specification.getUri());
		node.put("specificationVersion", specification.getSpecversion());
		node.put("numberCases", workItemManager.getRunningCasesBySpec(specification).size());
		return node;
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

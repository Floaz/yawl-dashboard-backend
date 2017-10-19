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
package org.yawlfoundation.dashboard.backend.endpoint.dashboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.yawlfoundation.dashboard.backend.dao.DashboardDao;
import org.yawlfoundation.dashboard.backend.dashlet.DashletTypeRegistry;
import org.yawlfoundation.dashboard.backend.model.Dashboard;
import org.yawlfoundation.dashboard.backend.session.SessionDataHolder;



/**
 * DashboardController.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
@RestController
class DashboardController {

	@Autowired
	private SessionDataHolder sessionDataHolder;

	@Autowired
	private DashboardDao dashboardDao;

	@Autowired
	private DashletTypeRegistry dashletTypeRegistry;

	@Autowired
	private ObjectMapper objectMapper;



	@RequestMapping(value="/api/dashboard", method=RequestMethod.GET)
	@ResponseBody
	@Transactional
	public DashboardListResource getAllDashboardsOfUser() {
		List<Dashboard> instanceList = dashboardDao.findByOwner(sessionDataHolder.getUsername());

		List<DashboardResource> result = instanceList.stream()
				.sorted(Comparator.comparingInt(Dashboard::getOrderNo))
				.map(DashboardController::transformDashboardToResource)
				.collect(Collectors.toList());

		return new DashboardListResource(result);
	}


	@RequestMapping(value="/api/dashboard/{id}", method=RequestMethod.GET)
	@ResponseBody
	@Transactional
	public DashboardResource getDashboardById(@PathVariable("id") String id) {
		Dashboard instance = dashboardDao.getOne(id);
		if(instance == null) {
			throw new DashboardNotFoundException();
		}
		if(!instance.getOwner().equals(sessionDataHolder.getUsername())) {
			throw new DashboardNotFoundException();
		}

		return transformDashboardToResource(instance);
	}


	@RequestMapping(value="/api/dashboard", method=RequestMethod.POST)
	@ResponseBody
	@Transactional
	public DashboardResource createNewDashboard(@RequestBody NewDashboardRequest newDashboardRequest)
										throws JsonProcessingException, InstantiationException, IllegalAccessException {
		if(newDashboardRequest.getTitle() == null) {
			throw new AttributesNotValidException();
		}

		List<Dashboard> dashboards = dashboardDao.findByOwner(sessionDataHolder.getUsername());
		int maxOrderNo = dashboards.stream().mapToInt(Dashboard::getOrderNo).max().orElse(0);

		Dashboard instance = new Dashboard();
		instance.setTitle(newDashboardRequest.getTitle());
		instance.setOwner(sessionDataHolder.getUsername());
		instance.setLayoutType("fixed-column-layout");
		instance.setLayoutSettings("{}".getBytes());
		instance.setOrderNo(maxOrderNo+1);

		dashboardDao.save(instance);

		return transformDashboardToResource(instance);
	}


	@RequestMapping(value="/api/dashboard/{id}", method=RequestMethod.PUT)
	@Transactional
	public void editDashboard(@PathVariable("id") String id, @RequestBody DashboardEditRequest editRequest) {
		Dashboard dashboard = dashboardDao.findOne(id);
		if(dashboard == null) {
			throw new DashboardNotFoundException();
		}

		if(!dashboard.getOwner().equals(sessionDataHolder.getUsername())) {
			throw new DashboardNotFoundException();
		}

		if(!StringUtils.isEmpty(editRequest.getTitle())) {
			dashboard.setTitle(editRequest.getTitle());
		}

		if(editRequest.getOrderNo() != null) {
			int currentOrderNo = dashboard.getOrderNo();
			if(currentOrderNo != editRequest.getOrderNo()) {
				Dashboard otherDashboard = dashboardDao.findByOwnerAndOrderNo(sessionDataHolder.getUsername(), editRequest.getOrderNo());
				if(otherDashboard == null) {
					throw new AttributesNotValidException();
				}
				dashboard.setOrderNo(editRequest.getOrderNo());
				otherDashboard.setOrderNo(currentOrderNo);
				dashboardDao.save(otherDashboard);
			}
		}

		dashboardDao.save(dashboard);
	}


	@RequestMapping(value="/api/dashboard/{id}", method=RequestMethod.DELETE)
	@Transactional
	public void removeDashboard(@PathVariable("id") String id) throws JsonProcessingException {
		Dashboard instance = dashboardDao.getOne(id);
		if(instance == null) {
			throw new DashboardNotFoundException();
		}

		if(!instance.getOwner().equals(sessionDataHolder.getUsername())) {
			throw new DashboardNotFoundException();
		}

		dashboardDao.delete(instance);
	}


	@RequestMapping(value="/api/dashboard/{id}/layout-settings", method=RequestMethod.GET)
	@Transactional
	public JsonNode getLayoutSettingsById(@PathVariable("id") String id) throws IOException, SQLException {
		Dashboard instance = dashboardDao.getOne(id);
		if(instance == null) {
			throw new DashboardNotFoundException();
		}

		if(!instance.getOwner().equals(sessionDataHolder.getUsername())) {
			throw new DashboardNotFoundException();
		}

		return objectMapper.readTree(instance.getLayoutSettings());
	}


	@RequestMapping(value="/api/dashboard/{id}/layout-settings", method=RequestMethod.PUT)
	@Transactional
	public void updateSettingsById(@PathVariable("id") String id, @RequestBody JsonNode newSettings) throws SQLException, JsonProcessingException {
		Dashboard instance = dashboardDao.getOne(id);
		if(instance == null) {
			throw new DashboardNotFoundException();
		}

		if(!instance.getOwner().equals(sessionDataHolder.getUsername())) {
			throw new DashboardNotFoundException();
		}

		byte[] newSettingsInBytes = objectMapper.writeValueAsBytes(newSettings);
		instance.setLayoutSettings(newSettingsInBytes);
		dashboardDao.save(instance);
	}


	private static DashboardResource transformDashboardToResource(Dashboard element) {
		DashboardResource resource = new DashboardResource();
		resource.setId(element.getId());
		resource.setOrderNo(element.getOrderNo());
		resource.setTitle(element.getTitle());
		resource.setLayoutType(element.getLayoutType());
		return resource;
	}


	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such dashboard with this id")
	public class DashboardNotFoundException extends RuntimeException {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Attributes are not valid")
	public class AttributesNotValidException extends RuntimeException {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "This type is not registered")
	public class DashletTypeNotFoundException extends RuntimeException {
	}

}

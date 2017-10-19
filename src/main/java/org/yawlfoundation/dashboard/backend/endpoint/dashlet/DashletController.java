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
package org.yawlfoundation.dashboard.backend.endpoint.dashlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.SQLException;
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
import org.yawlfoundation.dashboard.backend.dao.DashletInstanceDao;
import org.yawlfoundation.dashboard.backend.dashlet.DashletType;
import org.yawlfoundation.dashboard.backend.dashlet.DashletTypeRegistry;
import org.yawlfoundation.dashboard.backend.model.Dashboard;
import org.yawlfoundation.dashboard.backend.model.DashletInstance;
import org.yawlfoundation.dashboard.backend.session.SessionDataHolder;



/**
 * DashletController.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
@RestController
class DashletController {

	@Autowired
	private SessionDataHolder sessionDataHolder;

	@Autowired
	private DashboardDao dashboardDao;

	@Autowired
	private DashletInstanceDao dashletInstanceDao;

	@Autowired
	private DashletTypeRegistry dashletTypeRegistry;

	@Autowired
	private ObjectMapper objectMapper;



	@RequestMapping(value="/api/dashboard/{dashboardId}/dashlets", method=RequestMethod.GET)
	@ResponseBody
	@Transactional
	public DashletInstanceListResource getAllDashletsByDashboard(@PathVariable("dashboardId") String dashboardId) {
		Dashboard dashboard = dashboardDao.findOne(dashboardId);
		if(dashboard == null || !dashboard.getOwner().equals(sessionDataHolder.getUsername())) {
			throw new DashboardNotFoundException();
		}

		List<DashletInstance> instanceList = dashletInstanceDao.findByDashboard(dashboard);

		List<DashletInstanceResource> result = instanceList.stream()
				.map(DashletController::transformDashletInstanceToResource)
				.collect(Collectors.toList());

		return new DashletInstanceListResource(result);
	}


	@RequestMapping(value="/api/dashlet/{id}", method=RequestMethod.GET)
	@Transactional
	public DashletInstanceResource getDashletById(@PathVariable("id") String id) {
		DashletInstance instance = dashletInstanceDao.getOne(id);
		if(instance == null) {
			throw new DashletNotFoundException();
		}

		if(!instance.getDashboard().getOwner().equals(sessionDataHolder.getUsername())) {
			throw new DashletNotFoundException();
		}

		return transformDashletInstanceToResource(instance);
	}


	@RequestMapping(value="/api/dashboard/{dashboardId}/dashlet", method=RequestMethod.POST)
	@Transactional
	public DashletInstanceResource createNewDashlet(@PathVariable("dashboardId") String dashboardId,
													@RequestBody NewDashletRequest newDashletRequest)
										throws JsonProcessingException, InstantiationException, IllegalAccessException {
		if(newDashletRequest.getType() == null || newDashletRequest.getTitle() == null) {
			throw new AttributesNotValidException();
		}

		Dashboard dashboard = dashboardDao.findOne(dashboardId);
		if(dashboard == null || !dashboard.getOwner().equals(sessionDataHolder.getUsername())) {
			throw new DashboardNotFoundException();
		}

		DashletType dashletType = dashletTypeRegistry.getDashletType(newDashletRequest.getType());
		if(dashletType == null) {
			throw new DashletTypeNotFoundException();
		}

		DashletInstance instance = new DashletInstance();
		instance.setTitle(newDashletRequest.getTitle());
		instance.setType(newDashletRequest.getType());
		instance.setDashboard(dashboard);

		Class<?> settingsClass = dashletType.getSettingsClass();
		Object settings = settingsClass.newInstance();
		instance.setSettings(objectMapper.writeValueAsBytes(settings));

		dashletInstanceDao.save(instance);

		return transformDashletInstanceToResource(instance);
	}


	@RequestMapping(value="/api/dashlet/{id}", method=RequestMethod.PUT)
	@Transactional
	public void editDashboard(@PathVariable("id") String id, @RequestBody DashletEditRequest editRequest) {
		DashletInstance instance = dashletInstanceDao.findOne(id);
		if(instance == null) {
			throw new DashletNotFoundException();
		}

		if(!instance.getDashboard().getOwner().equals(sessionDataHolder.getUsername())) {
			throw new DashletNotFoundException();
		}

		if(!StringUtils.isEmpty(editRequest.getTitle())) {
			instance.setTitle(editRequest.getTitle());
		}

		dashletInstanceDao.save(instance);
	}


	@RequestMapping(value="/api/dashlet/{id}", method=RequestMethod.DELETE)
	@Transactional
	public void removeDashlet(@PathVariable("id") String id) throws JsonProcessingException {
		DashletInstance instance = dashletInstanceDao.getOne(id);
		if(instance == null) {
			throw new DashletNotFoundException();
		}

		if(!instance.getDashboard().getOwner().equals(sessionDataHolder.getUsername())) {
			throw new DashletNotFoundException();
		}

		dashletInstanceDao.delete(instance);
	}


	@RequestMapping(value="/api/dashlet/{id}/settings", method=RequestMethod.GET)
	@Transactional
	public JsonNode getSettingsById(@PathVariable("id") String id) throws IOException, SQLException {
		DashletInstance instance = dashletInstanceDao.getOne(id);
		if(instance == null) {
			throw new DashletNotFoundException();
		}

		if(!instance.getDashboard().getOwner().equals(sessionDataHolder.getUsername())) {
			throw new DashletNotFoundException();
		}

		return objectMapper.readTree(instance.getSettings());
	}


	@RequestMapping(value="/api/dashlet/{id}/settings", method=RequestMethod.PUT)
	@Transactional
	public void updateSettingsById(@PathVariable("id") String id, @RequestBody JsonNode newSettings) throws SQLException, JsonProcessingException {
		DashletInstance instance = dashletInstanceDao.getOne(id);
		if(instance == null) {
			throw new DashletNotFoundException();
		}

		if(!instance.getDashboard().getOwner().equals(sessionDataHolder.getUsername())) {
			throw new DashletNotFoundException();
		}

		byte[] newSettingsInBytes = objectMapper.writeValueAsBytes(newSettings);
		instance.setSettings(newSettingsInBytes);
		dashletInstanceDao.save(instance);
	}


	private static DashletInstanceResource transformDashletInstanceToResource(DashletInstance instance) {
		DashletInstanceResource resource = new DashletInstanceResource();
		resource.setId(instance.getId());
		resource.setTitle(instance.getTitle());
		resource.setType(instance.getType());
		return resource;
	}


	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such dashlet with this id")
	public class DashletNotFoundException extends RuntimeException {
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

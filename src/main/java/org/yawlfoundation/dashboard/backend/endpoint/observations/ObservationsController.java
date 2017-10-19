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
package org.yawlfoundation.dashboard.backend.endpoint.observations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.yawlfoundation.dashboard.backend.model.Observation;
import org.yawlfoundation.dashboard.backend.notifications.NotificationStoreService;
import org.yawlfoundation.dashboard.backend.session.SessionDataHolder;
import org.yawlfoundation.dashboard.backend.observation.ObserverRegistry;
import org.yawlfoundation.dashboard.backend.dao.ObservationDao;



/**
 * ObservationController.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
@RestController
@Secured("ROLE_ADMIN")
class ObservationsController {

	@Autowired
	private SessionDataHolder sessionDataHolder;

	@Autowired
	private ObserverRegistry observerRegistry;

	@Autowired
	private NotificationStoreService notificationStoreService;

	@Autowired
	private ObservationDao observationDao;

	@Autowired
	private ObjectMapper objectMapper;



	@RequestMapping(value="/api/observation", method=RequestMethod.GET)
	@Transactional
	public ObservationListResource getAllObservation() {
		List<ObservationsResource> result = observationDao.findAll()
												.stream()
												.map(this::transformObservationToResource)
												.collect(Collectors.toList());
		return new ObservationListResource(result);
	}


	@RequestMapping(value="/api/observation/{id}", method=RequestMethod.GET)
	@Transactional
	public ObservationsResource getObservationById(@PathVariable("id") String id) {
		Observation instance = observationDao.getOne(id);
		if(instance == null) {
			throw new ObservationNotFoundException();
		}

		return transformObservationToResource(instance);
	}


	@RequestMapping(value="/api/observation", method=RequestMethod.POST)
	@ResponseBody
	@Transactional
	public ObservationsResource createNewObservation(@RequestBody ObservationsResource requestData)
										throws JsonProcessingException, InstantiationException, IllegalAccessException {
		if(requestData.getType() == null
			|| observerRegistry.getObserver(requestData.getType()) == null) {
			throw new ObserverInvalidException();
		}

		if(requestData.getTitle() == null || requestData.getTitle().isEmpty()) {
			throw new AttributesNotValidException();
		}

		Observation instance = new Observation();
		instance.setTitle(requestData.getTitle());
		instance.setType(requestData.getType());
		instance.setCreator(sessionDataHolder.getUsername());
		instance.setCreationDate(LocalDateTime.now());
		instance.setStatus(Observation.STATUS_ENABLED);

		try {
			instance.setSettings(objectMapper.writeValueAsBytes(requestData.getSettings()));
		}
		catch(IOException ex) {
		}

		observationDao.save(instance);

		return transformObservationToResource(instance);
	}


	@RequestMapping(value="/api/observation/{id}", method=RequestMethod.PUT)
	@Transactional
	public void editObservation(@PathVariable("id") String id, @RequestBody ObservationsResource requestData) {
		Observation instance = observationDao.findOne(id);
		if(instance == null) {
			throw new ObservationNotFoundException();
		}

		if(requestData.getTitle() != null) {
			instance.setTitle(requestData.getTitle());
		}

		if(requestData.getStatus()!= null) {
			instance.setStatus(requestData.getStatus());
			if(requestData.getStatus().equals(Observation.STATUS_DISABLED)) {
				notificationStoreService.removeAllNotificationsByObservation(instance.getId());
			}
		}

		try {
			instance.setSettings(objectMapper.writeValueAsBytes(requestData.getSettings()));
		}
		catch(IOException ex) {
		}

		observationDao.save(instance);
	}


	@RequestMapping(value="/api/observation/{id}", method=RequestMethod.DELETE)
	@Transactional
	public void removeObservation(@PathVariable("id") String id) throws JsonProcessingException {
		Observation instance = observationDao.getOne(id);
		if(instance == null) {
			throw new ObservationNotFoundException();
		}

		observationDao.delete(instance);
	}


	private ObservationsResource transformObservationToResource(Observation instance) {
		ObservationsResource resource = new ObservationsResource();
		resource.setId(instance.getId());
		resource.setStatus(instance.getStatus());
		resource.setTitle(instance.getTitle());
		resource.setType(instance.getType());

		try {
			resource.setSettings(objectMapper.readTree(instance.getSettings()));
		}
		catch(IOException ex) {
		}

		return resource;
	}


	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such observation with this id")
	public class ObservationNotFoundException extends RuntimeException {
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "This observation type is not valid")
	public class ObserverInvalidException extends RuntimeException {
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Attributes are not valid")
	public class AttributesNotValidException extends RuntimeException {
	}

}

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
package org.yawlfoundation.dashboard.backend.observation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yawlfoundation.dashboard.backend.model.Observation;
import org.yawlfoundation.dashboard.backend.dao.ObservationDao;



/**
 * Runs the all active observations.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class ObservationRunner implements Runnable {

	private static final Logger logger = LogManager.getLogger(ObservationRunner.class);


	private final ObserverRegistry		registry;

	private final ObservationDao		observationDao;

	private final ObjectMapper			objectMapper;



	public ObservationRunner(ObserverRegistry registry, ObservationDao observationDao, ObjectMapper objectMapper) {
		this.registry = registry;
		this.observationDao = observationDao;
		this.objectMapper = objectMapper;
	}


	@Override
	public void run() {
		try {
			logger.debug("ObservationRunner start ...");
			long startTime = System.currentTimeMillis();

			List<ObservationContext> observations = getObservation();
			for(ObservationContext context : observations) {
				try {
					Observer observer = registry.getObserver(context.getObservationType());
					if(observer == null) {
						throw new RuntimeException("The observer is not registered.");
					}

					observer.runFullCheck(context);
				} catch (Exception ex) {
					logger.error("Error while running observer for observations: "
								 + "ID=\""+context.getObservationId()+"\" NAME=\""+context.getObservationTitle()+"\"", ex);
				}
			}

			logger.debug("ObservationRunner end");
			long endTime = System.currentTimeMillis();
			logger.debug("ObservationRunner needed "+(endTime-startTime)+" milliseconds!");
		} catch(Exception ex) {
			logger.error("Error while running observations. ", ex);
		}
	}


	@Transactional
	public List<ObservationContext> getObservation() {
		return observationDao.findByStatus(Observation.STATUS_ENABLED)
					.stream()
					.map(this::transformObservationToContext)
					.collect(Collectors.toList());
	}


	private ObservationContext transformObservationToContext(Observation observation) {
		try {
			ObservationContext context = new ObservationContext();
			context.setObservationId(observation.getId());
			context.setObservationTitle(observation.getTitle());
			context.setObservationType(observation.getType());
			context.setObservationSettings(objectMapper.readValue(observation.getSettings(), JsonNode.class));
			return context;
		} catch (IOException ex) {
			throw new RuntimeException("Could not parse observation settings. ID="+observation.getId());
		}
	}

}

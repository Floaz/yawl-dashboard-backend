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



/**
 * The context of an observation.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class ObservationContext {

	private String observationId;
	private String observationTitle;
	private String observationType;
	private JsonNode observationSettings;

	public String getObservationId() {
		return observationId;
	}

	public void setObservationId(String observationId) {
		this.observationId = observationId;
	}

	public String getObservationTitle() {
		return observationTitle;
	}

	public void setObservationTitle(String observationTitle) {
		this.observationTitle = observationTitle;
	}

	public String getObservationType() {
		return observationType;
	}

	public void setObservationType(String observationType) {
		this.observationType = observationType;
	}

	public JsonNode getObservationSettings() {
		return observationSettings;
	}

	public void setObservationSettings(JsonNode observationSettings) {
		this.observationSettings = observationSettings;
	}

}

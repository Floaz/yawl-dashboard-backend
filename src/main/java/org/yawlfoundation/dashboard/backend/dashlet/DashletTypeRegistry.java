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
package org.yawlfoundation.dashboard.backend.dashlet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



/**
 * Holds all dashlet types.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class DashletTypeRegistry {

	private final Map<String, DashletType>	typesMap = new HashMap<>();



	public DashletType getDashletType(String symbolicName) {
		return typesMap.get(symbolicName);
	}


	public Collection<DashletType> getAllDashletTypes() {
		return Collections.unmodifiableCollection(typesMap.values());
	}


	public void registerDashletType(String symbolicName, DashletType dashletType) {
		typesMap.put(symbolicName, dashletType);
	}


	public void deregisterDashletType(String symbolicName) {
		typesMap.remove(symbolicName);
	}


	public void deregisterDashletType(DashletType dashletType) {
		typesMap.values().remove(dashletType);
	}
}

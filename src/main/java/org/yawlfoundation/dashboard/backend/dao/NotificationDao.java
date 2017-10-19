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
package org.yawlfoundation.dashboard.backend.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yawlfoundation.dashboard.backend.model.Notification;
import org.yawlfoundation.dashboard.backend.model.Observation;



/**
 * NotificationDao.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public interface NotificationDao extends JpaRepository<Notification, String> {

	List<Notification> findByStatus(Byte status);

	List<Notification> findByObservation(Observation observation);

	List<Notification> findByObservation_Id(String id);

}

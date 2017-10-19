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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.SQLException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.yawlfoundation.dashboard.backend.dao.DashletInstanceDao;
import org.yawlfoundation.dashboard.backend.model.DashletInstance;



/**
 * DashletController.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class DashletSettingsService {

	@Autowired
	private DashletInstanceDao dashletInstanceDao;

	@Autowired
	private ObjectMapper objectMapper;


	@Transactional
	public <T> T getSettingsById(String id, Class<T> settingsClass) {
		DashletInstance instance = dashletInstanceDao.getOne(id);
		if(instance == null) {
			throw new DashletNotFoundException();
		}

		try {
			return objectMapper.readValue(instance.getSettings(), settingsClass);
		}
		catch(IOException e) {
			throw new RuntimeException("The settings could not be converted to settings class.", e);
		}
	}


	@Transactional
	public void saveSettingsById(@PathVariable("id") String id, Object newSettings) throws SQLException, JsonProcessingException {
		DashletInstance instance = dashletInstanceDao.getOne(id);
		if(instance == null) {
			throw new DashletNotFoundException();
		}

		byte[] newSettingsInBytes = objectMapper.writeValueAsBytes(newSettings);
		instance.setSettings(newSettingsInBytes);
		dashletInstanceDao.save(instance);
	}


	public class DashletNotFoundException extends RuntimeException {
	}

}

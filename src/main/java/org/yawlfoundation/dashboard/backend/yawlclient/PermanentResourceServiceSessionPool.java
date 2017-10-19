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
package org.yawlfoundation.dashboard.backend.yawlclient;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.yawlfoundation.yawl.resourcing.rsInterface.ResourceGatewayClient;

/**
 * PermanentResourceServiceSessionPool.
 * @author Philipp Thomas <philipp.thomas@floaz.de>
 */
public class PermanentResourceServiceSessionPool implements ResourceServiceSessionPool {

	private static Logger logger = LogManager.getLogger(PermanentResourceServiceSessionPool.class);

	private final ResourceGatewayClient	connection;
	private final String				username;
	private final String				password;

	private String						handle = null;


	public PermanentResourceServiceSessionPool(ResourceGatewayClient connection, String username, String password) {
		this.connection = connection;
		this.username = username;
		this.password = password;
	}


	@PostConstruct
	public synchronized void init() {
		try {
			connect();
		}
		catch(Exception e) {
			logger.error("Could not connect to YAWL-Resource-Service.");
		}
	}


	public synchronized void connect() throws IOException {
		if(isConnected()) {
			disconnect();
		}

		handle = connection.connect(username, password);
		if(!connection.successful(handle)) {
			handle = null;
			throw new RuntimeException("Could not connect! ");
		}
	}


	@PreDestroy
	public synchronized void disconnect() throws IOException {
		if(handle == null || connection == null) {
			return;
		}

		try {
			connection.disconnect(handle);
		}
		finally {
			handle = null;
		}
	}


    @Scheduled(fixedRate = 5000)
    public synchronized void refreshSessionHandle() throws IOException {
        String result = connection.checkConnection(handle);
		if(!connection.successful(result)) {
			reconnect();
		}
    }


    @Scheduled(fixedRate = 10000)
    public synchronized void reconnect() throws IOException {
        disconnect();
		connect();
    }


	public synchronized boolean isConnected() {
		return handle != null;
	}


	public synchronized void checkConnected() {
		if(!isConnected()) {
			throw new RuntimeException("No session handle available!");
		}
	}


	@Override
	public synchronized ResourceServiceSessionHandle getHandle() {
		if(!isConnected()) {
			try {
				connect();
			}
			catch(Exception e) {
				throw new RuntimeException("No session handle available!");
			}
		}
		return new SimpleResourceManagerSessionHandle(handle);
	}

}

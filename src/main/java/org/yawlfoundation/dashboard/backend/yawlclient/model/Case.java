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
package org.yawlfoundation.dashboard.backend.yawlclient.model;

import java.time.LocalDateTime;

/**
 * Role.
 * @author Philipp Thomas
 */
public class Case {

	private Integer id;
	private LocalDateTime	startTime;
	private Specification specification;


	public Case(Integer id, Specification specification) {
		this.id = id;
		this.specification = specification;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public LocalDateTime getStartTime() {
		return startTime;
	}


	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}


	public Specification getSpecification() {
		return specification;
	}


	public void setSpecification(Specification specification) {
		this.specification = specification;
	}



}

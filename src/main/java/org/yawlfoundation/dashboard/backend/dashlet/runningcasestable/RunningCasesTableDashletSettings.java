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
package org.yawlfoundation.dashboard.backend.dashlet.runningcasestable;

import java.util.LinkedList;
import java.util.List;
import org.yawlfoundation.dashboard.backend.model.SpecificationId;

/**
 * The settings for the participant-table-dashlet.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class RunningCasesTableDashletSettings {

	private String		sortColumn = "specificationName";
	private String		sortColumnDirection = "ASC";
	private String		selectionMode = "blacklist";
	private List<SpecificationId> whitelistedSpecifications = new LinkedList<>();
	private List<SpecificationId> blacklistedSpecifications = new LinkedList<>();


	public String getSortColumn() {
		return sortColumn;
	}


	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}


	public String getSortColumnDirection() {
		return sortColumnDirection;
	}


	public void setSortColumnDirection(String sortColumnDirection) {
		this.sortColumnDirection = sortColumnDirection;
	}


	public String getSelectionMode() {
		return selectionMode;
	}


	public void setSelectionMode(String selectionMode) {
		this.selectionMode = selectionMode;
	}


	public List<SpecificationId> getWhitelistedSpecifications() {
		return whitelistedSpecifications;
	}


	public void setWhitelistedSpecifications(List<SpecificationId> whitelistedSpecifications) {
		this.whitelistedSpecifications = whitelistedSpecifications;
	}


	public List<SpecificationId> getBlacklistedSpecifications() {
		return blacklistedSpecifications;
	}


	public void setBlacklistedSpecifications(List<SpecificationId> blacklistedSpecifications) {
		this.blacklistedSpecifications = blacklistedSpecifications;
	}

}

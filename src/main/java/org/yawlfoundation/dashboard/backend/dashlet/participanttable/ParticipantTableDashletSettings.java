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
package org.yawlfoundation.dashboard.backend.dashlet.participanttable;

import java.util.LinkedList;
import java.util.List;

/**
 * The settings for the participant-table-dashlet.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public class ParticipantTableDashletSettings {

	private String		sortName = "lastname";
	private Boolean		showColumnUsername = false;
	private Boolean		showColumnName = true;
	private Boolean		showColumnNumberOffered = true;
	private Boolean		showColumnNumberAccepted = true;
	private Boolean		showColumnNumberStarted = true;
	private Boolean		showColumnNumberAcceptedAndStarted = false;
	private Boolean		showColumnIdleTimeAccepted = false;
	private Boolean		showColumnIdleTimeStarted = false;
	private Boolean		showColumnSumIdleTime = true;
	private String		sortColumn = "lastname";
	private String		sortColumnDirection = "ASC";
	private Boolean		customSelection = false;
	private List<String> customSelectionParticipants = new LinkedList<>();
	private Boolean		autoSelection = true;
	private Integer		autoSelectionNumber = 5;
	private String		autoSelectionMode = "max";
	private String		autoSelectionSortColumn = "sumIdleTime";
	private List<String> autoSelectionBlacklist = new LinkedList<>();


	public String getSortName() {
		return sortName;
	}


	public void setSortName(String sortName) {
		this.sortName = sortName;
	}


	public Boolean getShowColumnUsername() {
		return showColumnUsername;
	}


	public void setShowColumnUsername(Boolean showColumnUsername) {
		this.showColumnUsername = showColumnUsername;
	}


	public Boolean getShowColumnName() {
		return showColumnName;
	}


	public void setShowColumnName(Boolean showColumnName) {
		this.showColumnName = showColumnName;
	}


	public Boolean getShowColumnNumberOffered() {
		return showColumnNumberOffered;
	}


	public void setShowColumnNumberOffered(Boolean showColumnNumberOffered) {
		this.showColumnNumberOffered = showColumnNumberOffered;
	}


	public Boolean getShowColumnNumberAccepted() {
		return showColumnNumberAccepted;
	}


	public void setShowColumnNumberAccepted(Boolean showColumnNumberAccepted) {
		this.showColumnNumberAccepted = showColumnNumberAccepted;
	}


	public Boolean getShowColumnNumberStarted() {
		return showColumnNumberStarted;
	}


	public void setShowColumnNumberStarted(Boolean showColumnNumberStarted) {
		this.showColumnNumberStarted = showColumnNumberStarted;
	}


	public Boolean getShowColumnNumberAcceptedAndStarted() {
		return showColumnNumberAcceptedAndStarted;
	}


	public void setShowColumnNumberAcceptedAndStarted(Boolean showColumnNumberAcceptedAndStarted) {
		this.showColumnNumberAcceptedAndStarted = showColumnNumberAcceptedAndStarted;
	}


	public Boolean getShowColumnSumIdleTime() {
		return showColumnSumIdleTime;
	}


	public void setShowColumnSumIdleTime(Boolean showColumnSumIdleTime) {
		this.showColumnSumIdleTime = showColumnSumIdleTime;
	}


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


	public Boolean getCustomSelection() {
		return customSelection;
	}


	public void setCustomSelection(Boolean customSelection) {
		this.customSelection = customSelection;
	}


	public List<String> getCustomSelectionParticipants() {
		return customSelectionParticipants;
	}


	public void setCustomSelectionParticipants(List<String> customSelectionParticipants) {
		this.customSelectionParticipants = customSelectionParticipants;
	}


	public Boolean getAutoSelection() {
		return autoSelection;
	}


	public void setAutoSelection(Boolean autoSelection) {
		this.autoSelection = autoSelection;
	}


	public String getAutoSelectionSortColumn() {
		return autoSelectionSortColumn;
	}


	public void setAutoSelectionSortColumn(String autoSelectionSortColumn) {
		this.autoSelectionSortColumn = autoSelectionSortColumn;
	}



	public Integer getAutoSelectionNumber() {
		return autoSelectionNumber;
	}


	public void setAutoSelectionNumber(Integer autoSelectionNumber) {
		this.autoSelectionNumber = autoSelectionNumber;
	}


	public String getAutoSelectionMode() {
		return autoSelectionMode;
	}


	public void setAutoSelectionMode(String autoSelectionMode) {
		this.autoSelectionMode = autoSelectionMode;
	}


	public List<String> getAutoSelectionBlacklist() {
		return autoSelectionBlacklist;
	}


	public void setAutoSelectionBlacklist(List<String> autoSelectionBlacklist) {
		this.autoSelectionBlacklist = autoSelectionBlacklist;
	}


	public Boolean getShowColumnIdleTimeAccepted() {
		return showColumnIdleTimeAccepted;
	}


	public void setShowColumnIdleTimeAccepted(Boolean showColumnIdleTimeAccepted) {
		this.showColumnIdleTimeAccepted = showColumnIdleTimeAccepted;
	}


	public Boolean getShowColumnIdleTimeStarted() {
		return showColumnIdleTimeStarted;
	}


	public void setShowColumnIdleTimeStarted(Boolean showColumnIdleTimeStarted) {
		this.showColumnIdleTimeStarted = showColumnIdleTimeStarted;
	}


}

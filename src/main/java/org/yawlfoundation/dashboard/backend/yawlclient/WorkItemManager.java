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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.yawlfoundation.dashboard.backend.yawlclient.model.Case;
import org.yawlfoundation.dashboard.backend.yawlclient.model.Participant;
import org.yawlfoundation.yawl.engine.interfce.WorkItemRecord;
import org.yawlfoundation.dashboard.backend.yawlclient.model.Specification;



/**
 * The service to manage work items.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
public interface WorkItemManager {

	List<Participant>		getAllParticipants();

	WorkItemRecord			getWorkItemById(String workItemId);

	List<WorkItemRecord>	getQueuedWorkItemsById(String participantId, int queue);

	List<WorkItemRecord>	getQueuedWorkItemsByUsername(String username, int queue);

	Set<WorkItemRecord>		getUnofferedWorkItems();

	Set<WorkItemRecord>		getOldWorkItems(LocalDateTime boundary);

	Set<WorkItemRecord>		getWorkItemsWithExpiringTimer(LocalDateTime boundary);

	int						getNumberQueuedWorkItemsById(String participantId, int queue);

	int						getNumberQueuedWorkItemsByUsername(String username, int queue);

	int						getNumberWorkItemsByCaseId(Integer caseId);

	String					acceptOffer(String participantId, String itemId);

	String					startWorkItem(String participantId, String itemId);

	String					completeWorkItem(String participantId, String itemId);


	Set<Integer>			getAllCasesWithWorkItems();

	List<Case>				getAllRunningCases();

	List<Integer>			getRunningCasesBySpec(Specification specification);

	List<Specification>		getAllLoadedSpecifications();

	String					launchCaseById(String caseId, String data);

	String					launchCaseByUri(String caseUri, String data);

}

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
package org.yawlfoundation.dashboard.backend.yawlclient.mashaller;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.yawlfoundation.yawl.engine.interfce.WorkItemRecord;


/**
 * ConsoleApplication.
 * @author Philipp Thomas <philipp.thomas@floaz.de>
 */
public abstract class WorkItemMarshaller {

    public static List<WorkItemRecord> unmarshalWorkItems(String xml) throws JDOMException, IOException {
		List<WorkItemRecord> set = new LinkedList<>();

		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(new StringReader(xml));
		Element root = document.getRootElement();
		for(Element workItemElement : root.getChildren()) {
			set.add(unmarshalWorkItem(workItemElement));
		}

		return set;
	}


    public static WorkItemRecord unmarshalWorkItem(String xml) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(new StringReader(xml));
		Element root = document.getRootElement();
		return unmarshalWorkItem(root);
	}


    public static WorkItemRecord unmarshalWorkItem(Element workItemElement) {
        if (workItemElement == null) return null;

        WorkItemRecord wir;
        String status = workItemElement.getChildText("status");
        String caseID = workItemElement.getChildText("caseid");
        String taskID = workItemElement.getChildText("taskid");
        String specURI = workItemElement.getChildText("specuri");
        String enablementTime = workItemElement.getChildText("enablementTime");
        if (caseID != null && taskID != null && specURI != null &&
            enablementTime != null && status != null) {

            wir = new WorkItemRecord(caseID, taskID, specURI, enablementTime, status);

            //wir.setExtendedAttributes(unmarshalWorkItemAttributes(workItemElement));
            wir.setUniqueID(workItemElement.getChildText("uniqueid"));
            wir.setTaskName(workItemElement.getChildText("taskname"));
            wir.setDocumentation(workItemElement.getChildText("documentation"));
            wir.setAllowsDynamicCreation(workItemElement.getChildText(
                                                              "allowsdynamiccreation"));
            wir.setRequiresManualResourcing(workItemElement.getChildText(
                                                           "requiresmanualresourcing"));
            wir.setCodelet(workItemElement.getChildText("codelet"));
            wir.setDeferredChoiceGroupID(workItemElement.getChildText(
                                                              "deferredChoiceGroupID"));
            wir.setSpecVersion(workItemElement.getChildText("specversion"));
            wir.setFiringTime(workItemElement.getChildText("firingTime"));
            wir.setStartTime(workItemElement.getChildText("startTime"));
            wir.setCompletionTimeMs(workItemElement.getChildText("completionTime"));
            wir.setEnablementTimeMs(workItemElement.getChildText("enablementTimeMs"));
            wir.setFiringTimeMs(workItemElement.getChildText("firingTimeMs"));
            wir.setStartTimeMs(workItemElement.getChildText("startTimeMs"));
            wir.setCompletionTimeMs(workItemElement.getChildText("completionTimeMs"));
            wir.setTimerTrigger(workItemElement.getChildText("timertrigger"));
            wir.setTimerExpiry(workItemElement.getChildText("timerexpiry"));
            wir.setStartedBy(workItemElement.getChildText("startedBy"));
            wir.setTag(workItemElement.getChildText("tag"));
            wir.setCustomFormURL(workItemElement.getChildText("customform"));

            String specid = workItemElement.getChildText("specidentifier") ;
            if (specid != null) wir.setSpecIdentifier(specid);

            String resStatus = workItemElement.getChildText("resourceStatus");
            if (resStatus != null) wir.setResourceStatus(resStatus);

            Element data = workItemElement.getChild("data");
            if ((null != data) && (data.getContentSize() > 0))
                   wir.setDataList((Element) data.getContent(0));

            Element updateddata = workItemElement.getChild("updateddata");
            if ((null != updateddata) && (updateddata.getContentSize() > 0))
                   wir.setUpdatedData((Element) updateddata.getContent(0));

            Element logPredicate = workItemElement.getChild("logPredicate");
            if (logPredicate != null) {
                wir.setLogPredicateStarted(logPredicate.getChildText("start"));
                wir.setLogPredicateCompletion(logPredicate.getChildText("completion"));
            }

            return wir;
        }
        throw new IllegalArgumentException("Input element could not be parsed.");
    }
}

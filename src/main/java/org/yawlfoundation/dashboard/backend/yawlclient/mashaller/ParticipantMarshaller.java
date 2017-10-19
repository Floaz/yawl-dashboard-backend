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
import org.yawlfoundation.dashboard.backend.yawlclient.model.Participant;


/**
 * ConsoleApplication.
 * @author Philipp Thomas <philipp.thomas@floaz.de>
 */
public abstract class ParticipantMarshaller {

	public static List<Participant> parseParticipants(String xml) throws IOException, JDOMException {
		List<Participant> result = new LinkedList<>();

		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(new StringReader(xml));
		Element root = document.getRootElement();
		for(Element workItemElement : root.getChildren()) {
			result.add(parseParticipant(workItemElement));
		}

		return result;
	}


	public static Participant parseParticipant(String xml) throws IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		Document document = (org.jdom2.Document) builder.build(new StringReader(xml));
		Element root = document.getRootElement();
		return parseParticipant(root);
	}


	public static Participant parseParticipant(Element element) throws IOException {
		Participant participant = new Participant();
		participant.setId(element.getAttributeValue("id"));
		participant.setUsername(element.getChildText("userid"));
		participant.setFirstname(element.getChildText("firstname"));
		participant.setLastname(element.getChildText("lastname"));
		participant.setDescription(element.getChildText("description"));
		participant.setNotes(element.getChildText("notes"));
		participant.setAdmin(Boolean.valueOf(element.getChildText("isAdministrator")));
		return participant;
	}

}

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
import org.yawlfoundation.dashboard.backend.yawlclient.model.Role;


/**
 * ConsoleApplication.
 * @author Philipp Thomas <philipp.thomas@floaz.de>
 */
public abstract class RoleMarshaller {

	public static List<Role> parseRoles(String xml) throws IOException, JDOMException {
		List<Role> result = new LinkedList<>();

		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(new StringReader(xml));
		Element root = document.getRootElement();
		for(Element item : root.getChildren()) {
			result.add(parseRole(item));
		}

		return result;
	}


	public static Role parseRole(String xml) throws IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		Document document = (org.jdom2.Document) builder.build(new StringReader(xml));
		Element root = document.getRootElement();
		return parseRole(root);
	}


	public static Role parseRole(Element element) throws IOException {
		Role role = new Role();
		role.setId(element.getAttributeValue("id"));
		role.setName(element.getChildText("name"));
		role.setDescription(element.getChildText("description"));
		role.setNotes(element.getChildText("notes"));
		return role;
	}

}

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
import org.yawlfoundation.dashboard.backend.yawlclient.model.Specification;


/**
 * ConsoleApplication.
 * @author Philipp Thomas <philipp.thomas@floaz.de>
 */
public abstract class SpecificationMarshaller {

	public static List<Specification> parseSepcifications(String xml) throws IOException, JDOMException {
		List<Specification> result = new LinkedList<>();

		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(new StringReader(xml));
		Element root = document.getRootElement();
		for(Element workItemElement : root.getChildren()) {
			result.add(parseSepcification(workItemElement));
		}

		return result;
	}


	public static Specification parseSepcification(String xml) throws IOException, JDOMException {
		SAXBuilder builder = new SAXBuilder();
		Document document = (Document) builder.build(new StringReader(xml));
		Element root = document.getRootElement();
		return parseSepcification(root);
	}


	public static Specification parseSepcification(Element element) throws IOException {
		Specification specification = new Specification();
		specification.setId(element.getChildText("id"));
		specification.setUri(element.getChildText("uri"));
		specification.setVersion(element.getChildText("version"));
		specification.setSpecversion(element.getChildText("specversion"));
		if(element.getChild("authors") != null) {
			for(Element author : element.getChild("authors").getChildren()) {
				specification.getAuthors().add(author.getTextTrim());
			}
		}
		return specification;
	}

}

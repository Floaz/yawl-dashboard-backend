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

import java.util.LinkedList;
import java.util.List;

/**
 * The dto for specification.
 * @author Philipp Thomas
 */
public class Specification {

	private String id;
	private String uri;
	private String version;
	private String specversion;
	private String documentation;
	private List<String> authors = new LinkedList<>();


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUri() {
		return uri;
	}


	public void setUri(String uri) {
		this.uri = uri;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getSpecversion() {
		return specversion;
	}


	public void setSpecversion(String specversion) {
		this.specversion = specversion;
	}


	public String getDocumentation() {
		return documentation;
	}


	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}


	public List<String> getAuthors() {
		return authors;
	}


	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}


}

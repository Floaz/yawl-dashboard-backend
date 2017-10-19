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
import java.util.Arrays;
import java.util.List;
import org.jdom2.JDOMException;
import org.yawlfoundation.yawl.resourcing.rsInterface.ResourceGatewayClient;
import org.yawlfoundation.dashboard.backend.yawlclient.mashaller.ParticipantMarshaller;
import org.yawlfoundation.dashboard.backend.yawlclient.mashaller.RoleMarshaller;
import org.yawlfoundation.dashboard.backend.yawlclient.model.Participant;
import org.yawlfoundation.dashboard.backend.yawlclient.model.Role;
import org.yawlfoundation.yawl.util.PasswordEncryptor;

/**
 * CreateUserScript.
 * @author Philipp Thomas <philipp.thomas@floaz.de>
 */
public class ResourceManagerImpl implements ResourceManager {

	private final ResourceServiceSessionPool	resourceManagerSessionPool;
	private final ResourceGatewayClient			connection;


	public ResourceManagerImpl(ResourceServiceSessionPool resourceManagerSessionPool, ResourceGatewayClient connection) {
		this.resourceManagerSessionPool = resourceManagerSessionPool;
		this.connection = connection;
	}


	@Override
	public boolean checkCredentials(String username, String password) {
		try(ResourceServiceSessionHandle handle = resourceManagerSessionPool.getHandle()) {
			String result = connection.validateUserCredentials(username, PasswordEncryptor.encrypt(password, password), false, handle.getRawHandle());
			return connection.successful(result);
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public List<String> getAllParticipantNames() {
		try(ResourceServiceSessionHandle handle = resourceManagerSessionPool.getHandle()) {
			String result = connection.getAllParticipantNames(handle.getRawHandle());

			if(result.startsWith("<failure>")) {
				return null;
			} else {
				return Arrays.asList(result.split(","));
			}
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public Participant getParticipantById(String id) {
		try(ResourceServiceSessionHandle handle = resourceManagerSessionPool.getHandle()) {
			String result = connection.getParticipant(id, handle.getRawHandle());

			if(!connection.successful(result)) {
				return null;
			} else {
				return ParticipantMarshaller.parseParticipant(result);
			}
		}
		catch(IOException | JDOMException ex) {
			throw new RuntimeException(ex);
		}
	}


	@Override
	public Participant getParticipantByName(String name) {
		try(ResourceServiceSessionHandle handle = resourceManagerSessionPool.getHandle()) {
			String result = connection.getParticipantFromUserID(name, handle.getRawHandle());

			if(!connection.successful(result)) {
				return null;
			} else {
				return ParticipantMarshaller.parseParticipant(result);
			}
		}
		catch(IOException | JDOMException ex) {
			throw new RuntimeException(ex);
		}
	}


	@Override
	public String addParticipant(String username, String password, String lastname, String firstname,
								 boolean admin, String description, String notes) {
		try(ResourceServiceSessionHandle handle = resourceManagerSessionPool.getHandle()) {
			String result = connection.addParticipant(username, password, true, lastname, firstname,
														admin, description, notes, handle.getRawHandle());

			if(result.startsWith("<failure>")) {
				return null;
			} else {
				return result;
			}
		}
		catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}


	@Override
	public void removeParticipantByName(String name) {
		try(ResourceServiceSessionHandle handle = resourceManagerSessionPool.getHandle()) {
			Participant participant = getParticipantByName(name);
			if(participant == null) {
				throw new RuntimeException("Participant does not exist!");
			}
			removeParticipantById(participant.getId());
		}
	}


	@Override
	public void removeParticipantById(String roleId) {
		try(ResourceServiceSessionHandle handle = resourceManagerSessionPool.getHandle()) {
			String result = connection.removeParticipant(roleId, handle.getRawHandle());

			if(result.startsWith("<failure>")) {
				throw new RuntimeException("Participant could not be deleted!");
			}
		}
		catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}


	@Override
	public List<String> getAllRoles() {
		try(ResourceServiceSessionHandle handle = resourceManagerSessionPool.getHandle()) {
			String result = connection.getAllRoleNames(handle.getRawHandle());

			if(result.startsWith("<failure>")) {
				return null;
			} else {
				return Arrays.asList(result.split(","));
			}
		}
		catch(IOException ex) {
			throw new RuntimeException(ex);
		}
//		try {
//			Document document = dBuilder.parse(new ByteArrayInputStream((result.getBytes(StandardCharsets.UTF_8))));
//			Element root = document.getDocumentElement();
//
//			if(root.getTagName().equals("<failure>")) {
//				throw new RuntimeException(root.getNodeValue());
//			} else {
//				return result;
//			}
//		}
//		catch(SAXException e) {
//			throw new RuntimeException(e);
//		}
	}


	@Override
	public Role getRoleById(String id) {
		try(ResourceServiceSessionHandle handle = resourceManagerSessionPool.getHandle()) {
			String result = connection.getRole(id, handle.getRawHandle());

			if(!connection.successful(result)) {
				throw new RuntimeException();
			} else {
				return RoleMarshaller.parseRole(result);
			}
		}
		catch(IOException | JDOMException ex) {
			throw new RuntimeException(ex);
		}
	}


	@Override
	public Role getRoleByName(String name) {
		try(ResourceServiceSessionHandle handle = resourceManagerSessionPool.getHandle()) {
			String result = connection.getRoleByName(name, handle.getRawHandle());

			if(!connection.successful(result)) {
				throw new RuntimeException();
			} else {
				return RoleMarshaller.parseRole(result);
			}
		}
		catch(IOException | JDOMException ex) {
			throw new RuntimeException(ex);
		}
	}


	@Override
	public String addRole(String name, String description, String notes, String parentId) {
		try(ResourceServiceSessionHandle handle = resourceManagerSessionPool.getHandle()) {
			String result = connection.addRole(name, description, notes, parentId, handle.getRawHandle());

			if(result.startsWith("<failure>")) {
				return null;
			} else {
				return result;
			}
		}
		catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}


	@Override
	public void removeRoleByName(String name) {
		Role role = getRoleByName(name);
		if(role == null) {
			throw new RuntimeException("Role does not exist!");
		}
		removeRoleById(role.getId());
	}


	@Override
	public void removeRoleById(String roleId) {
		try(ResourceServiceSessionHandle handle = resourceManagerSessionPool.getHandle()) {
			String result = connection.removeRole(roleId, handle.getRawHandle());

			if(result.startsWith("<failure>")) {
				throw new RuntimeException("Role could not be deleted!");
			}
		}
		catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}

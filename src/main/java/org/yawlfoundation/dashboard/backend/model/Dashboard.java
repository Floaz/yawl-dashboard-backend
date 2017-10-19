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
package org.yawlfoundation.dashboard.backend.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;



/**
 * The dashboard model.
 * @author Philipp Thomas
 */
@Entity
@Table(name="dashboards")
public class Dashboard {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id")
	private String id;

	@Column(name = "owner", nullable = false)
	private String owner;

	@Column(name = "order_no", nullable = false)
	private Integer orderNo;

	@Column(name = "layout_type", length = 200, nullable = false)
	private String layoutType;

	@Lob
	@Column(name = "layout_settings", length = 20971520, nullable = false)
	private byte[] layoutSettings;

	@Column(name = "dashboard_title", length = 200, nullable = false)
	private String title;

	@OneToMany(mappedBy = "dashboard", cascade = CascadeType.REMOVE)
	private List<DashletInstance> dashlets;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public Integer getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}


	public String getLayoutType() {
		return layoutType;
	}


	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}


	public byte[] getLayoutSettings() {
		return layoutSettings;
	}


	public void setLayoutSettings(byte[] layoutSettings) {
		this.layoutSettings = layoutSettings;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public List<DashletInstance> getDashlets() {
		return dashlets;
	}


	public void setDashlets(List<DashletInstance> dashlets) {
		this.dashlets = dashlets;
	}

}

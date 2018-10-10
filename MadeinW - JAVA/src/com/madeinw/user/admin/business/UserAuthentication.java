package com.madeinw.user.admin.business;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_authentication")
public class UserAuthentication implements Serializable {
	private static final long serialVersionUID = -1448668692234806775L;

	@Id
	@Column(name = "Role_ID")
	private Long roleId;

	@Id
	@Column(name = "Page_ID")
	private Long pageId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}
}

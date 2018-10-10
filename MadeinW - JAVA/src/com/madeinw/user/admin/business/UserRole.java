package com.madeinw.user.admin.business;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user_role")
public class UserRole implements Serializable {
	private static final long serialVersionUID = 3110683373038575913L;

	@Id
	@Column(name = "Role_ID")
	private Long roleId;

	@Column(name = "Role_NAME")
	private String roleName;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "role")
	private Set<AdminUsers> users = new HashSet<AdminUsers>(0);

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_authentication", joinColumns = { @JoinColumn(name = "Role_ID") }, inverseJoinColumns = { @JoinColumn(name = "Page_ID") })
	private Set<AdminPages> pages = new HashSet<AdminPages>(0);

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Set<AdminUsers> getUsers() {
		return users;
	}

	public void setUsers(Set<AdminUsers> users) {
		this.users = users;
	}

	public Set<AdminPages> getPages() {
		return pages;
	}

	public void setPages(Set<AdminPages> pages) {
		this.pages = pages;
	}
}

package com.madeinw.user.admin.business;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users_admin", catalog = "madeinw")
public class AdminUsers implements Serializable {

	private static final long serialVersionUID = -5498364958672696626L;

	private String userEmail;
	private String password;
	private String nickName;
	private Boolean isActive;
	private UserRole role;

	// Constructors
	/** default constructor */
	public AdminUsers() {
	}

	/** minimal constructor */
	public AdminUsers(String userEmail, String password) {
		this.userEmail = userEmail;
		this.password = password;
	}

	/** full constructor */
	public AdminUsers(String userEmail, String password, String nickName,
			Boolean isActive, String phone) {
		this.userEmail = userEmail;
		this.password = password;
		this.nickName = nickName;
		this.isActive = isActive;
	}

	// Property accessors
	@Id
	@Column(name = "UserEmail", unique = true, nullable = false, length = 30)
	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Column(name = "Password", nullable = false, length = 30)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "NickName", length = 30)
	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Column(name = "IsActive")
	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public AdminUsers reset() {
		this.nickName = null;
		this.password = null;
		this.userEmail = null;
		return this;

	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "Role_ID")
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

}

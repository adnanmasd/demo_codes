package com.madeinw.user.admin.business;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "admin_pages")
public class AdminPages implements Serializable, Comparable<AdminPages> {
	private static final long serialVersionUID = 3065527545930698882L;

	@Id
	@Column(name = "Page_ID")
	private Long pageId;

	@Column(name = "Page_NAME")
	private String pageName;

	@Column(name = "Page_PATTERN")
	private String pagePattern;

	@Column(name = "Page_showInMenu")
	private boolean showInMenu;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "pages")
	private Set<UserRole> role = new HashSet<UserRole>(0);

	public Long getPageId() {
		return pageId;
	}

	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPagePattern() {
		return pagePattern;
	}

	public void setPagePath(String pagePath) {
		this.pagePattern = pagePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pageId == null) ? 0 : pageId.hashCode());
		result = prime * result
				+ ((pagePattern == null) ? 0 : pagePattern.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdminPages other = (AdminPages) obj;
		if (pageId == null) {
			if (other.pageId != null)
				return false;
		} else if (!pageId.equals(other.pageId))
			return false;
		if (pagePattern == null) {
			if (other.pagePattern != null)
				return false;
		} else if (!pagePattern.equals(other.pagePattern))
			return false;
		return true;
	}

	@Override
	public int compareTo(AdminPages o) {
		if (pageId != null && o.getPageId() != null) {
			return pageId.compareTo(o.pageId) * 1;
		}
		return 0;
	}

	public boolean isShowInMenu() {
		return showInMenu;
	}

	public void setShowInMenu(boolean showInMenu) {
		this.showInMenu = showInMenu;
	}
}

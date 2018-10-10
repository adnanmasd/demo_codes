package com.madeinw.user.admin.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.madeinw.logging.LogUtil;
import com.madeinw.user.admin.business.AdminPages;

@SessionScoped
@ManagedBean
public class AdminUserNavbar implements Serializable {
	private static final long serialVersionUID = 3469071913262727660L;

	@ManagedProperty("#{adminUserBean.role.pages}")
	private Set<AdminPages> pages;

	private List<AdminPages> pagesList;
	private Map<String, ArrayList<AdminPages>> pageMap;

	@PostConstruct
	public void init() {
		LogUtil.logInfo("Generating Dynamic NavBar for Admin Interface ...");
		pagesList = new ArrayList<AdminPages>();
		pageMap = new TreeMap<String, ArrayList<AdminPages>>();
		for (AdminPages p : pages) {
			if (p.isShowInMenu() == false)
				continue;
			String[] urlArray = p.getPagePattern().split("/");
			ArrayList<AdminPages> l = new ArrayList<AdminPages>();
			if (urlArray.length < 4) {
				pagesList.add(p);
				continue;
			} else {
				String name = urlArray[2].replaceAll("(\\p{Ll})(\\p{Lu})",
						"$1 $2");
				if (pageMap.containsKey(name))
					l = pageMap.get(name);
				l.add(p);
				pageMap.put(name, l);
			}
		}
	}

	public List<AdminPages> getPagesList() {
		return pagesList;
	}

	public void setPagesList(List<AdminPages> pagesList) {
		this.pagesList = pagesList;
	}

	public Set<AdminPages> getPages() {
		return pages;
	}

	public void setPages(Set<AdminPages> pages) {
		this.pages = pages;
	}

	public Map<String, ArrayList<AdminPages>> getPageMap() {
		return pageMap;
	}

	public void setPageMap(Map<String, ArrayList<AdminPages>> pageMap) {
		this.pageMap = pageMap;
	}

}

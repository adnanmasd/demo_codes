package com.madeinw.user.admin.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.madeinw.user.admin.ui.AdminUserBean;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter({ "/admin*", "/admin/*" })
public class AdminLoginFilter implements Filter {

	private static final String MANAGED_BEAN_NAME = "adminUserBean";

	/**
	 * Default constructor.
	 */
	public AdminLoginFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String contextPath = ((HttpServletRequest) request).getContextPath();
		String pathInfo = httpRequest.getRequestURI().substring(
				httpRequest.getContextPath().length());

		if (!pathInfo.contains("login") && !pathInfo.contains("404")
				&& !pathInfo.contains("400") && !pathInfo.contains("500")
				&& !pathInfo.contains("403")) {
			HttpSession httpSession = httpRequest.getSession();
			AdminUserBean userBean = (AdminUserBean) httpSession
					.getAttribute(MANAGED_BEAN_NAME);
			if (userBean == null || userBean.getUserEmail() == null) {
				userBean = new AdminUserBean();
				userBean.setPreviousPage(httpRequest.getHeader("Referer"));
				userBean.setRestoreURL(pathInfo);
				httpSession.setAttribute(MANAGED_BEAN_NAME, userBean);
				((HttpServletResponse) response).sendRedirect(contextPath
						+ "/admin/login");
			} else {
				if (!userBean.isAuthenticatedFor(pathInfo)) {
					httpResponse.setStatus(302);
					httpResponse.sendRedirect(contextPath + "/admin/403");
				}
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

package com.madeinw.user.admin.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import com.madeinw.configuration.business.DBConfigurationBean;
import com.madeinw.configuration.persistence.ConfigurationPersistance;
import com.madeinw.contact.persistance.ProductLeadPersistance;
import com.madeinw.logging.LogUtil;
import com.madeinw.persistance.CaptchaStateLog;
import com.madeinw.persistance.CaptchaStateLogPersistance;
import com.madeinw.persistance.ProductLeadPlan;
import com.madeinw.registration.user.boundary.UserPersistance;
import com.madeinw.search.boundary.CountryPersistance;
import com.madeinw.search.boundary.ProductPersistance;
import com.madeinw.search.business.CountryStatus;
import com.madeinw.search.business.Product;
import com.madeinw.search.business.ProductLock;
import com.madeinw.user.admin.persistence.AdminDashboardPersistance;

@ManagedBean
@ViewScoped
public class AdminDashboard implements Serializable {
	private static final long serialVersionUID = 3264554700961712724L;
	public static final String IS_CAPTCHA_RENDERED_KEY = "productRegistration.isCaptchaRendered";

	@ManagedProperty("#{adminUserBean}")
	private AdminUserBean userbean;

	@ManagedProperty("#{dbConf}")
	private DBConfigurationBean dbConf;

	@Inject
	private ConfigurationPersistance dbConfPersistance;

	@Inject
	private AdminDashboardPersistance adminDashboardPersistance;

	@Inject
	private CaptchaStateLogPersistance captchaStateLogPersistance;

	@Inject
	private CountryPersistance countryPersistance;

	@Inject
	private ProductPersistance productPersistance;

	@Inject
	private UserPersistance userPersistance;

	@Inject
	private ProductLeadPersistance productLeadPersistance;

	private List<ProductLock> lockedTranslations;
	private boolean isCaptchaRendered;

	private DashboardModel model;

	private List<CountryStatus> countryActivationStatus;
	private List<CountryStatus> countryRegistrationStatus;
	private List<Product> lastTenRegisteredProducts;
	private List<Product> productsWithMostViews;
	private Map<String, Long> productsStatus;
	private Long totalRegisteredProducts;
	private Map<String, Long> usersStatus;
	private Long totalRegisteredUsers;
	private List<ProductLeadPlan> productsWithMostLeads;

	@PostConstruct
	public void init() {
		model = new DefaultDashboardModel();
		DashboardColumn column1 = new DefaultDashboardColumn();
		DashboardColumn column2 = new DefaultDashboardColumn();
		DashboardColumn column3 = new DefaultDashboardColumn();

		column1.addWidget("topFiveActivationCountries");
		column2.addWidget("topFiveRegistrationCountries");
		column3.addWidget("currentProductsStatus");

		column1.addWidget("lastTenRegisteredProducts");
		column2.addWidget("topTenMostViews");
		column3.addWidget("users");

		// column1.addWidget("topFiveUsers");
		column2.addWidget("captchaControl");
		column3.addWidget("topTenMostLeads");

		model.addColumn(column1);
		model.addColumn(column2);
		model.addColumn(column3);

		isCaptchaRendered = (dbConf.getConfigEntry(IS_CAPTCHA_RENDERED_KEY)
				.equals("1")) ? true : false;

		populateTopFiveActivationCountries();
		populateTopFiveRegistrationCountries();
		populateCurrentProductsStatus();
		populateCurrentUserStatus();
		populateLastTenRegisteredProducts();
		populateTopTenMostViews();
		populateTopTenMostLeads();
	}

	public void populateTopFiveActivationCountries() {
		countryActivationStatus = new ArrayList<CountryStatus>();
		List<CountryStatus> cs = countryPersistance
				.findTopFiveCountriesWithMostActivations();
		for (CountryStatus c : cs) {
			countryActivationStatus.add(c);
		}
	}

	public void populateTopFiveRegistrationCountries() {
		countryRegistrationStatus = new ArrayList<CountryStatus>();
		List<CountryStatus> cs = countryPersistance
				.findTopFiveCountriesWithMostRegistrations();
		for (CountryStatus c : cs) {
			countryRegistrationStatus.add(c);
		}
	}

	public void populateCurrentProductsStatus() {
		productsStatus = new LinkedHashMap<String, Long>();
		productsStatus.put("Active Products",
				productPersistance.findTotalActivatedProducts());
		productsStatus.put("Pending Products",
				productPersistance.findTotalPendingProducts());
		productsStatus.put("Rejected Products",
				productPersistance.findTotalRejectedProducts());
		totalRegisteredProducts = productPersistance
				.findTotalRegisteredProducts();
	}

	public void populateCurrentUserStatus() {
		usersStatus = new LinkedHashMap<String, Long>();
		usersStatus.put("Active Users",
				userPersistance.findTotalActivatedUsers());
		usersStatus.put("In Active Users",
				userPersistance.findTotalInActiveUsers());
		totalRegisteredUsers = userPersistance.findTotalUsers();
	}

	public void populateLastTenRegisteredProducts() {
		lastTenRegisteredProducts = new ArrayList<Product>();
		try {
			lastTenRegisteredProducts.addAll(productPersistance
					.findLastTenProducts());
		} catch (Exception e) {
			return;
		}
	}

	public void populateTopTenMostViews() {
		productsWithMostViews = new ArrayList<Product>();
		try {
			productsWithMostViews.addAll(productPersistance
					.findTopTenMostViewedProducts());
		} catch (Exception e) {
			return;
		}
	}

	public void populateTopTenMostLeads() {
		productsWithMostLeads = new ArrayList<ProductLeadPlan>();
		try {
			productsWithMostLeads.addAll(productLeadPersistance
					.findTopTenProductsWithMostLeads());
		} catch (Exception e) {
			return;
		}

	}

	public DashboardModel getModel() {
		return model;
	}

	public void toggleCaptchaStatus() {
		LogUtil.logInfo("Starting changing Captcha status ...");
		String toggledState = (isCaptchaRendered) ? "1" : "0";
		String action = (isCaptchaRendered) ? "turnedON" : "turnedOFF";

		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String userIp = request.getHeader("X-FORWARDED-FOR");
		if (userIp == null) {
			userIp = request.getRemoteAddr();
		}

		String userAgent = request.getHeader("user-agent");
		LogUtil.logInfo("Changing Captcha status now ...");
		if (dbConfPersistance.changeCaptchaStatus(toggledState)) {
			LogUtil.logInfo("Logging Captcha status change ...");
			captchaStateLogPersistance.logUserAction(new CaptchaStateLog(
					new Date(), userbean.getUserEmail(), action, userIp,
					userAgent));
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Captcha is successfully switched "

					+ (isCaptchaRendered ? "on." : "off."), null);
			FacesContext.getCurrentInstance().addMessage(null, m);
			dbConf.init();
			LogUtil.logInfo("Captcha status changed sucessfully...");
		} else {
			LogUtil.log("Capthca status not changes ... ", Level.SEVERE, null);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Some Error happend !!!", null);
			FacesContext.getCurrentInstance().addMessage(null, m);
		}
	}

	public AdminUserBean getUserbean() {
		return userbean;
	}

	public void setUserbean(AdminUserBean userbean) {
		this.userbean = userbean;
	}

	public List<ProductLock> getLockedTranslations() {
		return lockedTranslations;
	}

	public void setLockedTranslations(List<ProductLock> lockedTranslations) {
		this.lockedTranslations = lockedTranslations;
	}

	public DBConfigurationBean getDbConf() {
		return dbConf;
	}

	public void setDbConf(DBConfigurationBean dbConf) {
		this.dbConf = dbConf;
	}

	public boolean isCaptchaRendered() {
		return isCaptchaRendered;
	}

	public void setCaptchaRendered(boolean isCaptchaRendered) {
		this.isCaptchaRendered = isCaptchaRendered;
	}

	public ConfigurationPersistance getDbConfPersistance() {
		return dbConfPersistance;
	}

	public void setDbConfPersistance(ConfigurationPersistance dbConfPersistance) {
		this.dbConfPersistance = dbConfPersistance;
	}

	public AdminDashboardPersistance getAdminDashboardPersistance() {
		return adminDashboardPersistance;
	}

	public void setAdminDashboardPersistance(
			AdminDashboardPersistance adminDashboardPersistance) {
		this.adminDashboardPersistance = adminDashboardPersistance;
	}

	public CaptchaStateLogPersistance getCaptchaStateLogPersistance() {
		return captchaStateLogPersistance;
	}

	public void setCaptchaStateLogPersistance(
			CaptchaStateLogPersistance captchaStateLogPersistance) {
		this.captchaStateLogPersistance = captchaStateLogPersistance;
	}

	public List<CountryStatus> getCountryActivationStatus() {
		return countryActivationStatus;
	}

	public void setCountryActivationStatus(
			List<CountryStatus> countryActivationStatus) {
		this.countryActivationStatus = countryActivationStatus;
	}

	public List<CountryStatus> getCountryRegistrationStatus() {
		return countryRegistrationStatus;
	}

	public void setCountryRegistrationStatus(List<CountryStatus> countryStatus) {
		this.countryRegistrationStatus = countryStatus;
	}

	public List<Product> getLastTenRegisteredProducts() {
		return lastTenRegisteredProducts;
	}

	public void setLastTenRegisteredProducts(
			List<Product> lastTenRegisteredProducts) {
		this.lastTenRegisteredProducts = lastTenRegisteredProducts;
	}

	public List<Product> getProductsWithMostViews() {
		return productsWithMostViews;
	}

	public void setProductsWithMostViews(List<Product> productsWithMostViews) {
		this.productsWithMostViews = productsWithMostViews;
	}

	public List<ProductLeadPlan> getProductsWithMostLeads() {
		return productsWithMostLeads;
	}

	public void setProductsWithMostLeads(
			List<ProductLeadPlan> productsWithMostLeads) {
		this.productsWithMostLeads = productsWithMostLeads;
	}

	public CountryPersistance getCountryPersistance() {
		return countryPersistance;
	}

	public void setCountryPersistance(CountryPersistance countryPersistance) {
		this.countryPersistance = countryPersistance;
	}

	public ProductPersistance getProductPersistance() {
		return productPersistance;
	}

	public void setProductPersistance(ProductPersistance productPersistance) {
		this.productPersistance = productPersistance;
	}

	public ProductLeadPersistance getProductLeadPersistance() {
		return productLeadPersistance;
	}

	public void setProductLeadPersistance(
			ProductLeadPersistance productLeadPersistance) {
		this.productLeadPersistance = productLeadPersistance;
	}

	public Map<String, Long> getProductsStatus() {
		return productsStatus;
	}

	public void setProductsStatus(Map<String, Long> productsStatus) {
		this.productsStatus = productsStatus;
	}

	public Map<String, Long> getUsersStatus() {
		return usersStatus;
	}

	public void setUsersStatus(Map<String, Long> usersStatus) {
		this.usersStatus = usersStatus;
	}

	public Long getTotalRegisteredProducts() {
		return totalRegisteredProducts;
	}

	public void setTotalRegisteredProducts(Long totalRegisteredProducts) {
		this.totalRegisteredProducts = totalRegisteredProducts;
	}

	public Long getTotalRegisteredUsers() {
		return totalRegisteredUsers;
	}

	public void setTotalRegisteredUsers(Long totalRegisteredUsers) {
		this.totalRegisteredUsers = totalRegisteredUsers;
	}

	public UserPersistance getUserPersistance() {
		return userPersistance;
	}

	public void setUserPersistance(UserPersistance userPersistance) {
		this.userPersistance = userPersistance;
	}

}

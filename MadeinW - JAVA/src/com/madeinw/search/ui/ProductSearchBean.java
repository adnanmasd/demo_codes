package com.madeinw.search.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;

import com.madeinw.ConfBean;
import com.madeinw.LocalizedMessage;
import com.madeinw.logging.LogUtil;
import com.madeinw.persistance.Category;
import com.madeinw.search.boundary.CountryPersistance;
import com.madeinw.search.boundary.ProductPersistance;
import com.madeinw.search.business.Country;
import com.madeinw.search.business.Product;
import com.madeinw.search.business.ProductOperations;
import com.madeinw.user.admin.persistence.AddChamberOfCommercePersistance;
import com.madeinw.user.admin.persistence.ChamberOfCommerce;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;
import com.ocpsoft.pretty.faces.annotation.URLMappings;
import com.ocpsoft.pretty.faces.annotation.URLQueryParameter;

@ManagedBean(name = "productSearchBean")
@ViewScoped
@URLMappings(mappings = {
		@URLMapping(id = "productPage", pattern = "/products", viewId = "/web/xhtml/productSearchWorld.xhtml", parentId = "base"),
		@URLMapping(id = "countryPage", pattern = "/Made-In-#{productSearchBean.countryName}", viewId = "/web/xhtml/productSearchCountry.xhtml", parentId = "base") })
public class ProductSearchBean {

	@Inject
	ProductPersistance productPersistance;

	@Inject
	private AddChamberOfCommercePersistance chamberOfCommercePersistance;

	@Inject
	ProductOperations productOperations;

	private List<Product> productList;
	private List<ChamberOfCommerce> chamberList;

	@Inject
	private CountryPersistance countryPersistance;

	// @Inject
	private Country countrySearchingIn;

	private String countryId;
	private String countryName;

	@Inject
	ConfBean conf;

	@Inject
	LocalizedMessage lm;

	@URLQueryParameter(mappingId = "productPage", value = "q")
	private String productName;

	private List<String> searchWords;

	private Set<Country> listOfCountries;
	private Set<Category> listOfCategories;

	private Country countrySelected;
	private Category industrySelected;

	private Double maxUnitPrice;
	private Integer maxMOQ;

	private Boolean firstTimeLoad;

	public ProductSearchBean() {
		listOfCountries = new LinkedHashSet<Country>();
		listOfCategories = new LinkedHashSet<Category>();
		firstTimeLoad = true;
		searchWords = new LinkedList<String>();
	}

	@URLAction(mappingId = "productPage")
	public String init() {
		productName = productName == null ? (String) FacesContext
				.getCurrentInstance().getExternalContext().getFlash()
				.get("productName") : productName;
		if (productName != null && productName.length() > 0) {
			String p = lm.getLocalizedString("countrySuggestionPrefix");
			if (productName.startsWith(p)) {
				this.countryName = lm.getUrlFriendlyName(productName
						.substring(p.length()));
				return "pretty:countryPage";
			}
			loadProducts(productName);
			searchWords.addAll(Arrays.asList(productName.toLowerCase().split(
					" ")));
			return "#";
		} else {
			return "pretty:404";
		}
	}

	public List<String> completeText(String query) {
		List<String> results = new ArrayList<String>();
		List<String> countrySuggestions = findCountrySuggestion(query);
		Collections.sort(countrySuggestions);
		results.addAll(countrySuggestions);
		results.add(query);
		results.addAll(productPersistance.loadSuggestions(query));
		return results;
	}

	public List<String> findCountrySuggestion(String query) {
		List<String> results = new ArrayList<String>();
		List<Country> countryList = countryPersistance.loadCountries();
		List<String> countryNames = new ArrayList<String>();
		for (Country c : countryList) {
			countryNames.add(lm.getLocalizedString(c.getCountryCode()));
		}
		for (String s : countryNames) {
			if (s.toLowerCase().contains(query.toLowerCase()))
				results.add(lm.getLocalizedString("countrySuggestionPrefix")
						+ s);
		}
		return results;
	}

	public String search() {
		// FacesContext.getCurrentInstance().getExternalContext().getFlash().put("productName",
		// productName);
		return "/products?faces-redirect=true&amp;includeViewParams=true";
	}

	@URLAction(mappingId = "countryPage")
	public String initCountry() {
		if (firstTimeLoad) {
			if (countryName != null && !countryName.equals("")) {
				try {
					countrySearchingIn = countryPersistance
							.loadCountryByName(countryName);
				} catch (NoResultException e) {
					return "pretty:404";
				}
				productList = productOperations
						.findProductsByCountry(countrySearchingIn
								.getCountryId());
				chamberList = chamberOfCommercePersistance
						.findChamberOfCommerceByCountry(countrySearchingIn
								.getCountryCode());
				populateCategories();
				firstTimeLoad = false;
			}
		}
		return "#";
	}

	public void showProductDetails(String productId) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.setAttribute("productId", productId);
	}

	public int getProductListSize() {
		if (productList != null) {
			return productList.size();
		} else {
			return 0;
		}
	}

	public int getChamberListSize() {
		if (chamberList != null) {
			return chamberList.size();
		} else {
			return 0;
		}
	}

	public void loadProducts(String keyword) {
		LogUtil.logInfo("Searching products ...");
		productList = productOperations.findProducts(keyword, maxUnitPrice,
				maxMOQ, countrySearchingIn, industrySelected);
		populateCountries();
		populateCategories();
	}

	public void loadCountryProducts(String keyword) {
		LogUtil.logInfo("Searching Country products ...");
		searchWords.clear();
		if (productName != null)
			searchWords.addAll(Arrays.asList(productName.toLowerCase().split(
					" ")));
		productList = productOperations.findCountryProducts(keyword,
				maxUnitPrice, maxMOQ, countrySearchingIn, industrySelected);
		populateCountries();
		populateCategories();
	}

	private void populateCategories() {
		listOfCategories.clear();
		for (Product product : productList) {
			listOfCategories.add(product.getCategory());
		}
	}

	public void clearProductNameFilter() {
		this.productName = null;
		loadCountryProducts(productName);
	}

	public void clearCountryFilter() {
		this.countrySearchingIn = null;
		loadProducts(productName);
	}

	public void clearIndustryFilter() {
		this.industrySelected = null;
		loadCountryProducts(productName);
	}

	public void clearMaxUnitPriceFilter(String source) {
		this.maxUnitPrice = null;
		if (source != null && source.equals("country")) {
			loadCountryProducts(productName);
		} else {
			loadProducts(productName);
		}
	}

	public void clearMaxMOQFilter(String source) {
		this.maxMOQ = null;
		if (source != null && source.equals("country")) {
			loadCountryProducts(productName);
		} else {
			loadProducts(productName);
		}
	}

	private void populateCountries() {
		listOfCountries.clear();
		for (Product product : productList) {
			listOfCountries.add(product.getCompany().getCountry());
		}
	}

	public String generatePathForImages(ChamberOfCommerce chamber) {
		return "COC_" + chamber.getChamberCountryCode() + "_"
				+ chamber.getChamberId().toString();
	}

	public String getGroup(String keyword) {
		return (keyword.startsWith(lm
				.getLocalizedString("countrySuggestionPrefix"))) ? lm
				.getLocalizedString("searchGroupCountry") : lm
				.getLocalizedString("searchGroupWorld");
	}

	public Country getCountrySearchingIn() {
		return countrySearchingIn;
	}

	public void setCountrySearchingIn(Country countrySearchingIn) {
		this.countrySearchingIn = countrySearchingIn;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public Set<Country> getListOfCountries() {
		return listOfCountries;
	}

	public Set<Category> getListOfCategories() {
		return listOfCategories;
	}

	public Double getMaxUnitPrice() {
		return maxUnitPrice;
	}

	public void setMaxUnitPrice(Double maxUnitPrice) {
		this.maxUnitPrice = maxUnitPrice;
	}

	public Integer getMaxMOQ() {
		return maxMOQ;
	}

	public void setMaxMOQ(Integer maxMOQ) {
		this.maxMOQ = maxMOQ;
	}

	public Country getCountrySelected() {
		return countrySelected;
	}

	public void setCountrySelected(Country countrySelected) {
		this.countrySelected = countrySelected;
	}

	public Category getIndustrySelected() {
		return industrySelected;
	}

	public void setIndustrySelected(Category industrySelected) {
		this.industrySelected = industrySelected;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public List<String> getSearchWords() {
		return searchWords;
	}

	public AddChamberOfCommercePersistance getChamberOfCommercePersistance() {
		return chamberOfCommercePersistance;
	}

	public void setChamberOfCommercePersistance(
			AddChamberOfCommercePersistance chamberOfCommercePersistance) {
		this.chamberOfCommercePersistance = chamberOfCommercePersistance;
	}

	public List<ChamberOfCommerce> getChamberList() {
		return chamberList;
	}

	public void setChamberList(List<ChamberOfCommerce> chamberList) {
		this.chamberList = chamberList;
	}
}

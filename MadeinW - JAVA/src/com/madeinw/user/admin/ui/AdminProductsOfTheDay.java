package com.madeinw.user.admin.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.madeinw.logging.LogUtil;
import com.madeinw.search.boundary.ProductPersistance;
import com.madeinw.search.boundary.ProductsOfTheDayPersistance;
import com.madeinw.search.business.Country;
import com.madeinw.search.business.Product;
import com.madeinw.search.business.ProductsOfTheDay;

@ManagedBean(name = "adminPotd")
@ViewScoped
public class AdminProductsOfTheDay implements Serializable {

	private static final long serialVersionUID = 6803705521191490341L;

	private List<ProductsOfTheDay> productsOftheDayList;

	@Inject
	private ProductsOfTheDayPersistance productsOfTheDayPersistance;

	@ManagedProperty("#{continentBean.allContriesSorted}")
	private List<Country> countriesLoaded;

	private String countrySearchingIn;
	private Map<String, Object> listOfCountries;

	private Date dateToSearch;
	private Integer filledSlots;
	private Integer emptySlots;
	private String dateStr;
	private List<ProductsOfTheDay> emptyList;
	private Date dateToCopyFrom;

	private Product productToAdd;
	private String productToAddName;
	private String searchKeyword;
	private List<Product> productResultList;

	@Inject
	private ProductPersistance productPersistance;

	@PostConstruct
	public void init() {
		dateToSearch = new Date();
		dateStr = getDateStr(dateToSearch);
		listOfCountries = new LinkedHashMap<String, Object>();
		for (Country cont : countriesLoaded) {
			listOfCountries.put(cont.getURLFriendlyCountryName(),
					cont.getCountryId());
		}
		fetchProductsOftheDay();
	}

	public void fetchProductsOftheDay() {
		LogUtil.logInfo("Fetching product of the day for admin to view ...");
		productsOftheDayList = new ArrayList<ProductsOfTheDay>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateStr = dateFormat.format(dateToSearch);
		List<ProductsOfTheDay> pList = productsOfTheDayPersistance
				.findAllProductsOfTheDay(dateStr);
		filledSlots = pList.size();
		emptySlots = 3 - pList.size();
		if (!pList.isEmpty()) {
			productsOftheDayList.addAll(pList);
		}
		emptyList = new ArrayList<ProductsOfTheDay>(emptySlots);
		for (int i = emptySlots; i > 0; i--) {
			emptyList.add(new ProductsOfTheDay());
		}

	}

	public void copyProductsOfTheDay() {
		LogUtil.logInfo("Start Copying Products of the day ...");
		if (dateToCopyFrom != null) {
			String dateString = new SimpleDateFormat("yyyy-MM-dd")
					.format(dateToCopyFrom);
			List<ProductsOfTheDay> list = productsOfTheDayPersistance
					.findAllProductsOfTheDay(dateString);
			if (list == null || list.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(
						"",
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"No products to copy from selected date "
										+ dateString, ""));
				return;
			}
			List<ProductsOfTheDay> newList = new ArrayList<ProductsOfTheDay>();
			for (ProductsOfTheDay p : list) {
				newList.add(new ProductsOfTheDay(p.getProduct(), dateStr, p
						.getProductName()));
			}
			LogUtil.logInfo("Copying all products from " + dateString + " ...");
			productsOfTheDayPersistance.saveAllProducts(newList);
			LogUtil.logInfo("Copied Successfull");
			fetchProductsOftheDay();
		}
	}

	public void addProductOfTheDay() {
		LogUtil.logInfo("Starting Adding Product Of the Day ...");
		if (productToAdd == null) {
			FacesContext.getCurrentInstance().addMessage(
					"",
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Please Select A Product to Add", ""));
		} else {
			ProductsOfTheDay newProductOfTheDay = new ProductsOfTheDay(
					productToAdd, dateStr, productToAddName);
			productsOfTheDayPersistance.addProductOfTheDay(newProductOfTheDay);
			FacesContext.getCurrentInstance().addMessage(
					"",
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Product Of the Day Added Successfully.", ""));
			LogUtil.logInfo("Product of the day added for " + dateStr);
			fetchProductsOftheDay();
		}
	}

	public void deleteAll() {
		LogUtil.logInfo("Starting deleting all product of the day ...");
		if (productsOftheDayList != null && !productsOftheDayList.isEmpty()) {
			LogUtil.logInfo("Removing all products from " + dateStr + " ...");
			productsOfTheDayPersistance
					.removeAll(new ArrayList<ProductsOfTheDay>(
							productsOftheDayList));
			LogUtil.logInfo("All product Of the Day Removed Successfull ...");
			fetchProductsOftheDay();
		}
	}

	public void deleteProductOfTheDay(ProductsOfTheDay toDelete) {
		LogUtil.logInfo("Starting Delete product of the day ...");
		if (toDelete != null) {
			LogUtil.logInfo("Removing productID= "
					+ toDelete.getProduct().getProductId() + "  from "
					+ dateStr + " ...");
			productsOfTheDayPersistance.removeProduct(toDelete);
			LogUtil.logInfo("Product of the day Removed Successfull ...");
			fetchProductsOftheDay();
		}
	}

	public void findProducts() {
		LogUtil.logInfo("finding Product to add as Product of the day ...");
		productResultList = new ArrayList<Product>();
		if (countrySearchingIn != null
				&& (searchKeyword != null && !searchKeyword.equals(""))) {
			// search by keyword in country
			productResultList = productPersistance.findProductsForAdminPotd(
					countrySearchingIn, searchKeyword);
		} else {
			productResultList = productPersistance
					.findProductsByCountry(Integer.valueOf(countrySearchingIn));
		}
	}

	public String getDateStr(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	public List<ProductsOfTheDay> getProductsOftheDayList() {
		return productsOftheDayList;
	}

	public void setProductsOftheDayList(
			List<ProductsOfTheDay> productsOftheDayList) {
		this.productsOftheDayList = productsOftheDayList;
	}

	public ProductsOfTheDayPersistance getProductsOfTheDayPersistance() {
		return productsOfTheDayPersistance;
	}

	public void setProductsOfTheDayPersistance(
			ProductsOfTheDayPersistance productsOfTheDayPersistance) {
		this.productsOfTheDayPersistance = productsOfTheDayPersistance;
	}

	public Date getDateToSearch() {
		return dateToSearch;
	}

	public void setDateToSearch(Date dateToSearch) {
		this.dateToSearch = dateToSearch;
	}

	public Integer getFilledSlots() {
		return filledSlots;
	}

	public void setFilledSlots(Integer filledSlots) {
		this.filledSlots = filledSlots;
	}

	public Integer getEmptySlots() {
		return emptySlots;
	}

	public void setEmptySlots(Integer emptySlots) {
		this.emptySlots = emptySlots;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public List<ProductsOfTheDay> getEmptyList() {
		return emptyList;
	}

	public void setEmptyList(List<ProductsOfTheDay> emptyList) {
		this.emptyList = emptyList;
	}

	public Date getDateToCopyFrom() {
		return dateToCopyFrom;
	}

	public void setDateToCopyFrom(Date dateToCopyFrom) {
		this.dateToCopyFrom = dateToCopyFrom;
	}

	public Product getProductToAdd() {
		return productToAdd;
	}

	public void setProductToAdd(Product productToAdd) {
		this.productToAdd = productToAdd;
	}

	public String getProductToAddName() {
		return productToAddName;
	}

	public void setProductToAddName(String productToAddName) {
		this.productToAddName = productToAddName;
	}

	public List<Country> getCountriesLoaded() {
		return countriesLoaded;
	}

	public void setCountriesLoaded(List<Country> countriesLoaded) {
		this.countriesLoaded = countriesLoaded;
	}

	public String getCountrySearchingIn() {
		return countrySearchingIn;
	}

	public void setCountrySearchingIn(String countrySearchingIn) {
		this.countrySearchingIn = countrySearchingIn;
	}

	public Map<String, Object> getListOfCountries() {
		return listOfCountries;
	}

	public void setListOfCountries(Map<String, Object> listOfCountries) {
		this.listOfCountries = listOfCountries;
	}

	public List<Product> getProductResultList() {
		return productResultList;
	}

	public void setProductResultList(List<Product> productResultList) {
		this.productResultList = productResultList;
	}

	public ProductPersistance getProductPersistance() {
		return productPersistance;
	}

	public void setProductPersistance(ProductPersistance productPersistance) {
		this.productPersistance = productPersistance;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

}

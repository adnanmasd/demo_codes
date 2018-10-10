package com.madeinw.search.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import com.madeinw.logging.LogUtil;
import com.madeinw.search.boundary.ProductsOfTheDayPersistance;
import com.madeinw.search.business.ProductsOfTheDay;
import com.madeinw.user.admin.persistence.AddChamberOfCommercePersistance;
import com.madeinw.user.admin.persistence.ChamberOfCommerce;

@ManagedBean
@RequestScoped
public class HomePage implements Serializable {
	private static final long serialVersionUID = 3094781669867236180L;

	private List<ProductsOfTheDay> productsOfTheDay;
	private List<ChamberOfCommerce> chamberList;

	@Inject
	private ProductsOfTheDayPersistance productOfTheDayPersistance;

	@Inject
	private AddChamberOfCommercePersistance chamberOfCommercePersistance;

	@PostConstruct
	public void init() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = dateFormat.format(new Date());

		productsOfTheDay = productOfTheDayPersistance
				.findAllProductsOfTheDay(dateStr);
		chamberList = chamberOfCommercePersistance
				.findLastFiveChamberOfCommerce();
		if (!productsOfTheDay.isEmpty()) {
			LogUtil.logInfo("Products of the day found for " + dateStr
					+ "......");
			return;
		} else {
			LogUtil.log("Products of the day ---NOT found---- for" + dateStr
					+ "......", Level.SEVERE, null);
			return;
		}
	}

	public int getChamberListSize() {
		if (chamberList != null) {
			return chamberList.size();
		} else {
			return 0;
		}
	}

	public String generatePathForImages(ChamberOfCommerce chamber) {
		return "COC_" + chamber.getChamberCountryCode() + "_"
				+ chamber.getChamberId().toString();
	}

	public int getProductListSize() {
		return productsOfTheDay.size();
	}

	public ProductsOfTheDayPersistance getProductOfTheDayPersistance() {
		return productOfTheDayPersistance;
	}

	public void setProductOfTheDayPersistance(
			ProductsOfTheDayPersistance productOfTheDayPersistance) {
		this.productOfTheDayPersistance = productOfTheDayPersistance;
	}

	public List<ProductsOfTheDay> getProductsOfTheDay() {
		return productsOfTheDay;
	}

	public void setProductsOfTheDay(List<ProductsOfTheDay> productsOfTheDay) {
		this.productsOfTheDay = productsOfTheDay;
	}

	public List<ChamberOfCommerce> getChamberList() {
		return chamberList;
	}

	public void setChamberList(List<ChamberOfCommerce> chamberList) {
		this.chamberList = chamberList;
	}

	public AddChamberOfCommercePersistance getChamberOfCommercePersistance() {
		return chamberOfCommercePersistance;
	}

	public void setChamberOfCommercePersistance(
			AddChamberOfCommercePersistance chamberOfCommercePersistance) {
		this.chamberOfCommercePersistance = chamberOfCommercePersistance;
	}

}

package com.madeinw;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TimerService;
import javax.inject.Inject;

import com.madeinw.logging.LogUtil;
import com.madeinw.search.boundary.ProductsOfTheDayPersistance;
import com.madeinw.search.business.ProductsOfTheDay;

@Singleton
public class AutoCopyProductsOfTheDay {

	@Resource
	TimerService timeService;

	@Inject
	ProductsOfTheDayPersistance productsOftheDayPersistance;

	@Schedule(hour = "0", minute = "0")
	public void autoCopyProductsOfTheDayTrigger() {
		Date today = new Date(System.currentTimeMillis());
		Date yesterday = new Date(System.currentTimeMillis()
				- (24 * 60 * 60 * 1000));
		String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(today);
		String yesterdayStr = new SimpleDateFormat("yyyy-MM-dd")
				.format(yesterday);
		LogUtil.logInfo("Good Night ... New day is started ... Searching for Products of the day in new Date "
				+ todayStr);
		List<ProductsOfTheDay> productsToday = productsOftheDayPersistance
				.findAllProductsOfTheDay(todayStr);
		if (productsToday == null || productsToday.isEmpty()) {
			LogUtil.logInfo("No Products of the day found for today....");
			LogUtil.logInfo("Finding Products of the Day from yesterday...");
			List<ProductsOfTheDay> productsYesterday = productsOftheDayPersistance
					.findAllProductsOfTheDay(yesterdayStr);
			if (productsYesterday != null && !productsYesterday.isEmpty()) {
				LogUtil.logInfo("Found Products from yesterday.... Now trying copying from yesterday...");
				for (ProductsOfTheDay yP : productsYesterday) {
					productsToday.add(new ProductsOfTheDay(yP.getProduct(),
							todayStr, yP.getProductName()));
				}
				LogUtil.logInfo("Now Adding Products of the Day for today...");
				productsOftheDayPersistance.saveAllProducts(productsToday);
				LogUtil.logInfo("Products of the day for today is added successfully... YAWN");
			} else {
				LogUtil.log(
						"Cannot find products of the day from yesterday ...",
						Level.SEVERE, null);
			}
		} else {
			LogUtil.logInfo("Found products of the day for today... No need to copy from yesterday... :)");
		}
	}

	public TimerService getTimeService() {
		return timeService;
	}

	public void setTimeService(TimerService timeService) {
		this.timeService = timeService;
	}

	public ProductsOfTheDayPersistance getProductsOftheDayPersistance() {
		return productsOftheDayPersistance;
	}

	public void setProductsOftheDayPersistance(
			ProductsOfTheDayPersistance productsOftheDayPersistance) {
		this.productsOftheDayPersistance = productsOftheDayPersistance;
	}
}

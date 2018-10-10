package com.madeinw.search.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.madeinw.logging.LogUtil;
import com.madeinw.search.business.ProductsOfTheDay;

@Transactional
public class ProductsOfTheDayPersistance {

	@PersistenceContext
	EntityManager entityManager;

	public static final String DATE_TODAY = "day";
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public List<ProductsOfTheDay> findAllProductsOfTheDay(String date) {

		LogUtil.logInfo("finding products of the day for: " + date + " .....");
		List<ProductsOfTheDay> result = new ArrayList<ProductsOfTheDay>();
		try {
			final String queryString = "select model from ProductsOfTheDay model where model."
					+ DATE_TODAY + "= :day";
			EntityGraph<ProductsOfTheDay> graph = this.entityManager
					.createEntityGraph(ProductsOfTheDay.class);
			TypedQuery<ProductsOfTheDay> query = entityManager.createQuery(
					queryString, ProductsOfTheDay.class).setHint(
					"javax.persistence.fetchgraph", graph);
			query.setParameter("day", date);
			result = query.getResultList();
			return result;
		} catch (Exception e) {
			LogUtil.log("finding products of the day error .....",
					Level.SEVERE, e);
		}
		return null;

	}

	public void addProductOfTheDay(ProductsOfTheDay productOfTheDay) {
		try {
			LogUtil.logInfo("Adding Product of the Day ...");
			entityManager.persist(productOfTheDay);
		} catch (Exception e) {
			LogUtil.log("Adding Product Of the Day Failed...", Level.SEVERE, e);
		}
	}

	public void saveAllProducts(List<ProductsOfTheDay> list) {
		try {
			LogUtil.logInfo("Adding Product Of the Day by List ...");
			for (ProductsOfTheDay p : list) {
				entityManager.persist(p);
			}
		} catch (Exception e) {
			LogUtil.log("Adding Product Od the Day By list Failed...",
					Level.SEVERE, e);
		}
	}

	public void removeProduct(ProductsOfTheDay p) {
		try {
			LogUtil.logInfo("Removing product Of the Day ...");
			ProductsOfTheDay instance = entityManager.merge(p);
			entityManager.remove(instance);
		} catch (Exception e) {
			LogUtil.log("Removing product of the day Failed...", Level.SEVERE,
					e);
		}
	}

	public void removeAll(List<ProductsOfTheDay> toRemove) {
		try {
			LogUtil.logInfo("Removing all product Of the Day ...");
			for (ProductsOfTheDay p : toRemove) {
				ProductsOfTheDay instance = entityManager.merge(p);
				entityManager.remove(instance);
			}
		} catch (Exception e) {
			LogUtil.log("Removing all product Of the Day Failed...",
					Level.SEVERE, e);
		}
	}
}

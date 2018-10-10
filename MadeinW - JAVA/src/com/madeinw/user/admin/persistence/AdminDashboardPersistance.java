package com.madeinw.user.admin.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.madeinw.search.business.ProductLock;

@Transactional
public class AdminDashboardPersistance implements Serializable {
	private static final long serialVersionUID = -4172956257885279181L;
	@PersistenceContext
	EntityManager entityManager;

	public List<ProductLock> getTranslation(String user) {
		
		final String queryString = "select model from ProductLock model where model.user =:id ";
		EntityGraph<ProductLock> graph = this.entityManager
				.createEntityGraph(ProductLock.class);

		TypedQuery<ProductLock> query = entityManager.createQuery(queryString,
				ProductLock.class).setHint("javax.persistence.fetchgraph",
				graph);
		query.setParameter("id", user);

		List<ProductLock> result = query.getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}
}

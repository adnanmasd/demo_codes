package com.madeinw.user.admin.persistence;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.madeinw.activation.user.business.UserActivation;
import com.madeinw.logging.LogUtil;
import com.madeinw.registration.user.business.Users;
import com.madeinw.user.admin.business.AdminUsers;

@Transactional
public class AdminUserPersistence {

	public static final String USER_EMAIL = "userEmail";
	public static final String PASSWORD = "password";
	public static final String NICK_NAME = "nickName";

	@PersistenceContext
	private EntityManager entityManager;

	public AdminUsers login(String userEmail, String password) {
		AdminUsers userEntity = null;
		userEntity = executeQyeryLogin(userEmail, password);
		return userEntity;
	}

	public AdminUsers executeQyeryLogin(String userEmail, String password) {
		LogUtil.logInfo("finding User instance with property: " + USER_EMAIL
				+ ", value: " + userEmail);
		try {
			final String queryString = "select model from AdminUsers model where model."
					+ USER_EMAIL
					+ "= :userEmail and "
					+ PASSWORD
					+ "= :password" + " and isActive=true";
			EntityGraph<AdminUsers> userGraph = entityManager
					.createEntityGraph(AdminUsers.class);

			TypedQuery<AdminUsers> userQuery = entityManager.createQuery(
					queryString, AdminUsers.class).setHint(
					"javax.persistence.fetchgraph", userGraph);
			userQuery.setParameter(USER_EMAIL, userEmail);
			userQuery.setParameter(PASSWORD, password);
			return userQuery.getSingleResult();
		} catch (RuntimeException re) {
			LogUtil.logWarning("find by admin user failed");
			return null;
		}
	}

	public boolean verifyUser(String userEmail) {
		LogUtil.logInfo(userEmail + "= Verifying User ...");
		AdminUsers instance = entityManager.find(AdminUsers.class, userEmail);
		if (instance == null) {
			LogUtil.logInfo(userEmail + "= User found ...");
			return true;
		}
		return false;
	}

	public void register(AdminUsers user) {
		user.setIsActive(false);
		try {
			entityManager.persist(user);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	private UserActivation getAllPendingActivations(String hashKey) {
		final String queryString = "select model from UserActivation model where model.activationHash= :propertyValue";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("propertyValue", hashKey);
		return (UserActivation) query.getSingleResult();
	}

	public void checkPendingActivation(String hashKey) {
		UserActivation users = getAllPendingActivations(hashKey);
		if (users == null) {
			throw new RuntimeException();
		}
	}

	public String deleteKey(String hashKey) {
		UserActivation user = getAllPendingActivations(hashKey);
		entityManager.remove(user);
		return user.getUserEmail();
	}

	public void activate(String user) {
		final String queryString = "update Users model set model.isActive = true where model.userEmail= :propertyValue";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("propertyValue", user);

		int result = query.executeUpdate();
		if (result < 0) {
			throw new RuntimeException();
		}
	}

	public void deleteUser(String user) {
		try {
			UserActivation userActivation = entityManager.find(
					UserActivation.class, user);
			entityManager.remove(userActivation);

			Users userEntity = entityManager.getReference(Users.class, user);
			entityManager.remove(userEntity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean isExistUser(String username) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<AdminUsers> cq = cb.createQuery(AdminUsers.class);

		Root<AdminUsers> e = cq.from(AdminUsers.class);
		cq.select(e).where(cb.equal(e.get("userEmail"), username),
				cb.equal(e.get("isActive"), true));

		Query query = entityManager.createQuery(cq);
		List<AdminUsers> result = query.getResultList();

		if (result.size() > 0) {
			return true;
		}
		return false;
	}

	public AdminUsers findById(String id) {
		LogUtil.logInfo("finding Admin Users instance with id: " + id);
		try {
			AdminUsers instance = entityManager.find(AdminUsers.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.logWarning("find admin user failed");
			throw re;
		}
	}

	public boolean updatePassword(String username, String password) {
		try {
			AdminUsers instance = entityManager
					.find(AdminUsers.class, username);
			instance.setPassword(password);
			entityManager.merge(instance);
			return true;
		} catch (Exception E) {
			return false;
		}
	}

}

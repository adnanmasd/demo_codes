package com.madeinw.deactivation.user.boundary;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

import com.madeinw.logging.LogUtil;
import com.madeinw.registration.user.boundary.UserPersistance;
import com.madeinw.registration.user.business.Users;

@Singleton
public class UserDeactivationTimer {

	@Resource
	private TimerService timerService;

	@Inject
	UserPersistance userPersistance;

	public void createTimer(long intervalTimeout, String user) {
		LogUtil.logInfo("ProgrammaticalTimerEJB initialized");
		timerService.createSingleActionTimer(intervalTimeout, new TimerConfig(
				user, false));
	}

	@Timeout
	public void timedOut(final Timer timer) {
		LogUtil.logInfo("User :" + timer.getInfo() + "going to be deleted");
		if (null != timer && null != timer.getInfo()) {
			String userEmail = (String) timer.getInfo();
			if (null != userEmail && !userEmail.equals("")) {
				Users userEntity = userPersistance.findById(userEmail);
				if (userEntity != null && userEntity.getIsActive() == false) {
					userPersistance.deleteUser(userEmail);
				}
			}
		}
	}
}

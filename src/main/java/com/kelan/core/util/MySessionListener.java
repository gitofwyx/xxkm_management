package com.kelan.core.util;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;

public class MySessionListener implements HttpSessionListener {

	private Logger logger = Logger.getLogger(MySessionListener.class);

	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		System.out.println("添加Session");
		MySessionContext.AddSession(httpSessionEvent.getSession());
	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		HttpSession session = httpSessionEvent.getSession();
		try {
			logger.info("session注销完成");
			MySessionContext.DelSession(session);
		} catch (Exception e) {
			logger.warn("session注销异常");
			MySessionContext.DelSession(session);
		}
		return;
	}

}

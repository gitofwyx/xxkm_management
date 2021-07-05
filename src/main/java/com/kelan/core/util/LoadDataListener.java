package com.kelan.core.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Application Lifecycle Listener implementation class InitListener
 *
 */
@WebListener
public class LoadDataListener implements ServletContextListener {

	private static ApplicationContext applicationContext = null;

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent sce) {
		// System.out.println("ServletContext对象创建");
		// 初始化 ApplicationContext 对象
		applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		// sce.getServletContext().setAttribute("increaseCountMap",newConcurrentHashMap<Integer,AtomicInteger>());
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// System.out.println("ServletContext对象销毁");
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		// 获取业务层service Bean
		// CourseServiceImpl courserService = (CourseServiceImpl) webApplicationContext.getBean("CourseServiceImpl");
		// courserService.updateRateUtilization();// 更新点击量
		sce.getServletContext().removeAttribute("increaseCountMap");// 移除全局变量--点击量
	}

	// 用于那些非控制层中使用直接获取到的spring Bean的获取，如接口
	public static ApplicationContext getApplicatonContext() {
		return applicationContext;
	}

}

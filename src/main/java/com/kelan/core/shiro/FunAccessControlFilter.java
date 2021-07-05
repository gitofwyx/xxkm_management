package com.kelan.core.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.AccessControlFilter;

public class FunAccessControlFilter extends AccessControlFilter{

	@Override
	protected boolean isAccessAllowed(ServletRequest servletrequest, ServletResponse servletresponse, Object obj)
			throws Exception {
		// TODO Auto-generated method stub
		HttpServletRequest httpservletrequest=(HttpServletRequest)servletrequest;
		System.out.println("isAccessAllowed");
		httpservletrequest.getSession().setAttribute("error", "验证失败");
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest servletrequest, ServletResponse servletresponse) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("onAccessDenied");
		return false;
	}
	
}

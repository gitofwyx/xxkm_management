package com.kelan.core.shiro;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.kelan.core.util.MySessionContext;

public class CustomRolesAuthorizationFilter extends RolesAuthorizationFilter {

	private static Logger log = Logger.getLogger(CustomRolesAuthorizationFilter.class);

	private String unauthorizedUrl = "/unAuthorized";
	private String loginUrl = "/loginDenied";

	public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws IOException {
		HttpServletRequest httprequest = (HttpServletRequest) request;
		log.info("授权->请求路径：" + httprequest.getRequestURI());
		Subject subject = getSubject(request, response);
		String rolesArray[] = (String[]) (String[]) mappedValue;
		Set roles = CollectionUtils.asSet(rolesArray);
		boolean hasAllRoles = false;
		try {
			if (rolesArray == null || rolesArray.length == 0) {
				return true;
			} else {
				String roleBys = null;
				if (httprequest.getHeader("Set-Cookie") != null || "".equals(httprequest.getHeader("Set-Cookie"))) {
					log.warn("即将进行特定请求路径的二次验证与授权");
					System.out.println(httprequest.getHeader("Set-Cookie"));
					String[] JSESSIONID = httprequest.getHeader("Set-Cookie").split("=");
					String userId = (String) MySessionContext.getSession(JSESSIONID[1]).getAttribute("userId");
					roleBys = (String) MySessionContext.getSession(JSESSIONID[1]).getAttribute("role");
					if (userId != null || !"".equals(userId)) {
						log.warn("登陆验证已通过");
					} else {
						return hasAllRoles;
					}
				}
				System.out.println(roles);
				for (Object role : roles) {
					if (roleBys != null && !"".equals(roleBys)) {
						hasAllRoles=roleBys.equals(role);
						continue;
					}
					hasAllRoles = subject.hasRole((String) role);
				}
				if (hasAllRoles == false) {
					httprequest.setAttribute("msg", "没有下限");
					log.info("没有请求权限");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			log.error("验证出现异常！");
			log.warn("验证未通过");
		}
		return hasAllRoles;
	}

	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		log.warn("授权未通过>>>");
		Subject subject = getSubject(request, response);
		if (subject.getPrincipal() == null) {// 表示没有登录，重定向到登录页面
			log.warn(">>>用户未登录");
			saveRequest(request);
			WebUtils.issueRedirect(request, response, loginUrl);
		} else {
			if (StringUtils.hasText(unauthorizedUrl)) {// 如果有未授权页面跳转过去
				WebUtils.issueRedirect(request, response, unauthorizedUrl);
			} else {// 否则返回401未授权状态码
				WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		return false;
	}
}

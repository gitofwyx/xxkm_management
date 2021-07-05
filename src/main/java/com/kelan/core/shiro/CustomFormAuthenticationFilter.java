package com.kelan.core.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.kelan.core.util.MySessionContext;

public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

	private static Logger log = Logger.getLogger(CustomFormAuthenticationFilter.class);

	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httprequest = (HttpServletRequest) request;
		log.warn("用户没登录->请求路径：" + httprequest.getRequestURI());
		if (httprequest.getHeader("Set-Cookie") != null || "".equals(httprequest.getHeader("Set-Cookie"))) {
			log.warn("进行特定请求的路径二次验证");
			System.out.println(httprequest.getHeader("Set-Cookie"));
			String JSESSIONID[] = httprequest.getHeader("Set-Cookie").split("=");
			try {
				String userId = (String) MySessionContext.getSession(JSESSIONID[1]).getAttribute("userId");
				if(userId!=null||!"".equals(userId)){
					log.warn("验证已通过");
					return true;
				}
			} catch (Exception e) {
				System.out.println(e);
				log.error("获取session异常");
				log.warn("验证未通过");
			}
		}
		httprequest.setAttribute("msg", "用户没登录");
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				if (log.isTraceEnabled())
					log.trace("Login submission detected.  Attempting to execute login.");
				return executeLogin(request, response);
			}
			if (log.isTraceEnabled())
				log.trace("Login page view.");
			return true;
		}
		if (log.isTraceEnabled())
			log.trace((new StringBuilder())
					.append("Attempting to access a path which requires authentication.  Forwarding to the Authentication url [")
					.append(getLoginUrl()).append("]").toString());
		saveRequestAndRedirectToLogin(request, response);
		return false;
	}
	
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response)
	        throws Exception
	    {
			log.info("test");
	        issueSuccessRedirect(request, response);
	        return false;
	    }
	
}

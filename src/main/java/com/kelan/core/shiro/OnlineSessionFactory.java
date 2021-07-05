package com.kelan.core.shiro;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;

import com.kelan.core.util.IpUtil;

public class OnlineSessionFactory implements SessionFactory {  
	
	private Logger logger = Logger.getLogger(OnlineSessionFactory.class);
	  
    @Override  
    public Session createSession(SessionContext initData) {  
        OnlineSession session = new OnlineSession();  
        if (initData != null && initData instanceof WebSessionContext) {  
            WebSessionContext sessionContext = (WebSessionContext) initData;  
            HttpServletRequest request = (HttpServletRequest) sessionContext.getServletRequest();  
           if (request != null) {  
                session.setHost(IpUtil.getIpAddr(request));  
                session.setUserAgent(request.getHeader("User-Agent"));  
                session.setSystemHost(request.getLocalAddr() + ":" + request.getLocalPort());  
            }
        } 
        logger.info("createSession");
        return session;  
    }  
}   

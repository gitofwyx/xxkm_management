package com.kelan.core.shiro;

import java.io.Serializable;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.session.*;
import org.apache.shiro.session.mgt.*;
import org.apache.shiro.web.servlet.*;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.apache.shiro.web.util.WebUtils;

// Referenced classes of package org.apache.shiro.web.session.mgt:
//            WebSessionKey, WebSessionManager

public class DefaultWebSessionManager extends DefaultSessionManager
    implements WebSessionManager
{

    public DefaultWebSessionManager()
    {
        Cookie cookie = new SimpleCookie("JSESSIONID");
        cookie.setHttpOnly(true);
        sessionIdCookie = cookie;
        sessionIdCookieEnabled = true;
    }

    public Cookie getSessionIdCookie()
    {
        return sessionIdCookie;
    }

    public void setSessionIdCookie(Cookie sessionIdCookie)
    {
        this.sessionIdCookie = sessionIdCookie;
    }

    public boolean isSessionIdCookieEnabled()
    {
        return sessionIdCookieEnabled;
    }

    public void setSessionIdCookieEnabled(boolean sessionIdCookieEnabled)
    {
        this.sessionIdCookieEnabled = sessionIdCookieEnabled;
    }

    private void storeSessionId(Serializable currentId, HttpServletRequest request, HttpServletResponse response)
    {
        if(currentId == null)
        {
            String msg = "sessionId cannot be null when persisting for subsequent requests.";
            throw new IllegalArgumentException(msg);
        } else
        {
            Cookie template = getSessionIdCookie();
            Cookie cookie = new SimpleCookie(template);
            String idString = currentId.toString();
            cookie.setValue(idString);
            cookie.saveTo(request, response);
            log.trace("Set session ID cookie for session with id {}");
            return;
        }
    }

    private void removeSessionIdCookie(HttpServletRequest request, HttpServletResponse response)
    {
        getSessionIdCookie().removeFrom(request, response);
    }

    private String getSessionIdCookieValue(ServletRequest request, ServletResponse response)
    {
        if(!isSessionIdCookieEnabled())
        {
            log.debug("Session ID cookie is disabled - session id will not be acquired from a request cookie.");
            return null;
        }
        if(!(request instanceof HttpServletRequest))
        {
            log.debug("Current request is not an HttpServletRequest - cannot get session ID cookie.  Returning null.");
            return null;
        } else
        {
            HttpServletRequest httpRequest = (HttpServletRequest)request;
            return getSessionIdCookie().readValue(httpRequest, WebUtils.toHttp(response));
        }
    }

    private Serializable getReferencedSessionId(ServletRequest request, ServletResponse response)
    {
        String id = getSessionIdCookieValue(request, response);
        if(id != null)
        {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "cookie");
        } else
        {
            id = getUriPathSegmentParamValue(request, "JSESSIONID");
            if(id == null)
            {
                String name = getSessionIdName();
                id = request.getParameter(name);
                if(id == null)
                    id = request.getParameter(name.toLowerCase());
            }
            if(id != null)
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "url");
        }
        if(id != null)
        {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        }
        return id;
    }

    private String getUriPathSegmentParamValue(ServletRequest servletRequest, String paramName)
    {
        if(!(servletRequest instanceof HttpServletRequest))
            return null;
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String uri = request.getRequestURI();
        if(uri == null)
            return null;
        int queryStartIndex = uri.indexOf('?');
        if(queryStartIndex >= 0)
            uri = uri.substring(0, queryStartIndex);
        int index = uri.indexOf(';');
        if(index < 0)
            return null;
        String TOKEN = (new StringBuilder()).append(paramName).append("=").toString();
        uri = uri.substring(index + 1);
        index = uri.lastIndexOf(TOKEN);
        if(index < 0)
            return null;
        uri = uri.substring(index + TOKEN.length());
        index = uri.indexOf(';');
        if(index >= 0)
            uri = uri.substring(0, index);
        return uri;
    }

    private String getSessionIdName()
    {
        String name = sessionIdCookie == null ? null : sessionIdCookie.getName();
        if(name == null)
            name = "JSESSIONID";
        return name;
    }

    protected Session createExposedSession(Session session, SessionContext context)
    {
        if(!WebUtils.isWeb(context))
        {
            return super.createExposedSession(session, context);
        } else
        {
            ServletRequest request = WebUtils.getRequest(context);
            ServletResponse response = WebUtils.getResponse(context);
            SessionKey key = new WebSessionKey(session.getId(), request, response);
            return new DelegatingSession(this, key);
        }
    }

    protected Session createExposedSession(Session session, SessionKey key)
    {
        if(!WebUtils.isWeb(key))
        {
            return super.createExposedSession(session, key);
        } else
        {
            ServletRequest request = WebUtils.getRequest(key);
            ServletResponse response = WebUtils.getResponse(key);
            SessionKey sessionKey = new WebSessionKey(session.getId(), request, response);
            return new DelegatingSession(this, sessionKey);
        }
    }

    protected void onStart(Session session, SessionContext context)
    {
        super.onStart(session, context);
        if(!WebUtils.isHttp(context))
        {
            log.debug("SessionContext argument is not HTTP compatible or does not have an HTTP request/response pair. No session ID cookie will be set.");
            return;
        }
        HttpServletRequest request = WebUtils.getHttpRequest(context);
        HttpServletResponse response = WebUtils.getHttpResponse(context);
        if(isSessionIdCookieEnabled())
        {
            Serializable sessionId = session.getId();
            storeSessionId(sessionId, request, response);
        } else
        {
            log.debug("Session ID cookie is disabled.  No cookie has been set for new session with id {}");
        }
        request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
    }

    public Serializable getSessionId(SessionKey key)
    {
        Serializable id = super.getSessionId(key);
        if(id == null && WebUtils.isWeb(key))
        {
            ServletRequest request = WebUtils.getRequest(key);
            ServletResponse response = WebUtils.getResponse(key);
            id = getSessionId(request, response);
        }
        return id;
    }

    protected Serializable getSessionId(ServletRequest request, ServletResponse response)
    {
        return getReferencedSessionId(request, response);
    }

    protected void onExpiration(Session s, ExpiredSessionException ese, SessionKey key)
    {
        super.onExpiration(s, ese, key);
        onInvalidation(key);
    }

    protected void onInvalidation(Session session, InvalidSessionException ise, SessionKey key)
    {
        super.onInvalidation(session, ise, key);
        onInvalidation(key);
    }

    private void onInvalidation(SessionKey key)
    {
        ServletRequest request = WebUtils.getRequest(key);
        if(request != null)
            request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID);
        if(WebUtils.isHttp(key))
        {
            log.debug("Referenced session was invalid.  Removing session ID cookie.");
            removeSessionIdCookie(WebUtils.getHttpRequest(key), WebUtils.getHttpResponse(key));
        } else
        {
            log.debug("SessionKey argument is not HTTP compatible or does not have an HTTP request/response pair. Session ID cookie will not be removed due to invalidated session.");
        }
    }

    protected void onStop(Session session, SessionKey key)
    {
        super.onStop(session, key);
        if(WebUtils.isHttp(key))
        {
            HttpServletRequest request = WebUtils.getHttpRequest(key);
            HttpServletResponse response = WebUtils.getHttpResponse(key);
            log.debug("Session has been stopped (subject logout or explicit stop).  Removing session ID cookie.");
            removeSessionIdCookie(request, response);
        } else
        {
            log.debug("SessionKey argument is not HTTP compatible or does not have an HTTP request/response pair. Session ID cookie will not be removed due to stopped session.");
        }
    }

    public boolean isServletContainerSessions()
    {
        return false;
    }
    
    private static final Logger log = Logger.getLogger(DefaultWebSessionManager.class);
    //private static final Logger log = LoggerFactory.getLogger(org/apache/shiro/web/session/mgt/DefaultWebSessionManager);
    private Cookie sessionIdCookie;
    private boolean sessionIdCookieEnabled;

}


/*
	DECOMPILATION REPORT

	Decompiled from: E:\tools\maven\repository\org\apache\shiro\shiro-web\1.2.3\shiro-web-1.2.3.jar
	Total time: 116 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/

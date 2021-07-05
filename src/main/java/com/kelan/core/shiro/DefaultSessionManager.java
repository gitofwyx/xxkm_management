package com.kelan.core.shiro;

/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
//Jad home page: http://www.kpdus.com/jad.html
//Decompiler options: packimports(3) radix(10) lradix(10) 
//Source File Name:   DefaultSessionManager.java

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.AbstractValidatingSessionManager;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.SimpleSessionFactory;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;

import com.kelan.riding.route.control.RouteController;

// Referenced classes of package org.apache.shiro.session.mgt:
//            AbstractValidatingSessionManager, SimpleSessionFactory, SimpleSession, SessionFactory, 
//            SessionKey, SessionContext

public class DefaultSessionManager extends AbstractValidatingSessionManager
    implements CacheManagerAware
{

    public DefaultSessionManager()
    {
        deleteInvalidSessions = true;
        sessionFactory = new SimpleSessionFactory();
        sessionDAO = new MemorySessionDAO();
    }

    public void setSessionDAO(SessionDAO sessionDAO)
    {
        this.sessionDAO = sessionDAO;
        applyCacheManagerToSessionDAO();
    }

    public SessionDAO getSessionDAO()
    {
        return sessionDAO;
    }

    public SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    public boolean isDeleteInvalidSessions()
    {
        return deleteInvalidSessions;
    }

    public void setDeleteInvalidSessions(boolean deleteInvalidSessions)
    {
        this.deleteInvalidSessions = deleteInvalidSessions;
    }

    public void setCacheManager(CacheManager cacheManager)
    {
        this.cacheManager = cacheManager;
        applyCacheManagerToSessionDAO();
    }

    private void applyCacheManagerToSessionDAO()
    {
        if(cacheManager != null && sessionDAO != null && (sessionDAO instanceof CacheManagerAware))
            ((CacheManagerAware)sessionDAO).setCacheManager(cacheManager);
    }

    protected Session doCreateSession(SessionContext context)
    {
        Session s = newSessionInstance(context);
        if(log.isTraceEnabled())
            log.trace("Creating session for host {}");
        create(s);
        return s;
    }

    protected Session newSessionInstance(SessionContext context)
    {
        return getSessionFactory().createSession(context);
    }

    protected void create(Session session)
    {
        if(log.isDebugEnabled())
            log.debug((new StringBuilder()).append("Creating new EIS record for new session instance [").append(session).append("]").toString());
        log.info("create");
        sessionDAO.create(session);
    }

    protected void onStop(Session session)
    {
        if(session instanceof SimpleSession)
        {
            SimpleSession ss = (SimpleSession)session;
            java.util.Date stopTs = ss.getStopTimestamp();
            ss.setLastAccessTime(stopTs);
        }
        onChange(session);
    }

    protected void afterStopped(Session session)
    {
        if(isDeleteInvalidSessions())
            delete(session);
    }

    protected void onExpiration(Session session)
    {
        if(session instanceof SimpleSession)
            ((SimpleSession)session).setExpired(true);
        onChange(session);
    }

    protected void afterExpired(Session session)
    {
        if(isDeleteInvalidSessions())
            delete(session);
    }

    protected void onChange(Session session)
    {
        sessionDAO.update(session);
    }

    protected Session retrieveSession(SessionKey sessionKey)
        throws UnknownSessionException
    {
        Serializable sessionId = getSessionId(sessionKey);
        if(sessionId == null)
        {
            log.debug("Unable to resolve session ID from SessionKey [{}].  Returning null to indicate a session could not be found.");
            return null;
        }
        Session s = retrieveSessionFromDataSource(sessionId);
        if(s == null)
        {
            String msg = (new StringBuilder()).append("Could not find session with ID [").append(sessionId).append("]").toString();
            throw new UnknownSessionException(msg);
        } else
        {
            return s;
        }
    }

    protected Serializable getSessionId(SessionKey sessionKey)
    {
        return sessionKey.getSessionId();
    }

    protected Session retrieveSessionFromDataSource(Serializable sessionId)
        throws UnknownSessionException
    {
        return sessionDAO.readSession(sessionId);
    }

    protected void delete(Session session)
    {
        sessionDAO.delete(session);
    }

    protected Collection getActiveSessions()
    {
        Collection active = sessionDAO.getActiveSessions();
        return ((Collection) (active == null ? Collections.emptySet() : active));
    }
    private static final Logger log = Logger.getLogger(RouteController.class);
    //private static final Logger log = LoggerFactory.getLogger(com/kelan/core/shiro/DefaultSessionManager);
    private SessionFactory sessionFactory;
    protected SessionDAO sessionDAO;
    private CacheManager cacheManager;
    private boolean deleteInvalidSessions;

}


/*
	DECOMPILATION REPORT

	Decompiled from: E:\tools\maven\repository\org\apache\shiro\shiro-core\1.2.3\shiro-core-1.2.3.jar
	Total time: 571 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/
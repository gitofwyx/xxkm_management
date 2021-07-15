package com.xxkm.core.shiro;

import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

public class ShiroSessionListener implements SessionListener {

    private Logger logger = Logger.getLogger(ShiroSessionListener.class);

    @Override
    public void onExpiration(Session session) {
        try {
            logger.warn("会话过期：" + session.getId() + " " );
            // session.getTimeout());
            // session.setTimeout(1000);
        } catch (Exception e) {
            logger.warn(e);
        }
    }

    @Override
    public void onStart(Session session) {
        // TODO Auto-generated method stub
        //session.setTimeout(100000);
        logger.warn("会话创建：" + session.getId() + " " + session.getTimeout());

    }

    @Override
    public void onStop(Session session) {
        // TODO Auto-generated method stub
        logger.warn("退出会话：" + session.getId());
    }

}

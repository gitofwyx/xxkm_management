package com.kelan.core.shiro;

import org.apache.shiro.session.mgt.SimpleSession;

public class OnlineSession extends SimpleSession {  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static enum OnlineStatus {  
        on_line("在线"), hidden("隐身"), force_logout("强制退出");  
        private final String info;  
        private OnlineStatus(String info) {  
            this.info = info;  
        }  
        public String getInfo() {  
            return info;  
        }  
    }  
    public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public OnlineStatus getStatus() {
		return status;
	}
	
	public String getSystemHost() {
		return systemHost;
	}
	public void setSystemHost(String systemHost) {
		this.systemHost = systemHost;
	}
	private String userAgent; //用户浏览器类型  
    private OnlineStatus status = OnlineStatus.on_line; //在线状态  
    private String systemHost; //用户登录时系统IP  
    //省略其他  
}   

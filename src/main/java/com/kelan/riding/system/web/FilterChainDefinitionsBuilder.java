package com.kelan.riding.system.web;

import java.util.LinkedHashMap;

public class FilterChainDefinitionsBuilder {

	public LinkedHashMap<String, String> getFilterChainDefinitionsBuilder() {
		LinkedHashMap<String, String> definitions = new LinkedHashMap<String, String>();

		definitions.put("/unAuthorized", "anon");
		definitions.put("/login", "anon");
		definitions.put("/index.jsp", "anon");
		definitions.put("//loginDenied", "anon");
		definitions.put("/regist.jsp", "anon");
		// definitions.put("/route/addPoints", "anon");
		definitions.put("/regist", "anon");
		definitions.put("/checkUserUnique", "anon");
		// definitions.put("/updatePassword", "anon");
		// definitions.put("/userstory/edituserstory", "anon");
		// definitions.put("/route/rountbycity", "anon");
		// definitions.put("/RouteProcessor/**", "anon");
		definitions.put("/route/checkRoute", "anon");
		definitions.put("/route/rountbycity", "anon");
		definitions.put("/route/routeexhibition", "anon");
		definitions.put("/route/routebykeyword", "anon");
		definitions.put("/static/upload/**", "anon");
		definitions.put("/mapimage/**", "anon");
		definitions.put("/favicon.ico", "anon");
		definitions.put("/userstory/edituserstory", "anon");
		// 注销
		definitions.put("/logout", "logout");
		// 单点
		// definitions.put("/**/**", "kickout");
		definitions.put("/login", "kickout");
		definitions.put("/getUserById", "kickout");
		// 角色权限
		// definitions.put("/route/**", "roles[user]");
		// definitions.put("/userstory/**", "roles[user]");
		definitions.put("/getUserById", "roles[user]");
		// 登录权限
		// definitions.put("/**", "authc");

		return definitions;
	}
}

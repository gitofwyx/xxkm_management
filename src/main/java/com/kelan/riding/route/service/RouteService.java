package com.kelan.riding.route.service;

import java.util.List;
import java.util.Map;

import com.kelan.riding.route.entity.Route;
import com.kelan.riding.route.entity.RoutePoint;

public interface RouteService {
	
	public Route getRouteFilter(String... conditions);
	
	public Map<String, Object> getRecRouteMap(String id);
	
	public Route getRecRouteInfo(String id);

	void updateRoute(Route route);
	
	int addPointsToRd(String userId, String json);

	public List<RoutePoint> addPointsByRd(String userId, String json, String date);
	
	//添加线路到缓存
	public boolean makingRouteToRd(Route route);
	
	public Route getRouteByCache(String routeId);
	
	public Route getRouteByCache(String routeId, Route route);

	public void saveRoute(Route route);

	boolean deleteKey(String key, int dbNum);
		
}

package com.xxk.xxkm_management.route.service;

import java.util.List;
import java.util.Map;

import com.xxk.xxkm_management.route.entity.Record;
import com.xxk.xxkm_management.route.entity.RouteProcessor;

public interface UserRouteService {

	boolean addRecord(Record record);
	
	Record getRecord(String userId, String routeId);
	
	boolean updateRecord(String userId,Record record);

	boolean reomveRecordById(String id);

	boolean processRoute(String routeId, String userId, String isStore, String isPraise);

	List<RouteProcessor> getProcessors(String userId);
	
	List<RouteProcessor> getProcessorPage(String userId,int pageNow,int pageSize);

	Map<String, String> getProcessors(String userId, String routeId);

	String isStore(String userId, String routeId);
	
	String isPraise(String userId,String routeId);
	
	boolean deleteProcessors(String routeId,String userId);

}

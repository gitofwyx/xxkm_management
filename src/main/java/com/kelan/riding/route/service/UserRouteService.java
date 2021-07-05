package com.kelan.riding.route.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.kelan.riding.route.entity.Record;
import com.kelan.riding.route.entity.Route;
import com.kelan.riding.route.entity.RouteProcessor;

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

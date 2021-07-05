package com.kelan.riding.route.service;

import java.util.List;

import com.kelan.riding.route.entity.Record;

public interface RecordService {
	
	//查看用户挑战线路战绩
	 public List<Record> RecordByUserPage(String userId,int pageNow,int pageSize);
	 
	 List<Record> checkRecordByUser(String userId);
	 
	 List<Record> getUserRouteRecord(String userId,String routeId);
	 
	 boolean deleteRecord(String id);
}

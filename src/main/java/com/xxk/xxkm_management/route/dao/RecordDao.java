package com.xxk.xxkm_management.route.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xxk.xxkm_management.route.entity.Record;
@Repository
public interface RecordDao {
	
	//查看用户挑战线路战绩
	List<Record> RecordByUserPage(String userId,int pageStart,int pageSize);
	
	List<Record> checkRecordByUser(String userId);
	
	List<Record> getUserRouteRecord(String userId,String routeId);
	
	int deleteRecord(String id);
}

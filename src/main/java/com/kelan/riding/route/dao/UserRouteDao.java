package com.kelan.riding.route.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kelan.riding.route.entity.Record;
import com.kelan.riding.route.entity.RouteProcessor;

public interface UserRouteDao {

	int addRecord(Record record);
	
	Record getRecord(String userId,String routeId);
	
	int updateRecord(String userId,Record record);

	int reomveRecordById(String id);

	RouteProcessor getProcessor(@Param("routeId")String routeId,@Param("userId") String userId);

	int addProcessor(RouteProcessor routeProcessor);

	int updateProcessor(RouteProcessor processor);

	List<RouteProcessor> getProcessors(String userId);
	
	List<RouteProcessor> getProcessorPage(String userId,int pageStart,int pageSize);
	
	int deleteProcessors(String routeId,String userId);

}

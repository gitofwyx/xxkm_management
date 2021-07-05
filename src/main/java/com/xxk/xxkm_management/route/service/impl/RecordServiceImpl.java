package com.xxk.xxkm_management.route.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxk.xxkm_management.route.dao.RecordDao;
import com.xxk.xxkm_management.route.entity.Record;
import com.xxk.xxkm_management.route.service.RecordService;

@Service
public class RecordServiceImpl implements RecordService {
	
	@Autowired
	RecordDao dao;
	
	public List<Record> RecordByUserPage(String userId,int pageNow,int pageSize){
		System.out.println("pageNow:"+pageNow);
		System.out.println("pageSize:"+pageSize);
		return dao.RecordByUserPage(userId,(pageNow-1)*pageSize,pageSize);
		
	}

	@Override
	public List<Record> checkRecordByUser(String userId) {
		return dao.checkRecordByUser(userId);
	}
	
	@Override
	public List<Record> getUserRouteRecord(String userId, String routeId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean deleteRecord(String id){
		return dao.deleteRecord(id)==1?true:false;
		
	}

}

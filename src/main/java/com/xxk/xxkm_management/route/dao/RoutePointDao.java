package com.xxk.xxkm_management.route.dao;

import org.springframework.stereotype.Repository;

import com.xxk.xxkm_management.mongodb.MongodbBaseDao;
import com.xxk.xxkm_management.route.entity.RoutePoint;
@Repository
public class RoutePointDao extends MongodbBaseDao<RoutePoint>{

	@Override
	protected Class<RoutePoint> getEntityClass() {
		return RoutePoint.class;
	}

}

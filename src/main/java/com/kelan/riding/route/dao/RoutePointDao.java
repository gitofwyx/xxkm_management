package com.kelan.riding.route.dao;

import org.springframework.stereotype.Repository;

import com.kelan.riding.mongodb.MongodbBaseDao;
import com.kelan.riding.route.entity.RoutePoint;
@Repository
public class RoutePointDao extends MongodbBaseDao<RoutePoint>{

	@Override
	protected Class<RoutePoint> getEntityClass() {
		return RoutePoint.class;
	}

}

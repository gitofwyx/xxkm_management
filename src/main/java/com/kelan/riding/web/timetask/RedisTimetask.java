package com.kelan.riding.web.timetask;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kelan.riding.redis.SentRedisDao;
import com.kelan.riding.route.dao.RouteDao;
import com.kelan.riding.route.dao.UserRouteDao;
import com.kelan.riding.route.entity.Route;
import com.kelan.riding.route.service.RouteService;

@Component("RedisTimetask")
public class RedisTimetask {
	private static Logger log = Logger.getLogger(RedisTimetask.class);
/*	@Autowired
	private RouteDao routeDao;
	//@Autowired
	private SentRedisDao redisDao;
	//@Autowired
	private RouteService routeService;
	
	public void backupDatabase (){
		Set<String> allKeys=redisDao.keys("*",0);
		if(allKeys.isEmpty()){
			return;
		}
		for (String Key : allKeys) {
			if(routeDao.getRouteInfo(Key)==null){
				Route route=routeService.getRouteByCache(Key);
				routeDao.save(route);
			}
		}
		redisDao.fushDB(0);
		redisDao.fushDB(1);
	}*/

}

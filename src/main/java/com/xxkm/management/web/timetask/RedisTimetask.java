package com.xxkm.management.web.timetask;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

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

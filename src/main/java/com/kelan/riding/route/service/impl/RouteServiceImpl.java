package com.kelan.riding.route.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import com.kelan.core.util.DateUtil;
import com.kelan.core.util.JsonUtils;
import com.kelan.core.util.UUIdUtil;
import com.kelan.riding.redis.SentRedisDao;
import com.kelan.riding.route.dao.RouteDao;
import com.kelan.riding.route.entity.Route;
import com.kelan.riding.route.entity.RoutePoint;
import com.kelan.riding.route.service.RouteService;

@Service
public class RouteServiceImpl implements RouteService {

	private Logger logger = Logger.getLogger(RouteServiceImpl.class);

	@Autowired
	private RouteDao routeDao;

	//@Autowired
	private SentRedisDao redisDao;

	Query query;

	private Supplier<Route> routeSupplier = Route::new;
	private Supplier<RoutePoint> routePointSupplier = RoutePoint::new;

	public Route getRouteFilter(String... conditions) {
		for (int i = 0; i < conditions.length; i = i + 2) {
			query = Query.query(Criteria.where(conditions[0]).is(conditions[1]));
		}

		return null;
	}

	/**
	 * 根据id获取用户推荐的线路和原线路map
	 */
	public Map<String, Object> getRecRouteMap(String id) {
		Map<String, Object> RouteMap = new HashMap<>();
		try {
			Route route = routeDao.getRouteInfo(id);
			if (route == null) {
				return null;
			}
			Route recRoute = route;
			RouteMap.put("route", route);
			List<RoutePoint> Points = new ArrayList<RoutePoint>();
			int tab = 0;
			for (RoutePoint routePoint : route.getPoints()) {
				if ("1".equals(routePoint.getpRec()) || tab == 1) {
					if ("1".equals(routePoint.getpRec())) {
						routePoint.setpType("1");
						Points.add(routePoint);
					} else if ("2".equals(routePoint.getpRec())) {
						routePoint.setpType("3");
						recRoute.setPoints(Points);
						RouteMap.put("recRoute", recRoute);
						break;
					}
					tab = 1;
				}
			}
		} catch (Exception e) {
			return null;
		}
		return RouteMap;
	}

	/**
	 * 根据id获取用户标记推荐的一段线路
	 * 
	 * @return Route
	 */
	public Route getRecRouteInfo(String id) {
		Route route = null;
		List<RoutePoint> points = null;
		int tab = 0;
		try {
			route = routeDao.getRouteInfo(id);
			if (route == null) {
				return null;
			}
			points = new ArrayList<>();
			for (RoutePoint routePoint : route.getPoints()) {
				if ("1".equals(routePoint.getpRec()) || tab == 1) {
					if ("1".equals(routePoint.getpRec())) {
						if (tab == 0) {
							routePoint.setpType("1");
							tab = 1;
						} else {
							routePoint.setpType("3");
							points.add(routePoint);
							break;
						}
					}
					points.add(routePoint);
				}
			}

		} catch (Exception e) {
			logger.error(e);
			return null;
		}
		if (tab == 1 && points != null) {
			route.setPoints(points);
		}
		return route;
	}

	@Override
	public void updateRoute(Route route) {
		routeDao.updateRoute(route);
	}

	@Override
	public int addPointsToRd(String userId, String json) {
		if(redisDao.keys(userId,1).size()==100){
			
		}
		return redisDao.setHSetToDBNum(userId, DateUtil.getFullTime(), 1, json);
	}
	
	/**
	 * 将json转化成线路的节点列表
	 */
	@Override
	public List<RoutePoint> addPointsByRd(String userId, String json, String date) {

		String routeId = "";
		Map<String, String> result = new HashMap<>();
		if (date == null || "".equals(date)) {
			date = DateUtil.getFullTime();
		}
		List<RoutePoint> points = null;
		List<RoutePoint> midPoints = new ArrayList<>();
		try {
			// request.getCharacterEncoding();
			// String json = (String) request.getParameter("json");
			// System.out.println(json);
			// System.out.println(pType);
			points = JsonUtils.objectFromJsonArray(json, RoutePoint.class);
		} catch (Exception e) {
			logger.error("转换json数组出错!");
			result.put("isError", "1");
			result.put("msg", "转换json数组出错!");
		}
		try {
			midPoints = points.stream().filter(p -> p.getRouteId() != null && !p.getRouteId().equals(""))
					.collect(Collectors.toList());

			// if (midPoints.size() < points.size()) {
			// logger.error("提交的节点存在路线id为空的情况!");
			// result.put("isError", "1");
			// result.put("msg", "提交的节点存在路线id为空的情况!");
			// return result;
			// }
			for (RoutePoint routePoint : midPoints) {
				routePoint.setId(UUIdUtil.getUUID());
				routePoint.setCreateDate(date);
				routePoint.setCreateUserId(userId);
				routePoint.setDeleteFlag("0");
				if (routePoint.getRouteId() != null && !routeId.equals(routePoint.getRouteId())) {
					routeId = routePoint.getRouteId();
					// redisDao.saddAndHset(routeId, 1, 2,
					// JsonUtils.beanToMap(routePoint));
				}
			}
			// if (routeId == null || "".equals(routeId)) {
			// logger.error("获取线路出错!");
			// result.put("msg", "获取线路出错!");
			// }
			// route.setUpdateDate(Date);
			// route.setUpdateUserId(userId);
			// // 添加线路到缓存
			// WriteResult writeResult=routeDao.addPoints(route);
			// if (writeResult.isUpdateOfExisting()) {
			// logger.info("添加节点成功!");
			// result.put("succ", "0");
			// result.put("msg", "添加节点成功!");
			// } else {
			// logger.error("添加节点失败!");
			// result.put("isError", "1");
			// result.put("msg", "添加节点失败!");
			// }
		} catch (Exception e) {
			logger.error(e);
			// result.put("isError", "1");
			// result.put("msg", "添加节点失败!");
		}
		return points;
	}

	@Override
	public boolean makingRouteToRd(Route route) {
		return redisDao.hset(route.getId(), 0, JsonUtils.beanToMap(route));
	}

	/**
	 * 获取缓存里的线路
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Route getRouteByCache(String routeId) {
		Route route = routeSupplier.get();
		try {
			Map<String, String> routeMap = redisDao.hgetAll(routeId, 0);
			route = (Route) JsonUtils.toJavaBean(routeMap, route);
			if (routeMap.get("points") != null && !"".equals(routeMap.get("points"))) {
				route.setPoints((List<RoutePoint>) SerializationUtils.deserialize(routeMap.get("points").getBytes()));
			}
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
		return route;
	}

	/**
	 * 获取缓存里的线路，route为空时为获取缓存里的线路节点列表
	 */
	@Override
	public Route getRouteByCache(String routeId, Route route) {
		if (route == null) {
			route = getRouteByCache(routeId);
		}
		List<RoutePoint> pointList = route.getPoints();
		
		Map<String, String> pointMap = new TreeMap<String, String>(redisDao.hgetAll(route.getCreateUserId(), 1));
		//Iterator<String> iter = pointMap.keySet().iterator();
		// pointList = (List<RoutePoint>)
		// SerializationUtils.deserialize(routeMap.get("points").getBytes());
		if (pointList == null || pointList.isEmpty()) {
			pointList = new ArrayList<>();
			for (String key : pointMap.keySet()) {
				System.out.println(key);
				pointList.addAll(addPointsByRd(route.getCreateUserId(), pointMap.get(key), key));
			}
			route.setPoints(pointList);
			// routeDao.updateRoute(route);
			// routeDao.updateRoutePoint(route);
		}
		return route;
	}

	@Override
	public void saveRoute(Route route) {
		routeDao.save(route);
	}

	@Override
	public boolean deleteKey(String key, int dbNum) {
		return redisDao.del(key, dbNum);
	}

}

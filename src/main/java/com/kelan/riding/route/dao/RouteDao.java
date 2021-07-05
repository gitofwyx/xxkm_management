package com.kelan.riding.route.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.kelan.core.util.MethodUtil;
import com.kelan.core.util.StringUtil;
import com.kelan.riding.mongodb.MongodbBaseDao;
import com.kelan.riding.route.entity.Route;
import com.kelan.riding.route.entity.RoutePoint;
import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;

@Repository
public class RouteDao extends MongodbBaseDao<Route> {
	private static final Integer SIZE = 5;

	public static ThreadLocal<String> contentTL = new ThreadLocal<String>();

	public void save(Route route) {
		// MethodUtil.getSetMethod(route.getClass(),"id");
		/*
		 * Map<String, Method> MethodMap =
		 * MethodUtil.getSetMethod(route.getClass()); for (String key :
		 * MethodMap.keySet()) { if
		 * (MethodMap.get(key).getParameterTypes()[0].toString().indexOf(
		 * "String") != -1) { try {
		 * if(MethodUtil.getGetMethod(key,route.getClass()).invoke(route, new
		 * Object[0])!=null){ continue; } MethodMap.get(key).invoke(route, new
		 * Object[] { "" }); } catch (IllegalAccessException |
		 * IllegalArgumentException | InvocationTargetException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } } }
		 */
		Field[] fields = Route.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				@SuppressWarnings("rawtypes")
				Class[] parameterTypes = new Class[1];
				parameterTypes[0] = field.getType();
				if (parameterTypes[0].toString().indexOf("String") != -1) {
					field.setAccessible(true);
					if (field.get(route) != null) {
						continue;
					}
					field.set(route, "");
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.mongoTemplate.save(route);
	}

	@Override
	protected Class<Route> getEntityClass() {
		return Route.class;
	}

	/**
	 * 分页条件查询
	 * 
	 * @param start
	 * @return
	 */
	public List<Route> queryPage(Integer start, Query query, String sortValue) {
		// Query query = Query.query(Criteria.where("type").is("2"));

		// 按哪个字段做排序
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, sortValue));
		Sort sort = new Sort(orders);
		if (null != sort) {
			query.with(sort);
		}

		Page<Route> page = this.getPage(query, start, SIZE, sort);
		return page.getContent();
	}

	/**
	 * 修改路线的基础信息
	 * 
	 * @param route
	 */
	public void updateRoute(Route route) {
		Update update = new Update();

		if (!StringUtil.isNullorEmpty(route.getName())) {
			update.set("name", route.getName());
		}

		if (!StringUtil.isNullorEmpty(route.getMileage())) {
			update.set("mileage", route.getMileage());
		}

		if (!StringUtil.isNullorEmpty(route.getType())) {
			update.set("type", route.getType());
		}

		if (!StringUtil.isNullorEmpty(route.getProvinceCode())) {
			update.set("provinceCode", route.getProvinceCode());
		}

		if (!StringUtil.isNullorEmpty(route.getCityCode())) {
			update.set("cityCode", route.getCityCode());
		}

		if (!StringUtil.isNullorEmpty(route.getAreaCode())) {
			update.set("areaCode", route.getAreaCode());
		}

		if (!StringUtil.isNullorEmpty(route.getPicId())) {
			update.set("picId", route.getPicId());
		}

		if (!StringUtil.isNullorEmpty(route.getRemark())) {
			update.set("remark", route.getRemark());
		}

		if (!StringUtil.isNullorEmpty(route.getKeyWord())) {
			update.set("keyWord", route.getKeyWord());
		}

		if (route.getStoryNumber() >= 0) {
			update.set("storyNumber", route.getStoryNumber());
		}

		if (!StringUtil.isNullorEmpty(route.getDeleteFlag())) {
			update.set("deleteFlag", route.getDeleteFlag());
		}
		update.set("updateDate", route.getUpdateDate());
		update.set("updateUserId", route.getUpdateUserId());
		this.updateMulti(Query.query(Criteria.where("_id").is(route.getId())), update);
	}
	
	public void updateRoutePoint(Route route) {
		Update update = new Update();
		if (route.getPoints() != null && route.getPoints().size() > 0) {
			update.set("points", route.getPoints());
		}
		this.updateMulti(Query.query(Criteria.where("_id").is(route.getId())), update);
	}
	
	
	/**
	 * 获取某个节点的基础信息
	 * 
	 * @return
	 */
	public Route getPoint(String id, String pId) {

		Query query = new Query(Criteria.where("_id").is(id));
		query.fields().elemMatch("points", Criteria.where("_id").is(pId));
		return this.query(query);
	}

	/**
	 * 收藏数统计
	 * 
	 * @param route
	 */
	public void isStore(Route route) {
		Update update = new Update();
		if (route.getCollectNumber() >= 0) {
			update.set("collectNumber", route.getCollectNumber());
		}
		this.updateMulti(Query.query(Criteria.where("_id").is(route.getId())), update);
	}

	/**
	 * 点赞数统计
	 * 
	 * @param route
	 */
	public void isPraise(Route route) {
		Update update = new Update();
		if (route.getCollectNumber() >= 0) {
			update.set("supportNumber", route.getSupportNumber());
		}
		this.updateMulti(Query.query(Criteria.where("_id").is(route.getId())), update);
	}

	/**
	 * 获取线路的开始节点信息
	 * 
	 * @return
	 */
	public Route getPointBypType(String id, String pType) {

		Query query = new Query(Criteria.where("_id").is(id));
		query.fields().elemMatch("points", Criteria.where("pType").is(pType));
		return this.query(query);
	}

	/**
	 * 删除某条路线
	 */
	public void removeRoute(Route route) {
		this.delete(route);
	}

	/**
	 * 删除某个节点
	 * 
	 */
	public void removePoint(String id, String pId) {
		Update update = new Update();
		update.pull("points", new BasicDBObject("_id", pId));
		Query query = Query.query(Criteria.where("_id").is(id));
		this.updateFirst(query, update);
	}

	/**
	 * 修改路线中某个节点的信息
	 * 
	 * @param point
	 */
	public void editPoint(RoutePoint point) {
		Update update = new Update();

		if (!StringUtil.isNullorEmpty(point.getAddress())) {
			update.set("points.$.address", point.getAddress());
		}

		if (!StringUtil.isNullorEmpty(point.getName())) {
			update.set("points.$.name", point.getName());
		}

		update.set("points.$.updateDate", point.getUpdateDate());
		update.set("points.$.updateUserId", point.getUpdateUserId());

		Query query = Query.query(new Criteria().andOperator(Criteria.where("_id").is(point.getRouteId()),
				Criteria.where("points").elemMatch(Criteria.where("_id").is(point.getId()))));
		this.updateFirst(query, update);

	}

	/**
	 * 增加节点
	 * 
	 * @param route
	 * @return
	 * @return
	 */
	public WriteResult addPoints(Route route) {
		Query query = Query.query(Criteria.where("_id").is(route.getId()));
		Update update = new Update();
		update.pushAll("points", route.getPoints().toArray());
		WriteResult result = this.upsert(query, update);
		if (!StringUtil.isNullorEmpty(route.getUpdateUserId())) {
			update.set("updateUserId", route.getUpdateUserId());
		}
		if (!StringUtil.isNullorEmpty(route.getUpdateDate())) {
			update.set("updateDate", route.getUpdateDate());
		}
		if (route.getStoryNumber() != 0) {
			update.set("storyNumber", route.getStoryNumber());
		}
		return result;
	}
	
	/**
	 * 获取线路
	 * @param id
	 * @return
	 */
	public Route getRouteInfo(String id) {
		Query query = new Query(Criteria.where("_id").is(id));
		// query.fields().elemMatch("points", Criteria.where("routeId").is(id));
		return this.query(query);

	}

	public List<Route> getRouteFilter(String sortValue, int start, String... conditions) {
		Query query = null;
		for (int i = 0; i < conditions.length; i = i + 2) {
			if (conditions[i + 1] == null && "".equals(conditions[i + 1])) {
				continue;
			}
			query = Query.query(Criteria.where(conditions[i]).is(conditions[i + 1]));
		}
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, sortValue));
		Sort sort = new Sort(orders);
		if (null != sort && null != query) {
			query.with(sort);
			Page<Route> page = this.getPage(query, start, SIZE, sort);
			return page.getContent();
		}
		return null;
	}
	
	
}

package com.kelan.riding.route.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kelan.core.file.BaseController;
import com.kelan.core.file.ImageInfo;
import com.kelan.core.util.DateUtil;
import com.kelan.core.util.InetAddressUtil;
import com.kelan.core.util.JsonUtils;
import com.kelan.core.util.MySessionContext;
import com.kelan.core.util.UUIdUtil;
import com.kelan.riding.picture.entity.Picture;
import com.kelan.riding.picture.service.PicService;
import com.kelan.riding.route.dao.RouteDao;
import com.kelan.riding.route.entity.PointSel;
import com.kelan.riding.route.entity.Ranking;
import com.kelan.riding.route.entity.Record;
import com.kelan.riding.route.entity.Route;
import com.kelan.riding.route.entity.RoutePoint;
import com.kelan.riding.route.entity.RouteProcessor;
import com.kelan.riding.route.service.RankingService;
import com.kelan.riding.route.service.RecordService;
import com.kelan.riding.route.service.RouteService;
import com.kelan.riding.route.service.UserRouteService;
import com.kelan.riding.story.entity.UserStory;
import com.kelan.riding.story.service.UserStoryService;
import com.kelan.riding.user.service.UserService;
import com.mongodb.WriteResult;

import net.sf.json.JSONArray;

/**
 * 
 * @author 王骏
 *
 */

@Controller
@RequestMapping(value = "/route")
public class RouteController extends BaseController {
	@Autowired
	private RouteDao routeDao;
	@Autowired
	PicService pic;
	@Autowired
	RecordService recordService;
	@Autowired
	private UserRouteService userRouteService;

	private Supplier<RoutePoint> pointSupplier = RoutePoint::new;
	private Supplier<Route> routeSupplier = Route::new;
	private Supplier<Record> recordSupplier = Record::new;
	private Supplier<Ranking> rankingSupplier = Ranking::new;
	private Supplier<UserStory> userStorySupplier = UserStory::new;
	// private Subject currentUser = SecurityUtils.getSubject();
	// private Session session = currentUser.getSession();
	private Logger logger = Logger.getLogger(RouteController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private PicService picService;
	@Autowired
	private RankingService rankingService;
	@Autowired
	private UserStoryService userStoryService;
	//@Autowired
	//private RouteService routeService;

	private String rootDIr = "http://" + InetAddressUtil.IP + ":8080";

	@ResponseBody
	@RequestMapping("")
	public Map<String, String> list() {
		Map<String, String> result = new HashMap<>();
		result.put("isError", "1");
		return result;
	}

	/**
	 * 查看用户所有相关路线的点赞和收藏操作
	 */
	@RequestMapping("/queryUserProcess")
	public void queryUserProcess() {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String userId = (String) session.getAttribute("userId");
		List<RouteProcessor> processors = userRouteService.getProcessors(userId);
		System.out.println(processors);
	}

	/**
	 * 删除用户参与的路线
	 */
	@RequestMapping("/removeRouteRecord")
	public void removeRouteRecord() {
		boolean removeResult = userRouteService.reomveRecordById("9e336f4d-460e-40c0-bde1-d645716f2769");
		System.out.println("reomve record:+" + removeResult);
	}

	/**
	 * 创建挑战排行榜
	 */
	@ResponseBody
	@RequestMapping("/addRanking")
	public Map<String, Object> addRanking(Ranking ranking) {
		// Ranking ranking = rankingSupplier.get();
		Map<String, Object> result = new HashMap<>();
		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		// userId = "e8c6bad8-ac40-4459-b572-309eeeaa5f43";
		try {
			if (ranking != null && !"".equals(ranking)) {
				// Map objectMap = JsonUtils.MapforStrMap(objectJson);
				// if (userId == null) {
				// userId = (String) objectMap.get("userId");
				// }
				// ranking = (Ranking) JsonUtils.toJavaBean(ranking, objectMap);
				ranking.setId(UUIdUtil.getUUID());
				ranking.setCreateDate(DateUtil.getFullTime());
				ranking.setUpdateDate(DateUtil.getFullTime());
				if (ranking.getCreateUserId() == null || "".equals(ranking.getCreateUserId())) {
					ranking.setCreateUserId(userId);
					ranking.setUpdateUserId(userId);
				}
				ranking.setDeleteFlag("0");
				ranking.setPercent("100%");
				if (rankingService.addRank(ranking)) {
					logger.info("创建排行榜信息成功");
					result.put("msg", "创建成功");
				} else {
					logger.info("创建排行榜失败失败");
					return null;
				}
			}
		} catch (Exception e) {
			logger.error("创建排行榜信息失败");
			return null;
		}
		return result;
	}

	/**
	 * 挑战结果详情展示
	 */
	@ResponseBody
	@RequestMapping("/getRankTop")
	public Map<String, Object> getRankTop(@RequestParam(value = "routeId") String routeId) {
		Route route = routeSupplier.get();
		Ranking ranking = rankingSupplier.get();
		// String userId = (String)
		// SecurityUtils.getSubject().getSession().getAttribute("userId");
		Map<String, Object> result = new HashMap<>();
		route = routeDao.getRouteInfo(routeId);
		ranking = rankingService.getRankTop(routeId);
		List<Picture> pictures = pic.listRoutePic(routeId);

		if (ranking != null) {
			result.put("name", route.getName());
			result.put("mileage", route.getMileage());
			result.put("duration", ranking.getDuration());
			result.put("averageSpeed", ranking.getAverageSpeed());
			for (Picture picture : pictures) {
				if (picture.getFlag().equals("2")) {
					result.put("routePic", rootDIr + picture.getPicDir());
				} else {
					result.put("routePic", "不存在");
				}
			}
		} else {
			result.put("msg", "无历史挑战记录");
		}
		return result;
	}

	/**
	 * 增加用户参与的路线记录
	 */
	@ResponseBody
	@RequestMapping("/addUserRouteRecord")
	public Map<String, Object> addUserRouteRecord(Record record, Ranking ranking) {
		// List<Record> Records = null;
		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		// userId = "e8c6bad8-ac40-4459-b572-309eeeaa5f43";
		Map<String, Object> result = new HashMap<>();
		String id = UUIdUtil.getUUID();
		try {
			if (record != null && !"".equals(record)) {
				// json = "{routeId:'123',coordinateX:123,coordinateY:123}";
				// Map jsonmap = new HashMap();
				// jsonmap =JsonUtils.toMap(json);
				// Map objectMap = JsonUtils.MapforStrMap(objectJson);
				// if (userId == null) {
				// userId = (String) objectMap.get("userId");
				// }
				// record = (Record) JsonUtils.toJavaBean(record, objectMap);
				record.setId(id);
				record.setCoordinateZ("0");
				record.setJoinPerson(userId);
				record.setEndTime(DateUtil.getFullTime());
				// record.setAddress("无定位信息");
				record.setCreateDate(DateUtil.getFullTime());
				record.setUpdateDate(DateUtil.getFullTime());
				if (record.getCreateUserId() == null || "".equals(record.getCreateUserId())) {
					record.setCreateUserId(userId);
					record.setUpdateUserId(userId);
				}
				record.setDeleteFlag("0");
				if (record.getPercent() == null || "null".equals(record.getPercent())) {
					record.setPercent("无");
				}

				boolean addResult = userRouteService.addRecord(record);
				if (addResult) {
					logger.info("创建挑战成功");
					result.put("msg", "创建成功");
					if ("100%".equals(record.getPercent())) {
						addRanking(ranking);
					}
				} else {
					logger.error("创建挑战失败1");
					return null;
				}
			} else {
				logger.error("创建挑战失败2");
				return null;
			}
		} catch (Exception e) {
			logger.error("创建挑战失败3");
			System.out.println(e);
			return null;
		}
		return result;
	}

	/**
	 * 查询路线列表
	 */
	@RequestMapping("/pageForList")
	public List<Route> pageForList(String conditions, String value, int pageNumber, String sort) {
		// String chooseConditions,String chooseValue,
		// Query query =
		// Query.query(Criteria.where(conditions).is(value).where(chooseConditions).is(chooseValue)
		// );
		Query query = Query.query(Criteria.where(conditions).is(value));
		List<Route> routes = routeDao.queryPage(pageNumber, query, sort);
		System.out.println(routes.size());
		return routes;
	}

	/**
	 * 删除路线上某个节点
	 */
	@RequestMapping("/removePoint")
	public void removePoint() {
		routeDao.removePoint("cbe2f75d-29b6-4449-a449-af2991617fba", "137e6250-5b56-4c98-83ed-1d7bb57ba2a0");
		System.out.println("removePoint");
	}

	/**
	 * 
	 * @param routeId
	 */

	@ResponseBody
	@RequestMapping(value = "/checkRoute")
	public JSONArray checkRoute(@RequestParam(value = "routeId") String routeId) {

		JSONArray result = new JSONArray();
		List<Map<String, String>> midList = new ArrayList<Map<String, String>>();
		String pointJson = null;

		try {
			// System.out.println("获取线路信息：" + routeId);
			Route route = routeDao.getRouteInfo(routeId);
			// List<RoutePoint> points=
			// route.getPoints().stream().filter(r->r.getIsEdit().equals("1")).collect(Collectors.toList());
			List<RoutePoint> initPoints = route.getPoints();
			// List<RoutePoint> points = route.getPoints();
			List<RoutePoint> points = initPoints.stream().filter(p -> !"0.0".equals(p.getCoordinateX())
					&& !"0.0".equals(p.getCoordinateY()) && p.getRouteId() != null).collect(Collectors.toList());
			Collections.sort(points, new Comparator<Object>() {
				public int compare(Object a, Object b) {
					int first = Integer.parseInt(((RoutePoint) a).getpType());
					int last = Integer.parseInt(((RoutePoint) b).getpType());
					return first - last;
				}
			});
			for (int i = 0; i < points.size(); i++) {
				// System.out.println("第" + (i + 1) + "个点的信息:" + points.get(i));
				/*
				 * System.out.println( "第" + (i + 1) + "个点的ID:" +
				 * points.get(i).getId() + " iSave:" +
				 * points.get(i).getiSave()); System.out.println("第" + (i + 1) +
				 * "个点的X坐标:" + points.get(i).getCoordinateX());
				 * System.out.println("第" + (i + 1) + "个点的Y坐标:" +
				 * points.get(i).getCoordinateY()); System.out.println("第" + (i
				 * + 1) + "个点的ISAVE:" + points.get(i).getiSave());
				 * System.out.println("第" + (i + 1) + "个点的PTYPE:" +
				 * points.get(i).getpType());
				 */
				// System.out.println("rec:" + points.get(i).getpRec());
				Map<String, String> midMap = new LinkedHashMap<String, String>();
				midMap.put("id", points.get(i).getId());
				midMap.put("routeId", points.get(i).getRouteId());
				midMap.put("coordinateX", points.get(i).getCoordinateX());
				midMap.put("coordinateY", points.get(i).getCoordinateY());
				midMap.put("iSave", points.get(i).getiSave());
				midMap.put("pType", points.get(i).getpType());
				midList.add(midMap);
			}

			// logger.info("查询线路节点信息成功!");
		} catch (Exception e) {
			logger.info("查询线路节点信息失败！");
			logger.info(e);
			Map<String, String> midMap = new HashMap<String, String>();
			midMap.put("message", "获取失败");
			midList.add(midMap);
		}
		pointJson = JSONArray.fromObject(midList).toString();
		result = JSONArray.fromObject(pointJson);
		// System.out.println(result);
		return result;
	}

	private List<RoutePoint> createPoints(int num, String routeId, String createUserId, String createDate) {
		List<RoutePoint> points = new ArrayList<>();

		for (int i = 0; i < num; i++) {
			RoutePoint point = pointSupplier.get();
			point.setId(UUIdUtil.getUUID());
			point.setpType("2");
			point.setRouteId(routeId);
			point.setCreateUserId(createUserId);
			point.setCreateDate(createDate);
			point.setUpdateDate(createDate);
			point.setUpdateUserId(createUserId);

			points.add(point);
		}

		return points;
	}

	/**
	 * 查看节点信息，如果不已节点id作为查询条件则会返回所有节点
	 * 
	 * @param routeid,pointid
	 */
	@ResponseBody
	@RequestMapping(value = "/checkPoint", method = RequestMethod.POST)
	public Map<String, Object> checkPoint(String routeid, String pointid) {

		Map<String, Object> result = new HashMap<>();
		Route route = routeDao.getPoint(routeid, pointid);

		System.out.println(route.getPoints());
		result.put("point", route.getPoints());
		return result;

	}

	/**
	 * 更新路线的基本信息
	 * 
	 * @return result
	 * 
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/updateRoute")
	public Map<String, Object> updateRoute(Route routeReq, Ranking ranking, Record record, RoutePoint point,
			MultipartHttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		if (userId == null || "".equals(userId)) {
			String sessionId = request.getParameter("sessionId");
			if (sessionId != null && !"".equals(sessionId)) {
				try {
					userId = (String) MySessionContext.getSession(sessionId).getAttribute("userId");
				} catch (Exception e) {
					logger.error("获取session异常 ID:" + sessionId);
					result.put("msg", "用户没登录");
					// return result;
				}
			}
		}

		Route route = routeSupplier.get();
		List<RoutePoint> points = null;
		String Date = DateUtil.getFullTime();
		// Map<String, Object> objectMap = new HashMap<>();
		try {

			// Map<String, String[]> prammap = request.getParameterMap();
			// for (String key : prammap.keySet()) {
			//
			// String[] pramchar = prammap.get(key);
			// StringBuffer pramBuffer = new StringBuffer();
			// for (int i = 0; i < pramchar.length; i++) {
			// pramBuffer.append(pramchar[i]);
			// }
			// String pram = pramBuffer.toString();
			// objectMap.put(key, pram);
			// }
			// 获取线路
			String routeId = request.getParameter("routeId");
			String address = request.getParameter("address");

			if (routeId == null || "null".equals(routeId)) {
				logger.error("线路ID为空");
				result.put("msg", "上传失败无法获取到线路");
				return result;
			}
			if (address == null || "null".equals(address)) {
				address = "武汉市";
			}
			// route = (Route)
			// SerializationUtils.deserialize(redisService.get(routeId.getBytes("UTF-8")));
			route = routeDao.getRouteInfo(routeId);
			if (route == null) {
				result.put("code", "0001");
				result.put("msg", result.get("msg") + " 无法获取线路");
			}
			if (userId == null || "".equals(userId)) {
				userId = route.getUpdateUserId();
			}
			// objectMap.put("userId", userId);
			// 获取该线路的开始时间
			Route routeBypType = routeDao.getPointBypType(routeId, "1");
			if (routeBypType != null && routeBypType.getPoints() != null) {
				for (RoutePoint pointBypType : routeBypType.getPoints()) {
					if ("1".equals(pointBypType.getpType())) {
						record.setBeginTime(pointBypType.getCreateDate());
						ranking.setBeginTime(pointBypType.getCreateDate());
						logger.info("线路开始时间：" + pointBypType.getCreateDate());
					}
				}
			}
			// System.out.println(objectMap);
			// String routeId = request.getParameter("routeId");
			// String routeName = request.getParameter("name");
			// String routeType = request.getParameter("type");
			// String routeRemark = request.getParameter("routeRemark");
			// String duration = request.getParameter("duration");
			// String averageSpeed = request.getParameter("averageSpeed");
			// String mileage = request.getParameter("mileage");
			// pType = request.getParameter("pType");
			// String address = request.getParameter("address");
			// String coordinateX = request.getParameter("coordinateX");
			// String coordinateY = request.getParameter("coordinateY");

			// Record record = recordSupplier.get();
			// Ranking ranking = rankingSupplier.get();
			// 添加结束节点
			points = route.getPoints();
			// System.out.println("point:" + point);
			point.setId(UUIdUtil.getUUID());
			point.setCreateUserId(userId);
			point.setCreateDate(Date);
			point.setDeleteFlag("0");
			point.setiSave("0");
			point.setpType("3");
			// point.setRouteId(routeId);
			// point.setpType(pType);
			// point.setAddress(address);
			// point.setCoordinateX(coordinateX);
			// point.setCoordinateY(coordinateY);
			// point.setCoordinateZ("0.0");
			points.add(point);
			route.setPoints(points);
			// routeService.getRouteByCache(routeId,route);
			logger.info("添加节点完成!");
			// if (addFlag.isUpdateOfExisting()) {
			// logger.info("添加节点成功!");
			// result.put("isError", "0");
			// result.put("msg", "添加节点成功!");
			// } else {
			// logger.error("添加节点失败!");
			// result.put("isError", "1");
			// result.put("msg", "添加节点失败!");
			// }
			// 创建挑战者记录
			// Map<String, Object> recordmap = new HashMap<>();
			// List<Map<String, Object>> recordlist = new ArrayList<>();
			// recordmap.put("routeId", point.getRouteId());
			// recordmap.put("coordinateX", point.getCoordinateX());
			// recordmap.put("coordinateY", point.getCoordinateY());
			// recordmap.put("duration", duration);
			// recordmap.put("averageSpeed", averageSpeed);
			// recordlist.add(recordmap);
			record.setCreateUserId(userId);
			record.setPercent("100%");
			ranking.setCreateUserId(userId);
			if (addUserRouteRecord(record, ranking) != null) {
				logger.info("挑战记录创建完成");
			}
			// 创建排行榜
			// ranking.setCreateUserId(userId);
			// if (addRanking(ranking) != null) {
			// logger.info("排行榜记录创建完成");
			// }
			// ranking.setId(UUIdUtil.getUUID());
			// ranking.setCreateUserId(userId);
			// ranking.setCreateDate(Date);
			// ranking.setUpdateDate(Date);
			// ranking.setRouteId(routeId);
			// ranking.setBeginTime("");
			// ranking.setPercent("100%");
			// route.setId(routeId);
			// route=(Route)JsonUtils.toJavaBean(route, objectMap);
			if (routeReq.getName() == null || "".equals(routeReq.getName())) {
				routeReq.setName("骑行图");
			}
			route.setName(routeReq.getName());
			route.setType(routeReq.getType());
			route.setMileage(routeReq.getMileage());
			if (routeReq.getRemark() == null || "".equals(routeReq.getRemark())) {
				routeReq.setRemark("该用户没什么可说的");
			}
			route.setRemark(routeReq.getRemark());
			route.setCityCode("武汉");
			route.setKeyWord(route.getName() + route.getType() + route.getCityCode());
			route.setDeleteFlag("0");
			// route.setType(routeType);
			// route.setName(routeName);
			// route.setRemark(routeRemark);
			// route.setMileage(mileage);
			route.setUpdateDate(Date);
			route.setUpdateUserId(userId);
			String[] picDir = null;
			// redisService.set(route.getId().getBytes("UTF-8"),
			// SerializationUtils.serialize((route)));
			routeDao.save(route);
			//routeService.deleteKey(route.getId(), 0);
			// System.out.println(redisService.hget(route.getId(), "type"));
			// redisService.set(route.getcId(),
			// SerializationUtil.serialize(route).toString());
			logger.info("线路更新完成");
			// loadNnm = (String)
			// ContextLoader.getCurrentWebApplicationContext().getServletContext()
			// .getAttribute("loadNnm");
			// if (loadNnm == null || "".equals(loadNnm)) {
			// loadNnm = "0";
			// }
			// ContextLoader.getCurrentWebApplicationContext().getServletContext().setAttribute("loadNnm",
			// Integer.parseInt(loadNnm) + 1);
			// 保存图片（故事封面）

			result = uploadFile(request);
			if ("0".equals(result.get("isError")) && result.get("nameList") != null) {
				List<ImageInfo> nameList = (List<ImageInfo>) result.get("nameList");
				result.put("nameList", nameList);
				result.put("code", "0000");
				result.put("msg", "上传成功");
				picDir = new String[nameList.size()];
				// 获取图片保存路径
				for (int i = 0; i < nameList.size(); i++) {
					picDir[i] = nameList.get(i).getImgPath();
				}
				// 保存图片信息到数据库
				for (int i = 0; i < picDir.length; i++) {
					Picture pic = new Picture();
					String picId = UUIdUtil.getUUID();
					pic.setId(picId);
					pic.setPicDir(picDir[i]);
					pic.setCreateDate(Date);
					pic.setCreateUserId(userId);
					pic.setDeleteFlag("0");
					pic.setObjectId(routeId);
					pic.setFlag("2");
					boolean picResult = picService.addRoutePic(pic);
					if (picResult) {
						logger.info("上传图片成功");
					}
				}
			} else {
				result.put("code", "0001");
				result.put("msg", result.get("msg") + " 无封面不给建");
				logger.error("无法获取图片");
			}
		} catch (Exception e) {
			logger.error("更新线路信息失败");
			result.put("code", "1111");
			System.out.println(e);
			result.put("msg", "上传失败");
		}
		return result;
	}

	/**
	 * 上传线路封面
	 * 
	 * @param request
	 * @param routeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/routeMapPic", method = RequestMethod.POST)
	public Map<String, Object> routeMapPic(MultipartHttpServletRequest request,
			@RequestParam(value = "routeId") String routeId) {
		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		String Date = DateUtil.getFullTime();
		Map<String, Object> result = new HashMap<>();
		result = uploadFile(request);
		String[] picDir = null;
		try {
			if ("0".equals(result.get("isError")) && result.get("nameList") != null) {
				@SuppressWarnings("unchecked")
				List<ImageInfo> nameList = (List<ImageInfo>) result.get("nameList");
				result.put("nameList", nameList);
				result.put("code", "0000");
				result.put("msg", "上传成功");
				picDir = new String[nameList.size()];
				// 获取图片保存路径
				for (int i = 0; i < nameList.size(); i++) {
					picDir[i] = nameList.get(i).getImgPath();
				}
				// 保存图片信息到数据库
				for (int i = 0; i < picDir.length; i++) {
					Picture pic = new Picture();
					String picId = UUIdUtil.getUUID();
					pic.setId(picId);
					pic.setPicDir(picDir[i]);
					pic.setCreateDate(Date);
					pic.setCreateUserId(userId);
					pic.setDeleteFlag("0");
					pic.setObjectId(routeId);
					pic.setFlag("1");
					boolean picResult = picService.addRoutePic(pic);
					if (picResult) {
						logger.info("上传图片成功");
						result.put("msg", "上传图片成功");
					}
				}
			} else {
				result.put("code", "0001");
				result.put("msg", "是不是没图");
				logger.error("无法获取图片");
			}
		} catch (Exception e) {
			System.out.println(e);
			result.put("msg", "上传失败");
		}
		return result;
	}

	/**
	 * 在存在的路线上增加节点
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addPoints", method = RequestMethod.POST)
	public Map<String, String> addPoints(@RequestParam(value = "json") String json) {
		Map<String, String> result = new HashMap<>();
		try {
			String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
			String routeId = (String) SecurityUtils.getSubject().getSession().getAttribute("routeId");
			logger.info(routeId);
			if (userId == null) {
				result.put("msg", "用户ID为空!");
				return result;
			}
			Route route=routeSupplier.get();
			List<RoutePoint> Points=JsonUtils.getObjectFromJsonArray(json,RoutePoint.class);
			if(routeId==null||"".equals(routeId)){
				routeId=Points.get(0).getRouteId();
			}
			for (RoutePoint routePoint : Points) {
				routePoint.setId(UUIdUtil.getUUID());
			}
			route.setId(routeId);
			route.setPoints(Points);
			// userId = "e8c6bad8-ac40-4459-b572-309eeeaa5f43";
			WriteResult addFlag = routeDao.addPoints(route);
			if (addFlag.isUpdateOfExisting()) {
				// logger.info("添加节点成功!");
				result.put("succ", "0");
				result.put("msg", "添加节点成功!");
			} else {
				logger.error("添加节点失败!");
				result.put("isError", "1");
				result.put("msg", "添加节点失败!");
			}
		} catch (Exception e) {
			logger.error("严重:更新线路出错" + e);
			result.put("isError", "1");
			result.put("msg", "添加节点失败!");
		}
		// System.out.println("result:"+result);
		return result;
	}

	/**
	 * 在存在的路线上增加节点
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addPoint", method = RequestMethod.POST)
	public Map<String, String> addPoint(@RequestParam(value = "routeId") String routeId) {
		return null;
	}

	/**
	 * 获取可编辑节点
	 * 
	 * @param routeId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPointByiSave")
	public Map<String, Object> getPointByiSave(@RequestParam(value = "routeId") String routeId) {

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> listpoint = new HashMap<>();
		List<Object> pointlist = new ArrayList<>();
		List<RoutePoint> midPoints = new ArrayList<>();
		// Map<String, Object> pointmap = new HashMap<>();
		String pointId = "";
		try {
			if (routeId != null && !routeId.equals("")) {
				System.out.println("获取列表线路参数" + routeId);
				Route route = routeDao.getRouteInfo(routeId);
				/*if (route == null || "".equals(route)) {
					route = routeService.getRouteByCache(routeId);
				}*/
				UserStory userstory = userStorySupplier.get();
				List<RoutePoint> points = route.getPoints();
				List<Picture> userstorypic = new ArrayList<>();
				List<String> pointpic = new ArrayList<>();
				if (points == null || points.isEmpty()) {
					// result.put("msg", "可编辑节点列表为空");
					pointpic.add(rootDIr + "/mapimage/骑行节点图.jpg");
					listpoint.put("pointpic", pointpic);
					result.put("msg", listpoint);
					result.put("warn", "节点为空");
					return result;
				}
				midPoints = points.stream().filter(p -> "1".equals(p.getiSave()) || "2".equals(p.getiSave()))
						.collect(Collectors.toList());
				for (RoutePoint point : midPoints) {
					PointSel pointsel = new PointSel();
					pointId = point.getId();
					pointsel.setPointId(pointId);
					// 根据节点获取故事
					userstory = userStoryService.getUserStoryByPoint(pointId);
					if (userstory == null || "".equals(userstory)) {
						pointsel.setPointpic(rootDIr + "/mapimage/骑行节点图.jpg");
						pointsel.setTitle("未编辑");
						pointlist.add(pointsel);
						// System.out.println(pointlist);
						continue;
					}
					// 获取故事图片
					userstorypic = picService.listUserStoryPic(userstory.getId());
					for (Picture picture : userstorypic) {
						pointpic.add(rootDIr + picture.getPicDir());
						pointsel.setPointpic(rootDIr + picture.getPicDir());
					}
					// pointmap.put("pointpic", pointpic);
					if (userstory.getTitle() == null || "".equals(userstory.getTitle())) {
						pointsel.setTitle("未编辑");
					} else {
						pointsel.setTitle(userstory.getTitle());
					}
					pointlist.add(pointsel);
					// System.out.println(pointlist);
				}
				if (pointpic.isEmpty()) {
					pointpic.add(rootDIr + "/mapimage/骑行节点图.jpg");
				}
				listpoint.put("pointpic", pointpic);
				// pointlist.add(pointmap);
				// System.out.println(pointlist);
				listpoint.put("point", pointlist);
				result.put("msg", listpoint);
				result.put("succ", "获取成功");
			} else {
				result.put("isError", "获取失败");
				result.put("msg", "线路ID为空");
			}
		} catch (Exception e) {
			System.out.println(e);
			result.put("isError", "获取失败");
			result.put("msg", "没有获取到线路数据");
			logger.info("获取线路节点信息失败！");
		}
		// System.out.println("可编辑节点列表：" + result);
		return result;
	}

	/**
	 * 修改节点
	 * 
	 * @param userid,pointId,routeid
	 *            return
	 */
	@ResponseBody
	@RequestMapping(value = "/editPoint", method = RequestMethod.GET)
	public void editPoint() {
		String pointId = "63bf09cb-309d-4eed-844c-42295b29cdd6";
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String updateUserId = (String) session.getAttribute("userId");
		String updateDate = DateUtil.getFullTime();

		RoutePoint point = pointSupplier.get();
		point.setId(pointId);
		point.setName("节点");
		point.setAddress("海口市五公祠");
		point.setUpdateDate(updateDate);
		point.setRouteId("ba11874f-74bb-450a-94d4-56a560a86933");
		point.setUpdateUserId(updateUserId);

		routeDao.editPoint(point);

		System.out.println("editPoint");
	}

	/**
	 * 新增路线
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/createroute", method = RequestMethod.POST)
	public Map<String, Object> createRoute(Route route) {
		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		// userId = "e8c6bad8-ac40-4459-b572-309eeeaa5f43";
		// System.out.println("新增线路用户参数" + userId);
		Map<String, Object> result = new HashMap<String, Object>();
		if (userId == null || "".equals(userId)) {
			logger.info("用户未登录");
			userId="5517d871-a2b5-49c7-9bec-4de91d02194a";
		}
		String routeId = UUIdUtil.getUUID();
		String createDate = DateUtil.getFullTime();
		// Route route = routeSupplier.get();
		try {
			/*
			 * List<Method> ListMethod =
			 * MethodUtil.getSetMethod(route.getClass()); for (Method method :
			 * ListMethod) { if
			 * (method.getParameterTypes()[0].toString().indexOf("String") !=
			 * -1) { method.invoke(route, new Object[] { "" }); } }
			 */
			route.setId(routeId);
			route.setName("骑行图");
			route.setCreateUserId(userId);
			route.setCreateDate(createDate);
			route.setUpdateDate(createDate);
			route.setUpdateUserId(userId);
			route.setDeleteFlag("0");
			routeDao.save(route);
			SecurityUtils.getSubject().getSession().setAttribute("routeId", route.getId());
			// redisService.set(route.getId().getBytes("UTF-8"),
			// SerializationUtils.serialize(route));
			/*if (routeService.makingRouteToRd(route)) {
				logger.info("添加路线成功!");
				routeService.deleteKey(userId, 1);
			} else {
				logger.info("路线缓存失败!");
			}*/
			// System.out.println(redisService.hget(route.getId(), "id"));
			// route=(Route)
			// SerializationUtils.deserialize(redisService.get(routeId.getBytes("UTF-8")));
		} catch (Exception e) {
			logger.error("添加路线失败!\n" + e);
		}

		result.put("routeId", route.getId());
		logger.info("CREATE NEW ROUTE : " + result);

		return result;
	}

	/**
	 * 根据创建者查询路线
	 * 
	 * @author wanghui
	 * @param userid
	 */
	@ResponseBody
	@RequestMapping(value = "/routebyuserid", method = RequestMethod.POST)
	public JSONArray RouteByUserId(@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "handle", required = false) String handle, HttpServletRequest request) {

		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		JSONArray result = new JSONArray();
		List<Map<String, Object>> midList = new ArrayList<Map<String, Object>>();
		String pointJson = null;
		System.out.println("创建者参数" + userId);
		int pageNum = 1;
		if (pageNumber != null || !"".equals(pageNumber)) {
			pageNum += Integer.parseInt(pageNumber);
		}
		//System.out.println("当前登录用户ID是：" + userId);
		//System.out.println("刷新/请求：" + handle); // up:上拉操作为请求下一页数据，down:下拉操作为刷新数据,load:第一次进入时的加载数据
		//System.out.println("pageNumber：" + pageNum);
		//System.out.println("数据请求次数：" + (Integer.parseInt(pageNumber) + 1)); // 0：第一次请求数据，1,2,3……:上拉请求下一页数据
		if (userId != null && !userId.equals("")) {
			try {
				List<Route> routes = new ArrayList<Route>();
				// 根据userid获取线路信息
				if ("load".equals(handle) || pageNum == 0) {
					routes = pageForList("createUserId", userId, 1, "createDate");

				} else if ("up".equals(handle) || pageNum > 0) {
					routes = pageForList("createUserId", userId, pageNum, "createDate");
				}
				if (routes == null || routes.isEmpty()) {
					result.add(0);
					return result;
				}
				for (Route route : routes) {
					// 根据routeid获取线路图片
					List<Picture> routePics = picService.listRoutePic(route.getId());
					Map<String, Object> midMap = new HashMap<String, Object>();
					midMap.put("id", route.getId());
					midMap.put("points", checkRoute(route.getId()));
					midMap.put("routeName", route.getName());
					// 封装图片
					for (Picture pic : routePics) {
						if (pic.getFlag().equals("1")) {
							midMap.put("mapPic", rootDIr + pic.getPicDir());
						}

						if (pic.getFlag().equals("2")) {
							midMap.put("coverPic", rootDIr + pic.getPicDir());
						}
					}
					if (midMap.get("mapPic") == null) {
						char last = route.getId().charAt(route.getId().length() - 1);
						midMap.put("mapPic", picService.getDefaultMap(last, rootDIr));
					}
					if (midMap.get("coverPic") == null) {
						midMap.put("coverPic", rootDIr + "/mapimage/加载失败.jpg");
					}
					midList.add(midMap);
					pointJson = JSONArray.fromObject(midList).toString();
					result = JSONArray.fromObject(pointJson);
					// System.out.println(route);
				}
			} catch (Exception e) {
				result.add(e);
				logger.info(userId + "获取用户创建线路时异常!");
				logger.info("异常信息为" + e);
			}
			logger.info(userId + "获取用户创建线路成功！");
		} else {
			Map<String, Object> fail = new HashMap<String, Object>();
			fail.put("isError", "获取失败");
			fail.put("msg", "没有获取到用户数据");
			result.add(fail);
		}
		// System.out.println(result);
		return result;
	}

	/**
	 * 显示路线列表
	 * 
	 * @param 根据条件查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getRouteByAround")
	public JSONArray getRouteByAround(@RequestParam(value = "cityCode") String cityCode,
			@RequestParam(value = "areaCode", required = false) String areaCode) {

		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		JSONArray result = new JSONArray();
		List<Route> initRoutes = routeDao.getRouteFilter("createDate", 1, "cityCode", cityCode, "areaCode", areaCode);
		List<Route> routes = initRoutes.stream().filter(p -> p.getRemark() != null && !"".equals(p.getRemark()))
				.collect(Collectors.toList());
		try {
			if (routes.size() != 0) {
				List<Map<String, Object>> midList = new ArrayList<Map<String, Object>>();
				String pointJson = null;
				for (Route route : routes) {
					// 根据routeid获取线路图片
					List<Picture> routePics = pic.listRoutePic(route.getId());

					Map<String, Object> midMap = new HashMap<String, Object>();
					Map<String, String> status = new HashMap<String, String>();
					midMap.put("id", route.getId());
					midMap.put("points", checkRoute(route.getId()));
					if (route.getAreaCode() == null) {
						route.setAreaCode("无");
					}
					midMap.put("areaCode", route.getAreaCode());

					midMap.put("supportNumber", route.getSupportNumber());
					// 封装图片
					for (Picture pic : routePics) {
						if (pic.getFlag().equals("1")) {
							midMap.put("mapPic", rootDIr + pic.getPicDir());
						}
						// 当无法获取到线路图的时候根据ROUTEID最后一位随机生成mapPic
						if (pic.getFlag().equals("2")) {
							midMap.put("coverPic", rootDIr + pic.getPicDir());
						}
					}
					if (midMap.get("mapPic") == null) {
						char last = route.getId().charAt(route.getId().length() - 1);
						midMap.put("mapPic", picService.getDefaultMap(last, rootDIr));
					}
					if (midMap.get("coverPic") == null) {
						midMap.put("coverPic", rootDIr + "/mapimage/加载失败.jpg");
					}
					// 获取用户收藏信息
					status = userRouteService.getProcessors(userId, route.getId());

					if (status != null) {
						midMap.put("isStore", status.get("isStore"));
						midMap.put("isPraise", status.get("isPraise"));
					} else {
						midMap.put("isStore", "0");
						midMap.put("isPraise", "0");
					}
					midList.add(midMap);
					pointJson = JSONArray.fromObject(midList).toString();
					result = JSONArray.fromObject(pointJson);
				}
			} else {
				result.add(0);
				logger.info("线路数据已加载完");
			}
		} catch (Exception e) {
			result.add(e);
			logger.info(cityCode + "获取线路时出现异常!");
			logger.info("异常信息为" + e);
		}
		// System.out.println("result" + result);
		return result;
	}

	/**
	 * 推荐线路
	 * 
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/recLengthRoute")
	public Map<String, Object> recLengthRoute(@RequestParam(value = "routeId") String routeId,
			@RequestParam(value = "firstPId") String firstPId, @RequestParam(value = "secondId") String secondId) {
		Map<String, Object> result = new HashMap<>();
		try {
			Route route = routeDao.getRouteInfo(routeId);
			List<RoutePoint> points = route.getPoints();
			for (RoutePoint routePoint : points) {
				if (firstPId.equals(routePoint.getId()) || secondId.equals(routePoint.getId())) {
					routePoint.setpRec("1");
				}
			}
			route.setPoints(points);
			routeDao.updateRoutePoint(route);
			result.put("msg", "wc");
		} catch (Exception e) {
			result.put("msg", "error");
		}
		return result;
	}

	/**
	 * 筛选
	 */
	public List<Route> RouteChoose(String conditions, String value, String chooseConditions, String chooseValue,
			int pageNumber, String sort) {

		Query query = Query.query(Criteria.where(conditions).is(value).and(chooseConditions).is(chooseValue));

		// query = Query.query(Criteria.where(createDateTime).gt("2016-11-12
		// 00:00:00"));

		List<Route> routes = routeDao.queryPage(pageNumber, query, sort);
		// System.out.println(routes.size());
		return routes;
	}

	/**
	 * 删除路线
	 */
	@ResponseBody
	@RequestMapping(value = "/removeroute"/* , method = RequestMethod.POST */)
	public JSONArray removeRoute(@RequestParam(value = "routeId") String routeId) {
		JSONArray result = new JSONArray();
		Map<String, Object> msg = new HashMap<String, Object>();
		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		if (routeId != null && !routeId.equals("")) {
			System.out.println("删除操作用户参数" + routeId);
			Route route = routeDao.getRouteInfo(routeId);
			if (route == null) {
				msg.put("error", "獲取线路出错");
				msg.put("msg", "线路不存在或系统出错");
				result.add(msg);
			} else if (userId != null && route.getCreateUserId().equals(userId)) {
				// route.setId(routeId);
				routeDao.removeRoute(route);
				pic.deleteRoutePic(routeId);// 删除线路关联图片

				msg.put("succ", "删除成功");
				msg.put("msg", "已删除所选数据");
				result.add(msg);
			} else {
				msg.put("error", "删除出错");
				msg.put("msg", "不是管理員或不是该线路的创建者无法删除该线路");
				result.add(msg);
				logger.error("用户：" + userId + "不是创建者无法删除");
			}
		} else {

			msg.put("isError", "获取失败");
			msg.put("msg", "没有获取到用户数据");
			result.add(msg);
		}
		logger.info("删除结果" + result);
		return result;
	}

	/**
	 * 用户挑战信息查询
	 * 
	 * @param userId
	 */
	@ResponseBody
	@RequestMapping(value = "/checkrecord", method = RequestMethod.POST)
	public JSONArray checkRecord() {
		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		System.out.println("userId" + userId);
		JSONArray result = new JSONArray();
		if (userId == null || "".equals(userId)) {
			logger.warn("获取用户ID失败");
			result.add("用户没登陆");
		}
		try {
			List<Map<String, Object>> midList = new ArrayList<Map<String, Object>>();
			List<Record> records = recordService.checkRecordByUser(userId);
			if (records.size() != 0) {
				for (Record record : records) {
					Route route = routeDao.getRouteInfo(record.getRouteId());
					if (route == null) {
						if (recordService.deleteRecord(record.getId())) {
							logger.warn("已删除无线路对应的用户挑战记录");
						}
						continue;
					}
					Map<String, Object> midMap = new HashMap<String, Object>();
					midMap.put("routeId", record.getRouteId());
					midMap.put("routeLength", route.getMileage());
					midMap.put("routeType", route.getType());
					midMap.put("routeName", route.getName());
					midMap.put("recordTime", record.getCreateDate());
					midMap.put("recordPrecent", record.getPercent());
					midList.add(midMap);
					String pointJson = JSONArray.fromObject(midList).toString();
					result = JSONArray.fromObject(pointJson);
				}
				if (result.isEmpty()) {
					result.add(0);
				}
				logger.info("获取数据成功，共获取到" + records.size() + "挑战线路");
			} else {
				result.add(0);
				logger.info("挑战数据已加载完");
			}
		} catch (Exception e) {
			result.add(e);
			logger.info(userId + "获取用户创建线路时异常!");
			logger.info("异常信息为" + e);
		}
		// System.out.println(result);
		return result;
	}

	/**
	 * 查看用户收藏的线路
	 * 
	 * @param userId
	 */
	@ResponseBody
	@RequestMapping(value = "/routebyusercollection", method = RequestMethod.POST)
	public JSONArray RouteByUserCollection(@RequestParam(value = "pageNumber") String pageNumber) {
		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		JSONArray result = new JSONArray();
		Map<String, Object> fail = new HashMap<String, Object>();
		List<Map<String, Object>> midList = new ArrayList<Map<String, Object>>();
		String pointJson = null;

		List<RouteProcessor> processors = userRouteService.getProcessorPage(userId, Integer.parseInt(pageNumber) + 1,
				5);
		if (processors == null || processors.isEmpty()) {
			result.add(0);
			return result;
		}
		if (userId != null && !userId.equals("")) {
			try {
				for (RouteProcessor processor : processors) {
					Map<String, Object> midMap = new HashMap<String, Object>();
					if (processor.getRouteId() == null || "".equals(processor.getRouteId())
							|| "0".equals(processor.getStoreFlag())) {
						continue;
					}
					Route route = routeDao.getRouteInfo(processor.getRouteId());
					if (route == null || "".equals(route)) {
						if (userRouteService.deleteProcessors(processor.getRouteId(), userId)) {
							logger.error("已永久删除无线路关联的收藏数据！");
						}
						continue;
					}
					// 根据routeid获取线路图片
					List<Picture> routePics = picService.listRoutePic(route.getId());
					midMap.put("routeId", route.getId());
					midMap.put("points", checkRoute(route.getId()));
					midMap.put("routeName", route.getName());
					// 封装图片
					for (Picture pic : routePics) {
						if (pic.getFlag().equals("1")) {
							midMap.put("mapPic", rootDIr + pic.getPicDir());
						}
						if (pic.getFlag().equals("2")) {
							midMap.put("coverPic", rootDIr + pic.getPicDir());
						}
					}
					if (midMap.get("mapPic") == null) {
						char last = route.getId().charAt(route.getId().length() - 1);
						midMap.put("mapPic", picService.getDefaultMap(last, rootDIr));
					}
					if (midMap.get("coverPic") == null) {
						midMap.put("coverPic", rootDIr + "/mapimage/加载失败.jpg");
					}
					midList.add(midMap);
					pointJson = JSONArray.fromObject(midList).toString();
					result = JSONArray.fromObject(pointJson);
					// System.out.println(route);
				}
				if (pointJson == null) {
					result.add(0);
					logger.info("用户收藏已加载完");
				}
			} catch (Exception e) {
				result.add(e);
				logger.info(userId + "获取用户收藏线路时异常!");
				logger.info("异常信息为" + e);
			}
		} else {
			fail.put("isError", "获取失败");
			fail.put("msg", "没有获取到用户数据");
			logger.info("没有获取到用户数据");
		}
		// System.out.println(result);
		return result;
	}

	/**
	 * 查看用户挑战的线路
	 * 
	 * @param userId
	 */
	@ResponseBody
	@RequestMapping(value = "/routebyuserchallenge", method = RequestMethod.POST)
	public JSONArray RouteByUserChallenge(@RequestParam(value = "pageNumber") String pageNumber) {

		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		JSONArray result = new JSONArray();
		List<Map<String, Object>> midList = new ArrayList<Map<String, Object>>();
		String pointJson = null;

		List<Record> records = recordService.RecordByUserPage(userId, Integer.parseInt(pageNumber) + 1, 5);
		if (records == null || records.isEmpty()) {
			result.add(0);
			System.out.println(result);
			return result;
		}
		if (userId != null && !userId.equals("")) {
			try {
				for (Record record : records) {
					//
					Route route = routeDao.getRouteInfo(record.getRouteId());
					if (route == null || "".equals(route)) {
						if (recordService.deleteRecord(record.getId())) {
							logger.warn("已删除没有线路关联的挑战记录！");
						} else {
							logger.warn("无法获取相关挑战信息！");
						}
						continue;
					}
					// 根据routeid获取线路图片
					List<Picture> routePics = picService.listRoutePic(route.getId());
					Map<String, Object> midMap = new HashMap<String, Object>();
					midMap.put("id", route.getId());
					midMap.put("points", checkRoute(route.getId()));
					midMap.put("routeName", route.getName());
					// 线路节点
					midMap.put("points", checkRoute(route.getId()));
					// 封装图片
					for (Picture pic : routePics) {
						if (pic.getFlag().equals("1")) {
							midMap.put("mapPic", rootDIr + pic.getPicDir());
						}

						if (pic.getFlag().equals("2")) {
							midMap.put("coverPic", rootDIr + pic.getPicDir());
						}
					}
					if (midMap.get("mapPic") == null) {
						char last = route.getId().charAt(route.getId().length() - 1);
						midMap.put("mapPic", picService.getDefaultMap(last, rootDIr));
					}
					if (midMap.get("coverPic") == null) {
						midMap.put("coverPic", rootDIr + "/mapimage/加载失败.jpg");
					}
					midList.add(midMap);
					pointJson = JSONArray.fromObject(midList).toString();
					result = JSONArray.fromObject(pointJson);
					// System.out.println(route);
					// logger.info(userId + "获取用户挑战线路成功！");
				}
				if (result == null || result.isEmpty()) {
					result.add(0);
					logger.info("用户挑战已加载完");
				}
			} catch (Exception e) {
				result.add(e);
				logger.info(userId + "获取用户挑战线路时异常!");
				logger.info("异常信息为" + e);
			}
		} else {
			Map<String, Object> fail = new HashMap<String, Object>();
			fail.put("isError", "获取失败");
			fail.put("msg", "没有获取到用户数据");
			result.add(fail);
		}
		// System.out.println(result);
		return result;
	}

	/**
	 * 线路展示
	 */
	@ResponseBody
	@RequestMapping(value = "/routeexhibition"/*
												 * , method = RequestMethod.POST
												 */)
	public JSONArray RouteExhibtion(@RequestParam(value = "routeId", required = false) String routeId) {
		JSONArray result = new JSONArray();
		Route route = routeDao.getRouteInfo(routeId);
		if(route==null){
			return result;
		}
		// Route route =
		// routeDao.getRouteInfo("f732e9e1-9d83-4b7d-9554-823b97d362ba");
		List<Map<String, Object>> midList = new ArrayList<Map<String, Object>>();
		// 获取线路的必要信息
		Map<String, Object> routeMap = new HashMap<String, Object>();
		routeMap.put("routeId", route.getId());
		routeMap.put("routeName", route.getName());
		routeMap.put("routeDistince", route.getMileage());
		routeMap.put("routeRemark", route.getRemark());
		routeMap.put("creatDate", route.getCreateDate());
		routeMap.put("routeCreater", userService.getUserById(route.getCreateUserId()).getName());
		routeMap.put("routeCreaterPic", rootDIr + pic.getUserPic(route.getCreateUserId()));
		List<Picture> pictures = pic.listRoutePic(routeId);
		for (Picture picture : pictures) {
			if (picture.getFlag().equals("2")) {
				routeMap.put("routePic", rootDIr + picture.getPicDir());
			}
		}
		if (routeMap.get("routePic") == null || "".equals(routeMap.get("routePic"))) {
			routeMap.put("routePic", rootDIr + "/mapimage/骑行图.jpg");
		}
		// 获取用时
		List<Ranking> rankList = rankingService.RankByCreateUser(route.getCreateUserId(), route.getId());
		if (rankList.size() > 0) {
			routeMap.put("routeTime", rankList.get(0).getDuration());
		} else {
			routeMap.put("routeTime", "0.00h");
		}
		midList.add(routeMap);
		// String routeJson = JSONArray.fromObject(routeMap).toString();
		result = JSONArray.fromObject(routeMap);

		// 获取结点信息
		routeMap = getPointByiSave(routeId);
		if (!routeMap.isEmpty()) {
			midList.add(routeMap);
		}
		// midList.add(getPointByiSave("f732e9e1-9d83-4b7d-9554-823b97d362ba"));
		result = JSONArray.fromObject(JSONArray.fromObject(midList).toString());

		// 获取前十名挑战者信息
		routeMap = getRanking(routeId);
		if (!routeMap.isEmpty()) {
			midList.add(routeMap);
		}
		// midList.add(getRanking("1"));
		// System.out.println("rankingJson:" + rankingJson);
		result = JSONArray.fromObject(JSONArray.fromObject(midList).toString());

		// System.out.println("结果" + result);
		return result;
	}

	/**
	 * 获取线路排名
	 * 
	 * @param routeId
	 * @return
	 */
	private Map<String, Object> getRanking(String routeId) {

		Map<String, Object> result = new HashMap<String, Object>();
		// Map<String, Object> rankingMap = new HashMap<>();
		List<Map<String, Object>> midList = new ArrayList<Map<String, Object>>();
		List<Ranking> rankings = rankingService.listRankByRouteId(routeId);
		// List<Ranking> rankings = rankingService.listRankByRouteId("1");
		int j = 1;
		for (int i = rankings.size() - 1; i >= 0; i--) {
			Ranking ranking = rankings.get(i);
			Map<String, Object> rankMap = new HashMap<String, Object>();
			// User user = userService.getUserById(ranking.getCreateUserId());

			rankMap.put("rankingNo", j++);
			rankMap.put("ranker", userService.getUserById(ranking.getCreateUserId()).getName());
			rankMap.put("rankerPic", rootDIr + pic.getUserPic(ranking.getCreateUserId()));
			rankMap.put("rankPercent", ranking.getPercent());
			rankMap.put("rankDuration", ranking.getDuration());
			rankMap.put("rankScore", "暂无");

			midList.add(rankMap);
			result.put("msg", midList);
		}
		return result;
	}

	/**
	 * 模糊查询
	 */
	@ResponseBody
	@RequestMapping(value = "/routebykeyword", method = RequestMethod.POST)
	public JSONArray RouteByKeyWord(@RequestParam(value = "keyWord", required = false) String keyWord,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,
			@RequestParam(value = "handle", required = false) String handle) {

		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");

		System.out.println("模糊查询的条件是：" + keyWord);
		JSONArray result = new JSONArray();
		List<Route> routes = new ArrayList<Route>();
		// String createDate;
		int pageNum = 1; // 请求页面

		if (pageNumber != null || !"".equals(pageNumber)) {
			pageNum += Integer.parseInt(pageNumber);
		}
		System.out.println("页数：" + pageNum);
		// 第一次进入时，根据定位信息筛选线路{cityCode}
		try {
			// 首先根据handle判断操作
			if (handle.equals("load")) {
				routes = FuzzyQuery(keyWord, 1, "createDate");
			} else if (handle.equals("up")) {
				routes = FuzzyQuery(keyWord, pageNum, "createDate");
			} else if (handle.equals("down")) {
				routes = null;
			}

			if (routes.size() != 0) {
				List<Map<String, Object>> midList = new ArrayList<Map<String, Object>>();
				String pointJson = null;
				for (Route route : routes) {
					// 根据routeid获取线路图片
					// System.out.println(route.getKeyWord());
					List<Picture> routePics = pic.listRoutePic(route.getId());
					Map<String, Object> midMap = new HashMap<String, Object>();
					Map<String, String> status = new HashMap<String, String>();
					midMap.put("id", route.getId());
					midMap.put("routeName", route.getName());
					// 封装图片
					for (Picture pic : routePics) {
						if (pic.getFlag().equals("1")) {
							midMap.put("mapPic", rootDIr + pic.getPicDir());
						}
						// 当无法获取到线路图的时候根据ROUTEID最后一位随机生成mapPic
						if (pic.getFlag().equals("2")) {
							midMap.put("coverPic", rootDIr + pic.getPicDir());
						}
					}
					if (midMap.get("mapPic") == null) {
						char last = route.getId().charAt(route.getId().length() - 1);
						midMap.put("mapPic", picService.getDefaultMap(last, rootDIr));
					}
					if (midMap.get("coverPic") == null) {
						midMap.put("coverPic", rootDIr + "/mapimage/加载失败.jpg");
					}
					// 获取用户收藏信息
					status = userRouteService.getProcessors(userId, route.getId());

					if (status != null) {
						midMap.put("isStore", status.get("isStore"));
						midMap.put("isPraise", status.get("isPraise"));
					} else {
						midMap.put("isStore", "0");
						midMap.put("isPraise", "0");
					}

					midList.add(midMap);
					pointJson = JSONArray.fromObject(midList).toString();
					result = JSONArray.fromObject(pointJson);

				}
			} else {
				result.add(0);
				logger.info("线路数据已加载完");
			}
		} catch (Exception e) {
			result.add(e);
			logger.info(keyWord + "获取线路时出现异常!");
			logger.info("异常信息为" + e);
		}
		// System.out.println("result" + result);
		return result;

	}

	public List<Route> FuzzyQuery(String value, int pageNumber, String sort) {
		// Query query = Query.query(Criteria.where("name").in("%" + value+
		// "%"));
		Query query = Query.query(Criteria.where("keyWord").regex(value));
		List<Route> routes = routeDao.queryPage(pageNumber, query, sort);
		System.out.println(routes.size());
		return routes;
	}

	@ResponseBody
	@RequestMapping(value = "/rountbycity")
	public JSONArray rountByCity(@RequestParam(value = "cityCode", required = false) String cityCode,
			@RequestParam(value = "pageNumber", required = false) String pageNumber,//第几页（默认一页5行）
			@RequestParam(value = "handle", required = false) String handle,//手势
			@RequestParam(value = "chooseType", required = false) String chooseType,//线路类型
			@RequestParam(value = "level", required = false) String level,
			@RequestParam(value = "chooseCondition", required = false) String chooseCondition) {

		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		// String loadDate = (String)
		// SecurityUtils.getSubject().getSession().getAttribute("loadDate");
		/*
		 * System.out.println("当前登录用户ID是：" + userId);
		 * System.out.println("刷新/请求：" + handle); //
		 * up:上拉操作为请求下一页数据，down:下拉操作为刷新数据,load:第一次进入时的加载数据
		 * System.out.println("pageNumber：" + pageNumber);
		 * System.out.println("数据请求次数：" + (Integer.parseInt(pageNumber) + 1));
		 * // 0：第一次请求数据，1,2,3……:上拉请求下一页数据 System.out.println("筛选类型：" +
		 * chooseType); System.out.println("筛选条件：" + chooseCondition);
		 * System.out.println("城市：" + cityCode); System.out.println("层次：" +
		 * level);
		 */
		JSONArray result = new JSONArray();
		List<Route> routes = new ArrayList<Route>();
		// String createDate;
		int pageNum = 1; // 请求页面
		if (pageNumber != null || !"".equals(pageNumber)) {
			pageNum += Integer.parseInt(pageNumber);
		}
		// 第一次进入时，根据定位信息筛选线路{cityCode}
		try {
			// 首先根据handle判断操作
			if (handle.equals("load")) {
				routes = RouteChoose("cityCode", cityCode, chooseType, chooseCondition, 1, "createDate");
				// routes = pageForList("cityCode",cityCode,1);

			} else if (handle.equals("up")) {
				routes = RouteChoose("cityCode", cityCode, chooseType, chooseCondition, pageNum, "createDate");
				// routes = pageForList("cityCode",cityCode,1);
			} else if (handle.equals("down")) {
				// routes = RouteChoose("cityCode", cityCode, chooseType,
				// chooseCondition, pageNum + 1);
				// SecurityUtils.getSubject().getSession().setAttribute("loadNnm",
				// "0");
				routes = null;
			}
			if (routes.size() != 0) {
				List<Map<String, Object>> midList = new ArrayList<Map<String, Object>>();
				String pointJson = null;
				for (Route route : routes) {
					// 根据routeid获取线路图片
					List<Picture> routePics = pic.listRoutePic(route.getId());

					Map<String, Object> midMap = new HashMap<String, Object>();
					Map<String, String> status = new HashMap<String, String>();
					midMap.put("id", route.getId());
					midMap.put("points", checkRoute(route.getId()));
					midMap.put("routeName", route.getName());
					midMap.put("mileage", route.getMileage());
					if (route.getAreaCode() == null) {
						route.setAreaCode("无");
					}
					midMap.put("areaCode", route.getAreaCode());

					midMap.put("supportNumber", route.getSupportNumber());
					// 封装图片
					for (Picture pic : routePics) {
						if (pic.getFlag().equals("1")) {
							midMap.put("mapPic", rootDIr + pic.getPicDir());
						}
						// 当无法获取到线路图的时候根据ROUTEID最后一位随机生成mapPic
						if (pic.getFlag().equals("2")) {
							midMap.put("coverPic", rootDIr + pic.getPicDir());
						}
					}
					if (midMap.get("mapPic") == null) {
						char last = route.getId().charAt(route.getId().length() - 1);
						midMap.put("mapPic", picService.getDefaultMap(last, rootDIr));
					}
					if (midMap.get("coverPic") == null) {
						midMap.put("coverPic", rootDIr + "/mapimage/加载失败.jpg");
					}
					// 获取用户收藏信息
					status = userRouteService.getProcessors(userId, route.getId());

					if (status != null) {
						midMap.put("isStore", status.get("isStore"));
						midMap.put("isPraise", status.get("isPraise"));
					} else {
						midMap.put("isStore", "0");
						midMap.put("isPraise", "0");
					}
					midList.add(midMap);
					pointJson = JSONArray.fromObject(midList).toString();
					result = JSONArray.fromObject(pointJson);
				}
			} else {
				result.add(0);
				logger.info(handle + "线路数据已加载完");
			}
		} catch (Exception e) {
			result.add(e);
			logger.info(cityCode + "获取线路时出现异常!");
			logger.info("异常信息为" + e);
		}
		// System.out.println("result" + result);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/getUserByRoute")
	public Map<String, String> getUserByRoute(@RequestParam(value = "routeId") String routeId) {
		Map<String, String> result = new HashMap<>();
		String userId = userService.getUserById(routeDao.getRouteInfo(routeId).getCreateUserId()).getId();
		result.put("touxiang", rootDIr + pic.getUserPic(userId));
		return result;
	}

	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value = "/asynctask", method = RequestMethod.GET)//异步调试
	 * public String asyncTask(HttpServletRequest request, HttpServletResponse
	 * response) { //DeferredResult<ModelAndView> deferredResult = new
	 * DeferredResult<ModelAndView>();
	 * System.out.println("/asynctask 调用！thread id is : " +
	 * Thread.currentThread().getId()); List<String> zuoyes = new
	 * ArrayList<String>(); for (int i = 0; i < 10; i++) { zuoyes.add("zuoye" +
	 * i); } //final AsyncContext ac = request.startAsync(); //doZuoye(ac,
	 * zuoyes); try { longTimeAsyncCallService.saveUser(zuoyes);
	 * longTimeAsyncCallService.async(); } catch (Exception e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * System.out.println("布置作业"); return "线程"; }
	 */

	/*
	 * private void doZuoye(final AsyncContext ac, final List<String> zuoyes) {
	 * ac.setTimeout(1 * 60 * 60 * 1000L); ac.start(new Runnable() {
	 * 
	 * @Override public void run() { // 通过response获得字符输出流 try { for (String
	 * zuoye : zuoyes) { System.out.println(zuoye); Thread.sleep(1 * 1000L); }
	 * ac.complete(); } catch (Exception e) { e.printStackTrace(); } } }); }
	 */
}

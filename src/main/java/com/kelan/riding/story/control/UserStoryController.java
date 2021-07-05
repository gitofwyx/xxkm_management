package com.kelan.riding.story.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang.SerializationUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kelan.core.file.BaseController;
import com.kelan.core.file.ImageInfo;
import com.kelan.core.util.DateUtil;
import com.kelan.core.util.InetAddressUtil;
import com.kelan.core.util.JsonUtils;
import com.kelan.core.util.UUIdUtil;
import com.kelan.riding.picture.entity.Picture;
import com.kelan.riding.picture.service.PicService;
import com.kelan.riding.route.dao.RouteDao;
import com.kelan.riding.route.entity.Route;
import com.kelan.riding.route.entity.RoutePoint;
import com.kelan.riding.route.service.RouteService;
import com.kelan.riding.story.entity.UserStory;
import com.kelan.riding.story.service.UserStoryService;

import net.sf.json.JSONArray;

@Controller
@RequestMapping(value = "/userstory")
public class UserStoryController extends BaseController {
	private static Logger log = Logger.getLogger(UserStoryController.class);

	@Autowired
	private UserStoryService userStoryService;
	@Autowired
	private PicService picService;
	@Autowired
	private RouteDao routeDao;
	//@Autowired
	//private RouteService routeService;

	private Supplier<UserStory> userStorySupplier = UserStory::new;

	String rootDIr = "http://" + InetAddressUtil.IP + ":8080";

	/**
	 * 故事上传，包含多图片上上传
	 * 
	 * @param request
	 * @param reponse
	 * @return
	 */
	@RequiresRoles("")
	@ResponseBody
	@RequestMapping(value = "edituserstory")
	public Map<String, Object> editUserStory(MultipartHttpServletRequest request) {

		String loginId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		// loginId="e8c6bad8-ac40-4459-b572-309eeeaa5f43";
		// 故事保存，但是不包括故事图片的保存
		RoutePoint point = new RoutePoint();
		UserStory userStory = userStorySupplier.get();
		String createDate = DateUtil.getFullTime(); // 生成创建时间
		String updateDate = DateUtil.getFullTime();
		String userStoryId = UUIdUtil.getUUID();
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, String[]> prammap = request.getParameterMap();
			Map<String, Object> objectmap = new HashMap<>();
			for (String key : prammap.keySet()) {

				String[] pramchar = prammap.get(key);
				StringBuffer pramBuffer = new StringBuffer();
				for (int i = 0; i < pramchar.length; i++) {
					pramBuffer.append(pramchar[i]);
				}
				String pram = pramBuffer.toString();
				objectmap.put(key, pram);
			}
			Route route = routeDao.getRouteInfo((String)objectmap.get("routeId"));
			if(route==null||"".equals(route)){
				log.error("没有对应该routeId的线路信息！");
				result.put("msg", "线路信息出错");
				return result;
			}
			if (loginId == null || "".equals(loginId)) {
				loginId = route.getCreateUserId();
			}
			userStory = (UserStory) JsonUtils.toJavaBean(objectmap,userStory);
			userStory.setId(userStoryId);
			// String isave =(String) request.getParameter("isave");
			userStory.setCreateUserId(loginId);
			userStory.setCreateDate(createDate);
			userStory.setDeleteFlag("0");
			// userStory.setRemark(request.getParameter("remark"));
			// userStory.setType(request.getParameter("type"));
			// userStory.setLongitude(Double.parseDouble(request.getParameter("longitude")));
			// userStory.setLatitude(Double.parseDouble(request.getParameter("latitude")));
			// userStory.setTitle(request.getParameter("title"));
			
			//isave为1表示立即编辑
			if (objectmap.get("isave") != null && "1".equals((String) objectmap.get("isave"))) {
				List<RoutePoint> points = new ArrayList<>();
				String pointId = UUIdUtil.getUUID();
				route.setUpdateDate(updateDate);
				route.setUpdateUserId(loginId);
				route.setStoryNumber(route.getStoryNumber()+1);
				point.setId(pointId);
				point.setRouteId(route.getId());
				userStory.setPointId(pointId);
				point.setiSave((String) objectmap.get("isave"));
				point.setpType("2");
				point.setStoryNumber(point.getStoryNumber()+1);
				point.setCoordinateX(request.getParameter("longitude"));
				point.setCoordinateY(request.getParameter("latitude"));
				point.setCreateDate(createDate);
				point.setCreateUserId(loginId);
				point.setDeleteFlag("0");
				points.add(point);
				route.setPoints(points);
				//routeService.addPointsToRd(loginId, JSONArray.fromObject(points).toString());
				//routeService.makingRouteToRd(route);
				routeDao.addPoints(route);
				//redisService.set(route.getId().getBytes("UTF-8"), SerializationUtils.serialize((route)));
				//route=(Route) SerializationUtils.deserialize(redisService.get(route.getId().getBytes("UTF-8")));
				log.info("节点新建成功");
			}
			//isave为2表示以后编辑
			if (objectmap.get("isave") != null && "2".equals((String) objectmap.get("isave"))) {
				String pointId = request.getParameter("pointId");
				if (pointId == null && "".equals(pointId)) {
					log.info("节点ID为空");
					result.put("msg", "编辑失败");
					return result;
				}
				route.setStoryNumber(route.getStoryNumber()+1);
				//redisService.set(route.getId().getBytes("UTF-8"), SerializationUtils.serialize((route)));
				//route=(Route) SerializationUtils.deserialize(redisService.get(route.getId().getBytes("UTF-8")));
				routeDao.addPoints(route);
				userStory.setPointId(pointId);
				log.info("故事绑定节点完成");
			}
		} catch (Exception e) {
			System.out.println(e);
			log.info("节点新建失败");
		}

		// 多图保存功能

		String[] picDir = null;
		try {
			boolean userStoryResult = userStoryService.addUserStory(userStory);
			if (userStoryResult) {
				log.info("上传故事成功");
			} else {
				log.info("上传故事失败");
			}
			result = uploadFile(request);
			if ("0".equals(result.get("isError"))) {
				@SuppressWarnings("unchecked")
				List<ImageInfo> nameList = (List<ImageInfo>) result.get("nameList");
				result.put("nameList", nameList);

				result.put("code", "0000");
				result.put("msg", "上传成功");

				System.out.println("大小" + nameList.size());
				System.out.println("lalal" + result.toString());
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
					pic.setCreateDate(createDate);

					pic.setCreateUserId("testuser");
					pic.setDeleteFlag("0");
					pic.setObjectId(userStory.getId());

					boolean picResult = picService.addUserStoryPic(pic);
					if (picResult) {
						log.info("上传图片成功");
					} else {
						log.info("上传图片失败");
					}
				}

			} else {
				result.put("code", "0001");
				result.put("msg", result.get("msg"));
			}
		} catch (Exception e) {
			result.put("code", "1111");
			System.out.println(e);
			result.put("msg", "上传失败");
		}
		System.out.println("result:" + result);
		return result;
	}

	/**
	 * 故事列表，根据用户id
	 * 
	 */

	@RequestMapping(value = "/listuserstory")
	@ResponseBody
	public JSONArray listUserStory(@RequestParam(value = "pageNumber") String pageNumber,
			@RequestParam(value = "pageSize",required=false) String pageSize) {
		
		JSONArray result = new JSONArray();
		List<UserStory> userStoryList;
		List<Map<String, String>> midList = new ArrayList<Map<String, String>>();
		if(pageSize==null||"".equals(pageSize)){
			pageSize="8";
		}
		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		// userStoryList = userStoryService.listUserStory(loginId);
		try {
			userStoryList = userStoryService.listUserStory(userId, Integer.parseInt(pageNumber) + 1,
					Integer.parseInt(pageSize));// 用于测试函数是否成功，接入后删除！
			if (userStoryList != null && !userStoryList.isEmpty()) {
				for (UserStory userStory : userStoryList) {
					List<Picture> userStoryPic = picService.listUserStoryPic(userStory.getId());// 用于测试函数是否成功，接入后删除！
					if (userStoryPic == null || userStoryPic.isEmpty()) {
						continue;
					}
					Map<String, String> midMap = new HashMap<String, String>();
					midMap.put("storyId", userStory.getId());
					midMap.put("storyTitle", userStory.getTitle());
					midMap.put("storyCreateDate", userStory.getCreateDate());
					if (userStory.getAddress() == null || "".equals(userStory.getAddress())) {
						midMap.put("storyAddress", "未获取到地址");
					}
					midMap.put("storyPic", rootDIr + userStoryPic.get(0).getPicDir());
					midList.add(midMap);
					String pointJson = JSONArray.fromObject(midList).toString();
					result = JSONArray.fromObject(pointJson);
				}
			} else {
				result.add(0);
			}
			//log.info("获取成功");
		} catch (Exception e) {
			Map<String, Object> fail = new HashMap<String, Object>();
			fail.put("isError", "获取失败");
			result.add(fail);
			log.info("出现异常" + e);
		}

		//System.out.println(result);
		return result;
	}

	/**
	 * 根据故事id，获取故事详情
	 */
	@ResponseBody
	@RequestMapping(value = "/loaduserstory")
	public JSONArray loadUserStory(@RequestParam(value = "storyId", required = false) String storyId,
			@RequestParam(value = "pointId", required = false) String pointId) {

		//String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
//		if(storyId==null||"".equals(storyId)){
//			storyId=pointId;
//			pointId=null;
//		}
		System.out.println("userStoryId" + storyId);
		UserStory userStory = userStorySupplier.get();
		List<Picture> userStoryPic = new ArrayList<Picture>();
		JSONArray result = new JSONArray();
		Map<String, String> midMap = new HashMap<String, String>();
		try {
			if (storyId != null && !storyId.equals("")) {
				userStory = userStoryService.getUserStory(storyId);
				userStoryPic = picService.listUserStoryPic(storyId);
			} else if (pointId != null && !pointId.equals("")) {
				userStory = userStoryService.getUserStoryByPoint(pointId);
				userStoryPic = picService.listUserStoryPic(userStory.getId());
			}
			// userStory =
			// userStoryService.getUserStory("1050c95b-3b55-43c8-b8e4-7d787e0704f2");
			// userStoryPic =
			// picService.listUserStoryPic("1050c95b-3b55-43c8-b8e4-7d787e0704f2");
			System.out.println("aa:" + userStory.getId());
			System.out.println("aa:" + userStory.getRouteId());
			Route route = routeDao.getRouteInfo(userStory.getRouteId());
			if(route==null||"".equals(route)){
				midMap.put("storyRouteName", "无法获取线路名，该线路数据丢失。。。");
			}else{midMap.put("storyRouteName", route.getName());}
			midMap.put("storyTitle", userStory.getTitle());
			midMap.put("storyCreateDate", userStory.getCreateDate());
			if (userStory.getAddress() == null || "".equals(userStory.getAddress())) {
				midMap.put("storyAddress", "获取失败");
			} else {
				midMap.put("storyAddress", userStory.getAddress());
			}

			midMap.put("storyRemark", userStory.getRemark());
			String storyPic = "";
			for (Picture picture : userStoryPic) {
				storyPic += rootDIr + picture.getPicDir() + ",";
			}
			midMap.put("storyPic", storyPic);
			List<Map<String, String>> midList = new ArrayList<Map<String, String>>();
			midList.add(midMap);
			String storyJson = JSONArray.fromObject(midList).toString();
			result = JSONArray.fromObject(storyJson);

			log.info("获取成功");
		} catch (Exception e) {
			result.add("无法获取该节点相关的故事信息");
			log.info("异常,线路节点为空或为用户未编辑" + e);
		}
		System.out.println("result" + result);
		return result;
	}

}

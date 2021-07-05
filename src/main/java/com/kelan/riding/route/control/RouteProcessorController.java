package com.kelan.riding.route.control;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.kelan.riding.route.dao.RouteDao;
import com.kelan.riding.route.entity.Route;
import com.kelan.riding.route.service.UserRouteService;

/**
 * 
 * @author 王骏
 *
 */
@Controller
@RequestMapping("/RouteProcessor")
public class RouteProcessorController {
	 @Autowired
	 private RouteDao routeDao;
	 
	@Autowired
	private UserRouteService userRouteService;
	
	private Route route;
	private Logger logger = Logger.getLogger(RouteProcessorController.class);
	private Map<String, String> route_status = new HashMap<>();

	@ResponseBody
	@RequestMapping("")
	public Map<String, String> list() {
		Map<String, String> result = new HashMap<>();
		result.put("isError", "1");

		System.out.println(">>>>123");
		return result;
	}

	/**
	 * 查看用户所有相关路线的点赞和收藏操作
	 */
	@ResponseBody
	@RequestMapping(value="queryUserProcess", method = RequestMethod.POST)
	public Map<String, String> queryUserProcess(@RequestParam("routeId") String routeId) {
		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		try {
			route_status = userRouteService.getProcessors(userId, routeId);
			if(route_status==null){
				route_status = new HashMap<>();
				route_status.put("isStore", "0");
				route_status.put("isPraise", "0");
				return route_status;
			}
			System.out.println(route_status);
		} catch (Exception e) {
			logger.info(e);
		}
		return route_status;
	}

	/**
	 * 对路线做收藏和点赞操作
	 */
	@RequestMapping("processRoute")
	public void processRoute(@RequestParam("routeId") String routeId) {
		// String routeId = "483f9b57-98ab-4088-b5a7-b602be416e8a";
		String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
		Map<String, String> status = userRouteService.getProcessors(userId, routeId);
		if (status == null) {
			System.out.println("路线信息为空");
			return;
		}
		String isStore = status.get("isStore");
		String isPraise = status.get("isPraise");

		boolean processResult = userRouteService.processRoute(routeId, userId, isStore, isPraise);
		System.out.println("processResult: " + processResult);
	}
	
	@ResponseBody
	@RequestMapping(value="isStore", method = RequestMethod.POST)
	public Map<String, String> isStore(@RequestParam(value="routeId",required=false) String routeId) {
		Map<String, String> status =new HashMap<>();
		System.out.println("操作的线路id是：" + routeId);
		try {
			String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
			if (userId == null ||"".equals(userId)) {
				logger.info("userId获取失败");
				status.put("msg", "用户没登录");
				return status;
			}
			System.out.println("用户ID："+userId);
			route_status = userRouteService.getProcessors(userId, routeId);
			route = routeDao.getRouteInfo(routeId);
			String isStore = "";
			String isPraise = "";
			if (route_status == null) {
				isStore = "1";
				isPraise = "0";
				route.setCollectNumber(route.getCollectNumber()+1);
				routeDao.isStore(route);
			} else {
				isStore = route_status.get("isStore");
				isPraise = route_status.get("isPraise");
				if ("".equals(isStore) || "0".equals(isStore)) {
					isStore = "1";
					route.setCollectNumber(route.getCollectNumber()+1);
					routeDao.isStore(route);
				} else if ("1".equals(isStore)) {
					isStore = "0";
					route.setCollectNumber(route.getCollectNumber()-1);
					routeDao.isStore(route);
				}
			}
			boolean processResult = userRouteService.processRoute(routeId, userId, isStore, isPraise);
			if (processResult) {
				status.put("msg", "收藏操作成功");
				//logger.info("收藏操作成功");
			} else {
				status.put("msg", "系统异常");
				logger.info("收藏操作失败");
			}
		} catch (Exception e) {
			status.put("msg", "系统异常");
			logger.info("收藏操作出错");
			System.out.println(e);
		}
		//System.out.println(status);
		return status;
	}
	
	@ResponseBody
	@RequestMapping("isPraise")
	public Map<String, String> isPraise(@RequestParam(value="routeId",required=false) String routeId) {
		Map<String, String> status =new HashMap<>();
		System.out.println("操作的线路id是：" + routeId);
		try {
			String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
			if (userId == null ||"".equals(userId)) {
				logger.info("userId获取失败");
				status.put("msg", "用户没登录");
				return status;
			}
			System.out.println("用户ID："+userId);
			route_status = userRouteService.getProcessors(userId, routeId);
			route = routeDao.getRouteInfo(routeId);
			String isStore = "";
			String isPraise = "";
			if (route_status == null) {
				isStore = "0";
				isPraise = "1";
				route.setCollectNumber(route.getSupportNumber()+1);
				routeDao.isPraise(route);
			} else {
				isStore =(String) route_status.get("isStore");
				isPraise =(String) route_status.get("isPraise");
				if ("".equals(isPraise) || "0".equals(isPraise)) {
					isPraise = "1";
					route.setCollectNumber(route.getSupportNumber()+1);
					routeDao.isPraise(route);
				} else if ("1".equals(isPraise)) {
					isPraise = "0";
					route.setCollectNumber(route.getSupportNumber()-1);
					routeDao.isPraise(route);
				}
			}
			boolean processResult = userRouteService.processRoute(routeId, userId, isStore, isPraise);
			if (processResult) {
				status.put("msg", "点赞操作成功");
				//logger.info("点赞操作成功");
			} else {
				status.put("msg", "系统异常");
				logger.info("点赞操作失败");
			}
		} catch (Exception e) {
			status.put("msg", "系统异常");
			logger.info("点赞操作出错");
			System.out.println(e);
		}
		//System.out.println(status);
		return status;
	}
}

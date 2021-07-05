package com.kelan.riding.route.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kelan.core.util.UUIdUtil;
import com.kelan.riding.route.dao.RouteDao;
import com.kelan.riding.route.dao.UserRouteDao;
import com.kelan.riding.route.entity.Ranking;
import com.kelan.riding.route.entity.Record;
import com.kelan.riding.route.entity.Route;
import com.kelan.riding.route.entity.RoutePoint;
import com.kelan.riding.route.entity.RouteProcessor;
import com.kelan.riding.route.service.UserRouteService;
import com.kelan.riding.story.entity.UserStory;
import com.mongodb.WriteResult;

@Service
public class UserRouteServiceImpl implements UserRouteService {

	@Autowired
	private UserRouteDao dao;

	@Override
	public boolean addRecord(Record record) {
		return dao.addRecord(record) == 1 ? true : false;
	}

	@Override
	public Record getRecord(String userId, String routeId) {
		return dao.getRecord(userId, routeId);
	}

	@Override
	public boolean updateRecord(String userId, Record record) {
		return dao.updateRecord(userId, record) == 1 ? true : false;
	}

	@Override
	public boolean reomveRecordById(String id) {
		return dao.reomveRecordById(id) >= 1 ? true : false;
	}

	@Transactional
	@Override
	public boolean processRoute(String routeId, String userId, String isStore, String isPraise) {

		RouteProcessor processor = dao.getProcessor(routeId, userId);

		if (processor == null) {
			processor = new RouteProcessor();
			processor.setId(UUIdUtil.getUUID());
			processor.setRouteId(routeId);
			processor.setUserId(userId);
			processor.setPraiseFlag(isPraise);
			processor.setStoreFlag(isStore);
			return dao.addProcessor(processor) == 1 ? true : false;

		} else {
			processor.setPraiseFlag(isPraise);
			processor.setStoreFlag(isStore);
			return dao.updateProcessor(processor) == 1 ? true : false;
		}

	}

	/**
	 * 根据线路ID获取该线路的点赞数
	 */
	@Override
	public List<RouteProcessor> getProcessors(String userId) {
		return dao.getProcessors(userId);
	}

	/**
	 * 根据线路ID和用戶ID获取该线路的点赞数
	 */
	@Override
	public Map<String, String> getProcessors(String userId, String routeId) {
		try {
			// List<RouteProcessor> processors = dao.getProcessors(userId);
			RouteProcessor processor = dao.getProcessor(routeId, userId);
			if (processor != null && routeId.equals(processor.getRouteId())) {
				Map<String, String> status = new HashMap<>();
				// List<String> status=new ArrayList<String>();
				String isStore = processor.getStoreFlag();
				String isPraise = processor.getPraiseFlag();
				status.put("isStore", isStore);
				status.put("isPraise", isPraise);
				return status;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	@Override
	public List<RouteProcessor> getProcessorPage(String userId, int pageNow, int pageSize) {
		System.out.println("pageNow:" + pageNow);
		System.out.println("pageSize:" + pageSize);
		return dao.getProcessorPage(userId, (pageNow - 1) * pageSize, pageSize);
	}

	@Override
	public String isStore(String userId, String routeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String isPraise(String userId, String routeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteProcessors(String routeId, String userId) {
		return dao.deleteProcessors(routeId, userId) >= 1 ? true : false;
	}
}

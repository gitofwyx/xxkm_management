package com.kelan.riding.story.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelan.riding.story.dao.UserStoryDao;
import com.kelan.riding.story.entity.UserStory;
import com.kelan.riding.story.service.UserStoryService;
@Service
public class UserStoryServiceImpl implements UserStoryService{
	
	@Autowired
	UserStoryDao dao;
	
	@Override
	public UserStory getUserStory(String account){
		return dao.getUserStory(account);
	}
	
	@Override
	public boolean addUserStory(UserStory userStory){
		return dao.addUserStory(userStory) == 1 ? true : false;
		
	}
	
	@Override
	public boolean updateUserStory(UserStory userStory){
		return dao.updateUserStory(userStory) == 1 ? true : false;
	}
	@Override
	public List<UserStory> listUserStory(String userId,int pageNow,int pageSize){
		System.out.println("pageNow:"+pageNow);
		System.out.println("pageSize:"+pageSize);
		return dao.listUserStory(userId, (pageNow-1)*pageSize, pageSize);
	}
	
	@Override
	public List<UserStory> listUserStory(String userId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public UserStory getUserStoryByPoint(String pointId){
		List<UserStory> userstory=dao.getUserStoryByPoint(pointId);
		if(userstory==null||userstory.size()!=1){
			return null;
		}
		return userstory.get(0);
	}

	@Override
	public List<UserStory> getUserStoryByRount(String userId, String routeId) {
		return dao.getUserStoryByRount(userId, routeId);
	}

	
}

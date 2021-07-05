package com.kelan.riding.story.service;

import java.util.List;

import com.kelan.riding.story.entity.UserStory;

public interface UserStoryService {
	
	UserStory getUserStory(String userStoryId);				//根据故事id获取故事
	
	boolean addUserStory(UserStory userStory);				//添加故事
	
	boolean updateUserStory(UserStory userStory);		//根据故事id修改故事
	
	List<UserStory> listUserStory(String userId,int pageNow,int pageSize);//根据id获取故事列表
	
	List<UserStory> listUserStory(String userId);
	
	UserStory getUserStoryByPoint(String pointId);
	
	List<UserStory> getUserStoryByRount(String userId,String routeId);
}

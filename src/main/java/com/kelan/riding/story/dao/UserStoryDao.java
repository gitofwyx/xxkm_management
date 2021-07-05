package com.kelan.riding.story.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.kelan.riding.story.entity.UserStory;

@Repository
public interface UserStoryDao {

	UserStory getUserStory(String userStoryId);				//根据故事id获取故事详情
	
	int addUserStory(UserStory userStory);				//添加故事
	
	int updateUserStory(UserStory userStory);			//根据故事id修改故事
	
	List<UserStory> listUserStory(String userId,int pageStart,int pageSize);		//获取用户故事列表
	
	List<UserStory> getUserStoryByPoint(String pointId);//根据节点获取故事信息
	
	List<UserStory> getUserStoryByRount(String userId,String routeId);//根据节点获取故事信息
}

package com.xxk.xxkm_management.story.service;

import java.util.List;

import com.xxk.xxkm_management.story.entity.UserStoryPic;

public interface UserStoryPicService {
	
	boolean addUserStoryPic(UserStoryPic pic);
	List<UserStoryPic> listUserStoryPic(String userStoryId);		//获取用户故事图片
	UserStoryPic getUserStoryPic(String id);						//根据图片id获取图片
}

package com.kelan.riding.story.service;

import java.util.List;

import com.kelan.riding.story.entity.UserStoryPic;

public interface UserStoryPicService {
	
	boolean addUserStoryPic(UserStoryPic pic);
	List<UserStoryPic> listUserStoryPic(String userStoryId);		//获取用户故事图片
	UserStoryPic getUserStoryPic(String id);						//根据图片id获取图片
}

package com.xxk.xxkm_management.story.dao;

import java.util.List;

import com.xxk.xxkm_management.story.entity.UserStoryPic;

public interface UserStoryPicDao {
	int addUserStoryPic(UserStoryPic pic);					//添加用户故事图片
	List<UserStoryPic> listUserStoryPic(String userStoryId);		//获取用户故事图片
	UserStoryPic getUserStoryPic(String id);		//获取用户故事图片

}

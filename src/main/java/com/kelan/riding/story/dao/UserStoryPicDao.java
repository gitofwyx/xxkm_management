package com.kelan.riding.story.dao;

import java.util.List;

import com.kelan.riding.story.entity.UserStoryPic;

public interface UserStoryPicDao {
	int addUserStoryPic(UserStoryPic pic);					//添加用户故事图片
	List<UserStoryPic> listUserStoryPic(String userStoryId);		//获取用户故事图片
	UserStoryPic getUserStoryPic(String id);		//获取用户故事图片

}

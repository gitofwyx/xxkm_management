package com.kelan.riding.picture.dao;

import java.util.List;

import com.kelan.riding.picture.entity.Picture;
import com.kelan.riding.story.entity.UserStoryPic;

public interface PicDao {
	
	//添加用户头像信息
	int addUserPic(Picture pic);
	
	//获取用户头像信息
	String getUserPic(String id);
	
	//修改用户头像路径
	int updateUserPic(Picture pic);
	
	//用户故事图片添加
	int addUserStoryPic(Picture pic);						
	//获取用户故事图片
	List<Picture> listUserStoryPic(String userStoryId);		
	
	//获取用户故事图片
	UserStoryPic getUserStoryPic(String id);				
	
	//通过线路id获取线路图
	List<Picture> listRoutePic(String routeId);				
	
	//通过用户id获取线路图
	List<Picture> listRoutePicByUserId(String UserId);		
	
	//通过线路id删除线路图
	int deleteRoutePic(String routeId);		
	
	//添加线路图片
	int addRoutePic(Picture pic);
	
}

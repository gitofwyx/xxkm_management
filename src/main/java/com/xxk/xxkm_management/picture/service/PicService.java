package com.xxk.xxkm_management.picture.service;

import java.util.List;

import com.xxk.xxkm_management.picture.entity.Picture;
import com.xxk.xxkm_management.story.entity.UserStoryPic;

public interface PicService {

	boolean addUserPic(Picture pic);
	String getUserPic(String id);
	Boolean updateUserPic(Picture pic);
	boolean addUserStoryPic(Picture pic);		
	List<Picture> listUserStoryPic(String userStoryId);		
	UserStoryPic getUserStoryPic(String id);						
	List<Picture> listRoutePic(String routeId);
	List<Picture> listRoutePicByUserId(String userId);
	boolean deleteRoutePic(String routeId);	
	boolean addRoutePic(Picture pic);
	String getDefaultMap(char last, String rootDIr);
}

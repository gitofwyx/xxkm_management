package com.kelan.riding.story.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelan.riding.story.dao.UserStoryPicDao;
import com.kelan.riding.story.entity.UserStoryPic;
import com.kelan.riding.story.service.UserStoryPicService;
@Service
public class UserStoryPicServiceImpl implements UserStoryPicService{
	
	@Autowired
	private UserStoryPicDao dao;
	
	@Override
	public boolean addUserStoryPic(UserStoryPic pic){
		return dao.addUserStoryPic(pic)==1?true:false;
	}
	@Override
	public List<UserStoryPic> listUserStoryPic(String userStoryId){
		return dao.listUserStoryPic(userStoryId);
	}
	@Override
	public UserStoryPic getUserStoryPic(String id){
		return dao.getUserStoryPic(id);
	}

}

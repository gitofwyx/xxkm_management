package com.kelan.riding.picture.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelan.riding.picture.dao.PicDao;
import com.kelan.riding.picture.entity.Picture;
import com.kelan.riding.picture.service.PicService;
import com.kelan.riding.story.entity.UserStoryPic;

@Service
public class PicServiceImpl implements PicService {

	@Autowired
	private PicDao dao;

	// 用户头像信息保存
	@Override
	public boolean addUserPic(Picture pic) {
		return dao.addUserPic(pic) == 1 ? true : false;
	}

	// 根据用户id获取用户头像
	@Override
	public String getUserPic(String id) {
		return dao.getUserPic(id);
	}

	// 用户故事图片保存
	@Override
	public boolean addUserStoryPic(Picture pic) {
		return dao.addUserStoryPic(pic) == 1 ? true : false;
	}

	// 获取用户故事图片列表
	@Override
	public List<Picture> listUserStoryPic(String userStoryId) {
		return dao.listUserStoryPic(userStoryId);
	}

	// 获取用户故事图片
	@Override
	public UserStoryPic getUserStoryPic(String id) {
		return dao.getUserStoryPic(id);
	}

	// 更新用户头像
	@Override
	public Boolean updateUserPic(Picture pic) {
		return dao.updateUserPic(pic) == 1 ? true : false;
	}

	// 根据routeid获取线路图
	@Override
	public List<Picture> listRoutePic(String routeId) {
		return dao.listRoutePic(routeId);
	}

	// 根据routeid获取线路图
	@Override
	public List<Picture> listRoutePicByUserId(String userId) {
		return dao.listRoutePicByUserId(userId);
	}

	// 根据routeid删除图片
	@Override
	public boolean deleteRoutePic(String routeId) {
		return dao.deleteRoutePic(routeId) == 1 ? true : false;
	}

	// 添加线路图片
	@Override
	public boolean addRoutePic(Picture pic) {
		return dao.addRoutePic(pic) == 1 ? true : false;
	}
	
	//获取内置封面
	@Override
	public String getDefaultMap(char last, String rootDIr) {

		if (last >= '0' && last <= '2') {
			return rootDIr + "/mapimage/假日海滩.jpg";
		} else if (last >= '3' && last <= '5') {
			return rootDIr + "/mapimage/火山口.jpg";
		} else if (last >= '6' && last <= '9') {
			return rootDIr + "/mapimage/金牛岭公园.jpg";
		} else if (last >= 'a' && last <= 'c') {
			return rootDIr + "/mapimage/五公祠.jpg";
		} else if (last >= 'd' && last <= 'f') {
			return rootDIr + "/mapimage/冯小刚电影公社.jpg";
		} else {
			return rootDIr + "/mapimage/观澜湖.jpg.jpg";
		}
	}
}

package com.xxk.xxkm_management.story.entity;

import com.xxk.core.entity.BaseInfoEntity;

public class UserStoryPic extends BaseInfoEntity{

	private String picDir;
	private String objectId;

	public String getPicDir() {
		return picDir;
	}
	public void setPicDir(String picDir) {
		this.picDir = picDir;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	

}

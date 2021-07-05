package com.kelan.riding.picture.entity;

import com.kelan.core.entity.BaseInfoEntity;

public class Picture extends BaseInfoEntity {
	private String picDir;
	private String objectId;
	private String Flag;

	
	
	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getPicDir() {
		return picDir;
	}

	public void setPicDir(String picDir) {
		this.picDir = picDir;
	}

	public String getFlag() {
		return Flag;
	}

	public void setFlag(String flag) {
		Flag = flag;
	}
	

}

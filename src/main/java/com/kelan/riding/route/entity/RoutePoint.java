package com.kelan.riding.route.entity;

import java.io.Serializable;

import com.kelan.core.entity.BaseInfoEntity;

public class RoutePoint extends BaseInfoEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String keyWord;
	private String routeId;
	private String iSave;// 0 不可编辑, 1 立刻编辑,2以后编辑
	private String pType; // '1-起点，2-阶段点，3-终点，4-推荐起点，5-推荐终点,'
	private String pRec;
	private String iType;// '1：风景 2：美食 3：挑战 4：住宿 5：服务点',
	private String createUnit;//
	private String coordinateX;// ` double DEFAULT NULL,
	private String coordinateY;// ` double DEFAULT NULL,
	private String coordinateZ;// ` double DEFAULT NULL,
	private int collectNumber;// ` int(11) DEFAULT NULL,
	private int supportNumber;// ` int(11) DEFAULT NULL,
	private int commentNumber;// ` int(11) DEFAULT NULL,
	private int activeNumber;// ` int(11) DEFAULT NULL,
	private int storyNumber;// ` int(11) DEFAULT NULL,
	private String isEdit;
	private String picId;// ` char(36) DEFAULT NULL,
	private String address;// ` varchar(100) DEFAULT NULL,
	private String remark;// ` varchar(250) DEFAULT NULL,

	public String getiSave() {
		return iSave;
	}

	public void setiSave(String iSave) {
		this.iSave = iSave;
	}

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
	public String getpType() {
		return pType;
	}
	
	public void setpType(String pType) {
		this.pType = pType;
	}
	
	public String getpRec() {
		return pRec;
	}

	public void setpRec(String pRec) {
		this.pRec = pRec;
	}

	public String getiType() {
		return iType;
	}

	public void setiType(String iType) {
		this.iType = iType;
	}

	public String getCreateUnit() {
		return createUnit;
	}

	public void setCreateUnit(String createUnit) {
		this.createUnit = createUnit;
	}

	public String getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(String coordinateX) {
		this.coordinateX = coordinateX;
	}

	public String getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(String coordinateY) {
		this.coordinateY = coordinateY;
	}

	public String getCoordinateZ() {
		return coordinateZ;
	}

	public void setCoordinateZ(String coordinateZ) {
		this.coordinateZ = coordinateZ;
	}

	public int getCollectNumber() {
		return collectNumber;
	}

	public void setCollectNumber(int collectNumber) {
		this.collectNumber = collectNumber;
	}

	public int getSupportNumber() {
		return supportNumber;
	}

	public void setSupportNumber(int supportNumber) {
		this.supportNumber = supportNumber;
	}

	public int getCommentNumber() {
		return commentNumber;
	}

	public void setCommentNumber(int commentNumber) {
		this.commentNumber = commentNumber;
	}

	public int getActiveNumber() {
		return activeNumber;
	}

	public void setActiveNumber(int activeNumber) {
		this.activeNumber = activeNumber;
	}

	public int getStoryNumber() {
		return storyNumber;
	}

	public void setStoryNumber(int storyNumber) {
		this.storyNumber = storyNumber;
	}

	public String getPicId() {
		return picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "RoutePoint [name=" + name + ", keyWord=" + keyWord + ", routeId=" + routeId + ", iSave=" + iSave
				+ ", pType=" + pType + ", iType=" + iType + ", createUnit=" + createUnit + ", coordinateX="
				+ coordinateX + ", coordinateY=" + coordinateY + ", coordinateZ=" + coordinateZ + ", collectNumber="
				+ collectNumber + ", supportNumber=" + supportNumber + ", commentNumber=" + commentNumber
				+ ", activeNumber=" + activeNumber + ", storyNumber=" + storyNumber + ", isEdit=" + isEdit + ", picId="
				+ picId + ", address=" + address + ", remark=" + remark + "]";
	}

	
}

package com.kelan.riding.story.entity;

import java.util.Arrays;

import com.kelan.core.entity.BaseInfoEntity;

public class UserStory  extends BaseInfoEntity {
	private String keyWord;			//关键字
	private String type = "1";			//故事类别（默认值1）
	private String cType = "0";			//水平类别（0-普通，1-加星推荐）
	private String provinceCode;	//省份代码
	private String cityCode;		//城市代码
	private String areaCode;		//地区代码
	private String createUnit;		//归属单位
	private int readNumber;			//阅读量
	private int collectNumber;		//收藏人数
	private int supportNumber;		//点赞人数
	private int commentNumber;		//评论数
	private String picId;			//图片
	private String address;			//地址
	private String remark;			//备注
	private byte[] dataInfo;
	
	private String routeId;			//2016.11.4新增字段，故事所属线路
	private String pointId;			//2016.11.4新增字段，故事所属节点
	private double longitude;		//2016.11.4新增字段，节点经度
	private double latitude;		//2016.11.4新增字段，节点纬度
	private String title;			//2016.11.4新增字段，故事标题

	
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getPointId() {
		return pointId;
	}
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getcType() {
		return cType;
	}
	public void setcType(String cType) {
		this.cType = cType;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getCreateUnit() {
		return createUnit;
	}
	public void setCreateUnit(String createUnit) {
		this.createUnit = createUnit;
	}
	public int getReadNumber() {
		return readNumber;
	}
	public void setReadNumber(int readNumber) {
		this.readNumber = readNumber;
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

	public byte[] getDataInfo() {
		return dataInfo;
	}

	public void setDataInfo(byte[] dataInfo) {
		this.dataInfo = dataInfo;
	}
	
}

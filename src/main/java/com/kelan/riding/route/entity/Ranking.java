package com.kelan.riding.route.entity;

import com.kelan.core.entity.BaseInfoEntity;

public class Ranking extends BaseInfoEntity{
	
	private String routeId;
	private String keyWord;
	private String activeId;
	private String createUnit;
	private String beginTime;
	private String joinPerson;
	private String duration;//持续时间
	private float averageSpeed;//平均速度
	private float presentspeedH;
	private float presentspeedL;
	private String picId;
	private String address;
	private String remark;
	private String percent;//完成的百分比
	
	
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getActiveId() {
		return activeId;
	}
	public void setActiveId(String activeId) {
		this.activeId = activeId;
	}
	public String getCreateUnit() {
		return createUnit;
	}
	public void setCreateUnit(String createUnit) {
		this.createUnit = createUnit;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getJoinPerson() {
		return joinPerson;
	}
	public void setJoinPerson(String joinPerson) {
		this.joinPerson = joinPerson;
	}
	
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public float getAverageSpeed() {
		return averageSpeed;
	}
	public void setAverageSpeed(float averageSpeed) {
		this.averageSpeed = averageSpeed;
	}
	public float getPresentspeedH() {
		return presentspeedH;
	}
	public void setPresentspeedH(float presentspeedH) {
		this.presentspeedH = presentspeedH;
	}
	public float getPresentspeedL() {
		return presentspeedL;
	}
	public void setPresentspeedL(float presentspeedL) {
		this.presentspeedL = presentspeedL;
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
	
	

}

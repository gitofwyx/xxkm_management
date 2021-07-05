package com.kelan.riding.route.entity;

import com.kelan.core.entity.BaseInfoEntity;

public class Record extends BaseInfoEntity {
	private String routeId;
	private String keyWord;
	private String activeId;
	private String createUnit;
	private String coordinateX;
	private String coordinateY;
	private String coordinateZ;
	private String beginTime;
	private String endTime;
	private String joinPerson;
	private String extent;//里程(挑战用户已完成的路线长度)
	private String duration;//耗时
	private String averageSpeed;//平均速度
	private float presentspeedH;
	private float presentspeedL;
	private String collectFlag;
	private String address;
	private String remark;
	private String percent;//完成的百分比

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

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getJoinPerson() {
		return joinPerson;
	}

	public void setJoinPerson(String joinPerson) {
		this.joinPerson = joinPerson;
	}
	
	public String getExtent() {
		return extent;
	}

	public void setExtent(String extent) {
		this.extent = extent;
	}
	
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(String averageSpeed) {
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

	public String getCollectFlag() {
		return collectFlag;
	}

	public void setCollectFlag(String collectFlag) {
		this.collectFlag = collectFlag;
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

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

}

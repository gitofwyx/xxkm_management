package com.kelan.riding.route.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.kelan.core.entity.BaseInfoEntity;

@Document
public class Route extends BaseInfoEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String name;//线路名
	private String mileage;//线路里程(全长)
	private String rank;
	private int integral;
	private int userNumber;
	private int collectNumber;//收藏数
	private int supportNumber;//点赞数
	private int activeNumber;
	private int storyNumber;
	private int commentNumber;
	private String keyWord;
	private String type;// '1-景点线路，2-竞速线路，3-挑战者线路',
	private String cId;
	private String createUnit;
	private String provinceCode;
	private String cityCode;
	private String areaCode;
	// private String dataInfo` blob,
	private String picId;
	private String remark;
	@Field
	private List<RoutePoint> points;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public int getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(int userNumber) {
		this.userNumber = userNumber;
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

	public int getCommentNumber() {
		return commentNumber;
	}

	public void setCommentNumber(int commentNumber) {
		this.commentNumber = commentNumber;
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
	
	public String getcId() {
		return cId;
	}

	public void setcId(String cId) {
		this.cId = cId;
	}

	public String getCreateUnit() {
		return createUnit;
	}

	public void setCreateUnit(String createUnit) {
		this.createUnit = createUnit;
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

	public String getPicId() {
		return picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<RoutePoint> getPoints() {
		return points;
	}	

	public void setPoints(List<RoutePoint> points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "Route [id=" + id + ", name=" + name + ", rank=" + rank + ", integral=" + integral + ", userNumber="
				+ userNumber + ", collectNumber=" + collectNumber + ", supportNumber=" + supportNumber
				+ ", activeNumber=" + activeNumber + ", storyNumber=" + storyNumber + ", commentNumber=" + commentNumber
				+ ", keyWord=" + keyWord + ", type=" + type + ", cId=" + cId + ", createUnit=" + createUnit
				+ ", provinceCode=" + provinceCode + ", cityCode=" + cityCode + ", areaCode=" + areaCode + ", picId="
				+ picId + ", remark=" + remark + ", points=" + points + ", createDate=" + createDate + ", createUserId="
				+ createUserId + ", updateDate=" + updateDate + ", updateUserId=" + updateUserId + ", deleteFlag="
				+ deleteFlag + ", getId()=" + getId() + ", getName()=" + getName() + ", getRank()=" + getRank()
				+ ", getIntegral()=" + getIntegral() + ", getUserNumber()=" + getUserNumber() + ", getCollectNumber()="
				+ getCollectNumber() + ", getSupportNumber()=" + getSupportNumber() + ", getActiveNumber()="
				+ getActiveNumber() + ", getStoryNumber()=" + getStoryNumber() + ", getCommentNumber()="
				+ getCommentNumber() + ", getKeyWord()=" + getKeyWord() + ", getType()=" + getType() + ", getcId()="
				+ getcId() + ", getCreateUnit()=" + getCreateUnit() + ", getProvinceCode()=" + getProvinceCode()
				+ ", getCityCode()=" + getCityCode() + ", getAreaCode()=" + getAreaCode() + ", getPicId()=" + getPicId()
				+ ", getRemark()=" + getRemark() + ", getPoints()=" + getPoints() + ", getCreateDate()="
				+ getCreateDate() + ", getCreateUserId()=" + getCreateUserId() + ", getUpdateDate()=" + getUpdateDate()
				+ ", getUpdateUserId()=" + getUpdateUserId() + ", getDeleteFlag()=" + getDeleteFlag() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}

package com.kelan.riding.user.entity;

import java.util.Arrays;

import com.kelan.core.entity.BaseInfoEntity;

public class User extends BaseInfoEntity {
	private String name;
	private String birth;
	private String sex;// '0:女 1：男',
	private String nickName;
	private int integral;//
	private String rank;
	private String qq;
	private String phone;
	private String education;
	private String marriage;// '0:未婚 1：已婚',
	private String roleId;// '1-用户，3-商家，5-平台管理，9-超级管理员',
	private String createUnit;//归属单位
	private String provinceCode;
	private String cityCode;
	private String areaCode;
	private String type;
	private String remark;
	private String address;
	private String account;
	private String password;
	private byte[] dataInfo;
	private String keyWord;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getDataInfo() {
		return dataInfo;
	}

	public void setDataInfo(byte[] dataInfo) {
		this.dataInfo = dataInfo;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	@Override
	public String toString() {
		return "[{\"name\":\"" + name + "\",\"birth\":\"" + birth + "\",\"sex\":\"" + sex + "\",\"nickName\":\""
				+ nickName + "\",\"integral\":\"" + integral + "\",\"rank\":\"" + rank + "\",\"qq\":\"" + qq
				+ "\",\"phone\":\"" + phone + "\",\"education\":\"" + education + "\",\"marriage\":\"" + marriage
				+ "\",\"roleId\":\"" + roleId + "\",\"createUnit\":\"" + createUnit + "\",\"provinceCode\":\""
				+ provinceCode + "\",\"cityCode\":\"" + cityCode + "\",\"areaCode\":\"" + areaCode + "\",\"type\":\""
				+ type + "\",\"remark\":\"" + remark + "\",\"address\":\"" + address + "\",\"account\":\"" + account
				+ "\",\"password\":\"" + password + "\",\"dataInfo\":\"" + Arrays.toString(dataInfo)
				+ "\",\"keyWord\":\"" + keyWord + "\",\"createDate\":\"" + createDate + "\",\"createUserId\":\""
				+ createUserId + "\",\"updateDate\":\"" + updateDate + "\",\"updateUserId\":\"" + updateUserId
				+ "\",\"deleteFlag\":\"" + deleteFlag + "\",\"id\":\"" + id + "\"} ]";
	}

}

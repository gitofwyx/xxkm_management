package com.kelan.core.file;

/**
 * 功能：图片信息类
 * 
 * @author 吴晓敏
 *
 */
public class ImageInfo {
	private String imgName; // 图片名称
	private String imgPath; // 图片存储地址
	private String miniImgPath; // 缩略图存储地址
	private String isCut; // 是否缩略[0:否][1:是]
	private String isError; // 是否上传失败[0:否][1:是]
	private String msg; // 上传失败消息

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getMiniImgPath() {
		return miniImgPath;
	}

	public void setMiniImgPath(String miniImgPath) {
		this.miniImgPath = miniImgPath;
	}

	public String getIsCut() {
		return isCut;
	}

	public void setIsCut(String isCut) {
		this.isCut = isCut;
	}

	public String getIsError() {
		return isError;
	}

	public void setIsError(String isError) {
		this.isError = isError;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}

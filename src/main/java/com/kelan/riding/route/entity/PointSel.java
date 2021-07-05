package com.kelan.riding.route.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取节点列表打包类                                                                                                                                                            
 * @author WYX
 *
 */
public class PointSel{
	
	private String pointId;//节点ID
	List<String> pointpic = new ArrayList<>();//节点对应的故事图片列表
	private String title;//节点对应的故事标题
	
	public String getPointId() {
		return pointId;
	}
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}
	public List<String> getPointpic() {
		return pointpic;
	}
	public void setPointpic(String pointpic) {
		this.pointpic.add(pointpic);
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
}

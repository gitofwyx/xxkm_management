package com.kelan.riding.upload.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kelan.core.file.BaseController;
import com.kelan.core.file.ImageInfo;

/**
 * 文件上传控制器
 * 
 * @author 吴晓敏
 *
 */
@Controller
@RequestMapping(value = "uploadFile")
public class UploadFileController extends BaseController {

	/**
	 * 单个图片文件上传，若文件大小超过允许最大大小，则生成压缩图片（压缩比例默认0.25，图片质量默认0.25）
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "uploadImage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadImage(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) {
		Map<String, Object> result = new HashMap<String, Object>(); // 返回结果集
		try {
			Map<String, String> upresult = uploadFile(file,request);
			
			if ("0".equals(upresult.get("isError"))) {
				String picUrl = upresult.get("picUrl");
				result.put("fileName", picUrl);
				
				result.put("code", "0000");
				result.put("msg", "上传成功");
			} else {
				result.put("code", "0001");
				result.put("msg", result.get("msg"));
			}
		} catch (Exception e) {
			result.put("code", "1111");
			result.put("msg", "上传失败");
		}
		return result;
	}

	/**
	 * 多个图片文件上传，若文件大小超过允许最大大小，则生成压缩图片
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "uploadImages", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadImages(MultipartHttpServletRequest request, HttpServletResponse reponse) {
		// 定义返回json格式的Map数据
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			Map<String, Object> result = uploadFile(request);
			
			if ("0".equals(result.get("isError"))) {
				@SuppressWarnings("unchecked")
				List<ImageInfo> nameList = (List<ImageInfo>) result.get("nameList");
				resultMap.put("nameList", nameList);
				
				resultMap.put("code", "0000");
				resultMap.put("msg", "上传成功");
			} else {
				resultMap.put("code", "0001");
				resultMap.put("msg", result.get("msg"));
			}
		} catch (Exception e) {
			resultMap.put("code", "1111");
			resultMap.put("msg", "上传失败");
		}
		
		return resultMap;
	}
}
package com.kelan.riding.user.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.kelan.core.file.BaseController;
import com.kelan.core.file.ImageInfo;
import com.kelan.core.util.DateUtil;
import com.kelan.core.util.InetAddressUtil;
import com.kelan.core.util.MySessionContext;
import com.kelan.core.util.StringUtil;
import com.kelan.core.util.UUIdUtil;
import com.kelan.riding.picture.entity.Picture;
import com.kelan.riding.picture.service.PicService;
import com.kelan.riding.roles.entity.Roles;
import com.kelan.riding.roles.service.RolesService;
import com.kelan.riding.user.entity.User;
import com.kelan.riding.user.service.UserService;

@Controller
public class UserController extends BaseController {
	private static Logger log = Logger.getLogger(UserController.class);
	@Autowired
	private PicService picService;
	@Autowired
	private UserService userService;
	@Autowired
	private RolesService rolesService;
	
	private String picurl = "http://"+InetAddressUtil.IP+":8080";

	@ResponseBody
	@Scope("prototype")
	@RequestMapping(value = "/regist", method = RequestMethod.POST)//用户注册路径
	public Map<String, String> regist(User user, HttpServletRequest request,
			@RequestParam(value = "userPic", required = false) MultipartFile userPic) {
		Map<String, String> result = new HashMap<>();
		String createDate = DateUtil.getFullTime();
		String id = UUIdUtil.getUUID();
		String picId = UUIdUtil.getUUID();
		String realPath = request.getSession().getServletContext().getRealPath("");
		Picture pic = new Picture();
		try {
			if (!StringUtil.isMobilePhone(user.getPhone(), true)) {
				result.put("3", "手机号格式错误");
				System.out.println(result);
				return result;
			}
			if (user.getAccount() == null) {
				user.setAccount(user.getPhone());
			}
			if (!userService.checkUserUnique(user.getAccount(), user.getPhone())) {
				result.put("2", "注册信息已存在");
				System.out.println(result);
				return result;
			}
			if (userPic != null && !(userPic.isEmpty()) && userPic.getSize() > 0) {
				ImageInfo imgInf = uploadImage(userPic, realPath);
				
				if (imgInf.getMsg() == null) {
					pic.setPicDir(imgInf.getImgPath());
				} else {
					throw new RuntimeException(imgInf.getMsg());
				}

				pic.setId(picId);
				pic.setCreateDate(createDate);
				pic.setCreateUserId(id);
				pic.setUpdateDate(createDate);
				pic.setUpdateUserId(id);
				pic.setDeleteFlag("0");

				boolean picResult = picService.addUserPic(pic);

				if (!(picResult)) {
					throw new RuntimeException("图片上传失败!");
				} else {
					log.info(">>>>用户图片上传成功");
				}

			}
		} catch (Exception e) {
			System.out.println(e);
			log.info(">>>>用户图片上传失败");
			result.put("msg", "系统错误");
			//System.out.println(result);
			return result;
		}

		user.setId(id);
		user.setRoleId(rolesService.getUserRoleId("user"));
		user.setCreateDate(createDate);
		user.setUpdateDate(createDate);
		user.setDeleteFlag("0");
		user.setName("");
		user.setAddress("");
		user.setBirth("");
		user.setQq("");
		user.setSex("");
		boolean userResult = userService.addUser(user);

		try {
			if (!(userResult)) {
				throw new RuntimeException("注册失败!");
			} else {
				log.info(">>>>注册成功");
				result.put("0", "注册成功");
				System.out.println(result);
				return result;
			}
		} catch (Exception e) {
			log.info(">>>>注册失败");
			result.put("msg", "注册失败");
			System.out.println(result);
			return result;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/checkUserUnique", method = RequestMethod.GET)//注册检验：登录名和手机号是否唯一
	public Map<String, String> checkUserUnique(@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "phone", required = false) String phone) {
		Map<String, String> result = new HashMap<>();
		try {
			if (account == null) {
				account = "";
			}
			if (phone == null) {
				phone = "";
			}
			if ("".equals(account) && "".equals(phone)) {
				result.put("3", "输入为空");
				System.out.println(result);
				return result;
			}
			if (userService.checkUserUnique(account, phone)) {
				result.put("0", "该账号可用");
			} else {
				result.put("1", "注册账号已存在");
			}
		} catch (Exception e) {
			System.out.println(e);
			result.put("2", "查询失败");
		}
		System.out.println(result);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)//用户登录路径
	public Map<String, String> login(@RequestParam("userName") String userName,
			@RequestParam("passWord") String passWord,HttpServletRequest request,HttpServletResponse response) {

		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		Map<String, String> result = new HashMap<>();
		if (!currentUser.isAuthenticated()) {
			UsernamePasswordToken token = null;
			if (StringUtil.isMobilePhone(userName, true)) {
				userName = userService.gatAccountByPhone(userName);
			}
			if (userName != null && !"".equals(userName)) {
				token = new UsernamePasswordToken(userName, passWord);// 获取登录令牌
				try {
					token.setRememberMe(true);
					currentUser.login(token);
					result.put("0", "已登录");
					session.setAttribute("account", userName);
					response.setHeader("Set-Cookie", "JSESSIONID="+request.getSession().getId()+"; Path=/; HttpOnly");
					request.getSession().setMaxInactiveInterval((int)session.getTimeout());
					MySessionContext.AddSession(request.getSession());
					
				} catch (UnknownAccountException uae) {
					log.info("There is no user with username of " + token.getPrincipal());
					result.put("1", "没有帐号");
				} catch (IncorrectCredentialsException ice) {
					log.info("Password for account " + token.getPrincipal() + " was incorrect!");
					result.put("2", "密码不正确");
				} catch (AuthenticationException ae) {
					log.info("some error unknown accure");
					result.put("3", "系统错误");
				}
			} else {
				result.put("1", "没有帐号");
			}
		} else {
			result.put("4", "已登录账号"+session.getAttribute("account")+",重新登录请先注销");
		}
		return result;

	}

	@ResponseBody
	@RequestMapping(value = "/getUserById"/*, method = RequestMethod.POST*/)//根据id获取用户信息
	public Map<Object, Object> getUserById(HttpServletRequest request) {
		Subject currentUser = SecurityUtils.getSubject();
		Map<Object, Object> result = new HashMap<>();
		User user = null;
		String userId = "";
		if (currentUser.isAuthenticated()) {
			Session session = currentUser.getSession();
			userId = (String) session.getAttribute("userId");
			// System.out.println("userId:" + userId);
			try {
				user = userService.getUserById(userId);// 根据用户名获取用户实体信息
				String picDir =picurl + picService.getUserPic(user.getId());
				//JSONObject JObject = JsonUtils.toJSON(user);
				// String forUser = user.toString();
				result.put("msg", "获取成功");
				result.put("user", user);
				result.put("picdir", picDir);
				log.info(picDir);
			} catch (Exception e) {
				System.out.println(e);
				log.error("获取用户信息失败");
				result.put("msg", "获取失败");
			}
		}
		//System.out.println(result);
		// JSONObject jsonObject = JSONObject.fromMap(productMap);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST) // 修改用户信息
	public Map<String, String> updateUser(User user, HttpServletRequest request,
			@RequestParam(value = "userPic", required = false) MultipartFile userPic) {
		Subject currentUser = SecurityUtils.getSubject();
		Map<String, String> result = new HashMap<>();
		String updateDate = DateUtil.getFullTime();
		String id = user.getId();
		String picId = UUIdUtil.getUUID();
		String realPath = request.getSession().getServletContext().getRealPath("");
		Picture pic = new Picture();
		try {
			if (userPic != null && !(userPic.isEmpty()) && userPic.getSize() > 0) {
				ImageInfo imgInf = uploadImage(userPic, realPath);

				if (imgInf.getMsg() == null) {
					pic.setPicDir(imgInf.getImgPath());
				} else {
					throw new RuntimeException(imgInf.getMsg());
				}

				pic.setId(picId);
				pic.setCreateUserId(id);
				pic.setUpdateDate(updateDate);
				pic.setUpdateUserId(id);
				pic.setDeleteFlag("0");

				boolean picResult = picService.addUserPic(pic);

				if (!(picResult)) {
					throw new RuntimeException("图片上传失败!");
				} else {
					log.info(">>>>用户图片上传成功");
				}
			} else {

			}
		} catch (Exception e) {
			log.info(">>>>用户图片上传失败");
			// result.put("isError", "2");
			result.put("2", "图片上传失败");
			System.out.println(result);
			return result;
		}
		user.setAccount((String) currentUser.getSession().getAttribute("account"));
		user.setUpdateDate(updateDate);// 用户信息设置

		boolean userResult = userService.updateUser(user);
		try {
			if (!(userResult)) {
				throw new RuntimeException("修改失败!");
			} else {
				log.info(">>>>修改成功");
				// result.put("isError", "0");
				result.put("0", "修改成功");
				System.out.println(result);
				return result;
			}
		} catch (Exception e) {
			log.info(">>>>修改失败");
			// result.put("isError", "1");
			result.put("1", "修改失败");
			System.out.println(result);
			return result;
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/updatePassword") // 修改用户信息
	public Map<String, String> updatePassword(HttpServletRequest request, @RequestParam(value = "passWord") String password,
			@RequestParam(value = "lastkey", required = false) String lastkey) {
		Subject currentUser = SecurityUtils.getSubject();
		Map<String, String> result = new HashMap<>();
		String id=(String) currentUser.getSession().getAttribute("userId");
		String username=(String) currentUser.getSession().getAttribute("account");
		//String realPath = request.getSession().getServletContext().getRealPath("");
		boolean userResult=false;
		UsernamePasswordToken token = null;
		try {
			if(id!=null&&!"".equals(id)){
				if (username != null && !"".equals(username)) {
					token = new UsernamePasswordToken(username, lastkey);
					currentUser.login(token);
				}
				userResult = userService.updatePassword(id, password);
				if(userResult){
					result.put("0", "修改密码完成");
				}else{
					result.put("1", "修改密码失败");
				}
			}else if(request.getParameter("userName")!=null&&!"".equals(request.getParameter("userName"))){
				username=(String)request.getParameter("userName");
				if (StringUtil.isMobilePhone(username, true)) {
					String account=userService.gatAccountByPhone(username);
					if(account!=null&&!"".equals(account)){
						userResult =userService.forgetPassword(username, password);
						if(userResult){
							result.put("0", "修改密码完成");
						}else{
							result.put("1", "修改密码失败");
						}
					}else{result.put("0", "没有相关账号信息");}
				}else{result.put("0", "账号格式错误");}
			}
			else {
				result.put("2", "密码为空");
			}
		} catch (UnknownAccountException uae) {
			log.info("There is no user with username of " + token.getPrincipal());
			result.put("4", "系统错误");
		} catch (IncorrectCredentialsException ice) {
			log.info("Password for account " + token.getPrincipal() + " was incorrect!");
			result.put("3", "原始密码不正确");
		} catch (AuthenticationException ae) {
			log.info("some error unknown accure");
			result.put("4", "系统错误");
		} 
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/addUserPic", method = RequestMethod.POST)//添加、更新头像信息
	public Map<String, Object> addUserPic(MultipartHttpServletRequest request, HttpServletResponse response) {
		Subject currentUser = SecurityUtils.getSubject();
		// Map<String, Object> result = new HashMap<>();
		String id = (String) currentUser.getSession().getAttribute("userId");
		String createDate = DateUtil.getFullTime(); // 生成创建时间
		String picDir = "";
		Map<String, Object> result = uploadFile(request);
		try {
			if (id != null && !"".equals(id)) {
				picDir = picService.getUserPic(id);
			} else {
				result.put("msg", "没有登录");
				return result;
			}

			boolean picResult = false;
			if ("0".equals(result.get("isError"))) {
				@SuppressWarnings("unchecked")
				List<ImageInfo> nameList = (List<ImageInfo>) result.get("nameList");
				result.put("picurl", picurl+nameList.get(0).getMiniImgPath());
				result.put("code", "0000");
				result.put("msg", "上传成功");
				System.out.println("大小" + nameList.size());
				// System.out.println("lalal" + result.toString());
				// 将图片信息写入到数据库
				System.out.println("aa" + result);
				Picture pic = new Picture();
				pic.setPicDir(nameList.get(0).getMiniImgPath());
				if (picDir == null || "".equals(picDir)) {
					pic.setId(UUIdUtil.getUUID());
					pic.setObjectId(id);
					id = UUIdUtil.getUUID();
					pic.setCreateDate(createDate);
					pic.setCreateUserId(id);
					pic.setDeleteFlag("0");
					picResult = picService.addUserPic(pic);
				}
				pic.setObjectId(id);
				pic.setUpdateDate(createDate);
				pic.setUpdateUserId(id);
				picResult = picService.updateUserPic(pic);
				if (picResult) {
					
					log.info("保存用户头像成功");
				} else {
					log.info("保存用户头像失败");
				}
			} else {
				result.put("code", "0001");
				result.put("msg", result.get("msg"));
			}
		} catch (Exception e) {
			result.put("code", "1111");
			System.out.println(e);
			result.put("msg", "上传失败");
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/addRoles", method = RequestMethod.GET) // 添加角色信息
	public Map<String, String> addRoles(HttpServletRequest request) {
		// Subject currentUser = SecurityUtils.getSubject();
		Map<String, String> result = new HashMap<>();
		Roles role = new Roles();
		String createDate = DateUtil.getFullTime();
		String id = UUIdUtil.getUUID();
		InputStreamReader ir; // 定义一个字符输入流
		BufferedReader in; // 一个字符缓冲流
		ir = new InputStreamReader(System.in); // ir 接收从键盘得到的字符
		in = new BufferedReader(ir); // 对IR进行包装
		System.out.print("role x is :");
		try {
			String roleVal = in.readLine();
			System.out.println(roleVal);
			role.setId(id);
			role.setRoleVal(roleVal);
			role.setDeleteFlag("0");
			role.setCreateDate(createDate);
			role.setUpdateDate(createDate);
			if (rolesService.addUserRole(role)) {
				result.put("0", "添加角色成功");
				log.info("添加角色成功");
			} else {
				result.put("1", "添加角色失败");
				log.error("添加角色失败");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// int x=Integer.parseInt(s); //把得到字符串转化为整型
		catch (Exception e) {
			System.out.println(e);
			log.info(">>>>添加角色失败");
			result.put("isError", "1");
			result.put("1", "添加角色失败");
			System.out.println(result);
			return result;
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/loginDenied") //用户登录权限跳转路径
	public Map<String, String> loginDenied(HttpServletRequest request,HttpServletResponse Response) {
		Subject currentUser = SecurityUtils.getSubject();
		Map<String, String> result=new HashMap<>();
		try {
			if(!currentUser.isAuthenticated()){
				result.put("msg", "用户没登录1");
			}else{
			}
		} 
		catch (Exception e) {
			log.info(e);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/unAuthorized") //用户角色权限跳转路径
	public Map<String, String> unAuthorized(HttpServletRequest request,HttpServletResponse Response) {
		//Subject currentUser = SecurityUtils.getSubject();
		Map<String, String> result=new HashMap<>();
		try {
			result.put("msg", "没有权限");
		} 
		catch (Exception e) {
			log.info(e);
		}
		return result;
	}
	
	@ResponseBody
	@RequestMapping(value = "/kickOutUser") //用户角色权限跳转路径
	public Map<String, String> kickOutUser(HttpServletRequest request,HttpServletResponse Response) {
		//Subject currentUser = SecurityUtils.getSubject();
		Map<String, String> result=new HashMap<>();
		try {
			result.put("msg", "该用户已异地登录,请重新登录,如不是本人请修改密码");
		} 
		catch (Exception e) {
			log.info(e);
		}
		return result;
	}
}

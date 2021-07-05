package com.kelan.core.interceptor;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kelan.core.shiro.CustomFormAuthenticationFilter;
import com.kelan.core.util.MD5Util;

/**
 * 请求校验拦截器
 * 
 * @author 王骏
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	private static Logger log = Logger.getLogger(CustomFormAuthenticationFilter.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// if(handler instanceof HandlerMethod){
		// HandlerMethod handlerMethod = (HandlerMethod)handler;
		// MethodParameter[] methodParameters =
		// handlerMethod.getMethodParameters();
		// for(MethodParameter methodParameter : methodParameters){
		// System.out.println(methodParameter.getParameterName());
		// }
		// }

		// 自动登录
		// Subject subject = ShiroKit.getSubject(ai.getController()
		// .getRequest().getServletContext());
		//
		// if(!subject.isAuthenticated() && subject.isRemembered()){
		// User user =
		// User.dao.getUserByEmail(subject.getPrincipal().toString());
		// subject.login(new UsernamePasswordToken(user.getStr(UserKey.EAMIL),
		// user.getStr(UserKey.PASSWORD), true,
		// ai.getController().getRequest().getRemoteAddr()));
		//
		// Session session = subject.getSession(false);
		// session.setAttribute(MyConstants.SESSION_USER_NAME, user);
		// }
		log.info(">>>拦截器状态：已获取URI-> " + request.getRequestURI() + " ID地址-> " + request.getRemoteAddr());
		//System.out.println("Hcode:"+request.getAttribute("Hcode"));
		Map<String, String[]> prammap = request.getParameterMap();// 接收请求参数
		Map<String, String> sortedMap = new TreeMap<String, String>();// 定义排序map
		String Token = "";
		String MD5Token = "";
		// 将接收到的map值转换成String类型并通过TreeMap排序
		for (String key : prammap.keySet()) {

			String[] pramchar = prammap.get(key);
			StringBuffer pramBuffer = new StringBuffer();   
			for (int i = 0; i < pramchar.length; i++) {
				pramBuffer.append(pramchar[i]);
			}
			String pram = pramBuffer.toString();
			// 筛选到APPtoken
			if (key.equals("token")) {
				MD5Token = pram;
				continue;
			}
			sortedMap.put(key, pram);
		}
		// 生成本地的token字符串
		for (String key : sortedMap.keySet()) {
			String value = sortedMap.get(key);
			Token += key + "=" + value + "&";
		}
		// 通过MD5加密token
		if (!"".equals(Token)) {
			Token += "kelansoft";
			System.out.println(Token);
			byte[] token = Token.getBytes("UTF-8");// 加密前注意编码格式，最好用getBytes转换
			Token = MD5Util.getMD5(token);// 加密
			// System.out.println("本地："+Token);
			// System.out.println("APP："+MD5Token);

			// 与获取到的token进行比对
			if (!Token.equals(MD5Token)) {
				request.setAttribute("msg", "签名错误");
				// response.sendRedirect("/unAuthorized");
				// request.getRequestDispatcher("/unAuthorized").forward(request,
				// response);
				log.warn(">>>拦截器拦截到非法签名！\n");
				return true;
			} else if (Token.equals(MD5Token)) {
				log.info(">>>拦截器拦截验证通过！");
			}
		}
		log.info(">>>拦截器状态：处理完成");
		return true;
	}

	// public static void main(String[] args) {
	// // TODO Auto-generated method stub
	// String str =
	// MD5Util.getMd5("address=M78星云birth=1990年3月27日name=迪迦奥特曼qq=586942682sex=男kelansoft");
	// System.out.println(str);
	// System.out.println(str.equals("7366238c66f2922a3c298fc9f64edffe"));
	// System.out.println(UUIdUtil.getUUID());
	// }
}

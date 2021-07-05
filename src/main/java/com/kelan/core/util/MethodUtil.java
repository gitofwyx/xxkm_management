package com.kelan.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class MethodUtil {

	private final static Logger logger = Logger.getLogger(MethodUtil.class);

	/**
	 * 
	 * java反射bean的get方法
	 * 
	 * 
	 * 
	 * @param objectClass
	 * 
	 * @param fieldName
	 * 
	 * @return
	 * 
	 */

	@SuppressWarnings("unchecked")

	public static Method getGetMethod(Class objectClass, String fieldName) {

		StringBuffer sb = new StringBuffer();

		sb.append("get");

		sb.append(fieldName.substring(0, 1).toUpperCase());

		sb.append(fieldName.substring(1));

		try {

			return objectClass.getMethod(sb.toString());

		} catch (Exception e) {

		}

		return null;

	}

	/**
	 * 根据set获取get方法
	 * 
	 * @param sFieldName
	 * @param objectClass
	 * @return getMethod
	 */
	@SuppressWarnings("unchecked")

	public static Method getGetMethod(String sFieldName, Class objectClass) {

		StringBuffer sb = new StringBuffer();

		sb.append(sFieldName.substring(0, 1).replaceFirst("s", "g"));

		sb.append(sFieldName.substring(1));

		try {

			return objectClass.getMethod(sb.toString());

		} catch (Exception e) {

		}

		return null;

	}

	/**
	 * 
	 * java反射bean的set方法
	 * 
	 * 
	 * 
	 * @param objectClass
	 * 
	 * @param fieldName
	 * 
	 * @return
	 * 
	 */

	@SuppressWarnings("unchecked")

	public static Method getSetMethod(Class objectClass, String fieldName) {

		try {

			Class[] parameterTypes = new Class[1];
			Field field = objectClass.getDeclaredField(fieldName);

			parameterTypes[0] = field.getType();

			StringBuffer sb = new StringBuffer();

			sb.append("set");

			sb.append(fieldName.substring(0, 1).toUpperCase());

			sb.append(fieldName.substring(1));

			Method method = objectClass.getMethod(sb.toString(), parameterTypes);

			return method;

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}
	
	/**
	 * 根据class类名获取函数名和函数
	 * @param objectClass
	 * @return map("函数名"，Method)
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })

	public static Map<String, Method> getSetMethod(Class objectClass) {
		Map<String, Method> MethodMap = new HashMap();
		String[] fieldName = null;
		Field[] fields = objectClass.getDeclaredFields();
		for (Field field : fields) {
			try {
				Class[] parameterTypes = new Class[1];
				parameterTypes[0] = field.getType();
				StringBuffer sb = new StringBuffer();
				fieldName = field.toString().split("\\.");
				fieldName[0] = fieldName[fieldName.length - 1];
				sb.append("set");
				if(Character.isUpperCase(fieldName[0].substring(1, 2).charAt(0))){
					sb.append(fieldName[0].substring(0, 1));
				}else{
					sb.append(fieldName[0].substring(0, 1).toUpperCase());
				}
				sb.append(fieldName[0].substring(1));
				// sb.append(field.toString().substring(1));

				Method method = objectClass.getDeclaredMethod(sb.toString(), parameterTypes);
				MethodMap.put(sb.toString(), method);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return MethodMap;
	}

	/**
	 * 
	 * 执行set方法
	 * 
	 * 
	 * 
	 * @param o执行对象
	 * 
	 * @param fieldName属性
	 * 
	 * @param value值
	 * 
	 */

	public static void invokeSet(Object o, String fieldName, Object value) {

		Method method = getSetMethod(o.getClass(), fieldName);

		try {

			method.invoke(o, new Object[] { value });

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	/**
	 * 
	 * 执行get方法
	 * 
	 * 
	 * 
	 * @param o执行对象
	 * 
	 * @param fieldName属性
	 * 
	 */

	public static Object invokeGet(Object o, String fieldName) {

		Method method = getGetMethod(o.getClass(), fieldName);

		try {

			return method.invoke(o, new Object[0]);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return null;

	}
}

package com.kelan.core.interceptor;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kelan.core.util.DateUtil;
import com.kelan.riding.route.entity.Record;
import com.kelan.riding.system.web.HttpRequest;

public class Test {
	public static void main(String[] args) throws SocketException {
		// String str = "{attach= sfsd , sub_mch_id=10000100,
		// time_end=20140903131540, openid=oUpF8uMEb4qRXf22hE3X68TekukE,
		// bank_type=CFT, return_code=SUCCESS}";
		// Map<String, String> map = mapStringToMap(str);
		// System.out.println(map);
		// Record Record=new Record();
		// json = "{routeId:'123',coordinateX:123,coordinateY:123}";
		// Map map=new HashMap();
		// map.put("routeId","123");
		// map.put("coordinateX", "123");
		// map.put("coordinateY", "123");
		// String
		// s=HttpRequest.sendPost("http://127.0.0.1:8080/route/addUserRouteRecord","json="+map.toString());
		// System.out.println("response:\n"+s);
		// System.out.println(DateUtil.getDateDescription("00:12"));
		// System.out.println(DateUtil.getFullDate());
		// try {
		// InetAddress addr = InetAddress.getLocalHost();
		// System.out.println("InetAddress:"+addr.getHostAddress().toString());
		// } catch (UnknownHostException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
			System.out.println(netInterface.getName());
			Enumeration addresses = netInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = (InetAddress) addresses.nextElement();
				if (ip != null && ip instanceof Inet4Address) {
					System.out.println("本机的IP = " + ip.getHostAddress());
				}
			}
		}

	}

	// public static Map<String, String> mapStringToMap(String str) {
	// str = str.substring(1, str.length() - 1);
	// String[] strs = str.split(",");
	// Map<String, String> map = new HashMap<String, String>();
	// for (String string : strs) {
	// String key = string.split("=")[0];
	// String value = string.split("=")[1];
	// map.put(key, value);
	// }
	// return map;
	// }
	public static List<String> getLocalIPList() {
		List<String> ipList = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			NetworkInterface networkInterface;
			Enumeration<InetAddress> inetAddresses;
			InetAddress inetAddress;
			String ip;
			while (networkInterfaces.hasMoreElements()) {
				networkInterface = networkInterfaces.nextElement();
				inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					inetAddress = inetAddresses.nextElement();
					if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
						ip = inetAddress.getHostAddress();
						ipList.add(ip);
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ipList;
	}
}

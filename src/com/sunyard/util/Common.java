package com.sunyard.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
public class Common {
	/**
	 * 判断变量是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		if (null == s || "".equals(s) || "".equals(s.trim())
				|| "null".equalsIgnoreCase(s)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 用来去掉List中空值和相同项的。
	 * 
	 * @param list
	 * @return
	 */
	public static List<String> removeSameItem(List<String> list) {
		List<String> difList = new ArrayList<String>();
		for (String t : list) {
			if (t != null && !difList.contains(t)) {
				difList.add(t);
			}
		}
		return difList;
	}

	/**
	 * 返回用户的IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String toIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取当前认证通过的用户名
	 * 
	 * @return
	 */
	public static String getAuthenticatedUsername() {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		if("anonymousUser".equals(userName)) userName = "";
		return userName;
	}

	/**
	 * 解决不同浏览器文件名乱码问题
	 * @param request
	 * @param response
	 * @param FileName
	 */
	public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName){
		final String userAgent = getBrowserName(request.getHeader("USER-AGENT").toLowerCase());
		try {
			String finalFileName = null;
			if("ie".equals(userAgent)) {// IE浏览器
				finalFileName = URLEncoder.encode(fileName, "UTF8");
			}else if("firefox".equals(userAgent)) {//火狐浏览器
				finalFileName = new String(fileName.getBytes(), "ISO8859-1");
			}else{
				finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
			}
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ finalFileName + "\"");// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		} catch (UnsupportedEncodingException e) {
			
		}
	}
	
	public static String getBrowserName(String agent) {
		if (agent.indexOf("msie") > 0) {
			return "ie";
		} else if (agent.indexOf("firefox") > 0) {
			return "firefox";
		} else if (agent.indexOf("gecko") > 0 && agent.indexOf("rv:11") > 0) {
			return "ie";
		} else {
			return "Others";
		}
	}
}

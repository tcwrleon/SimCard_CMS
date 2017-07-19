package com.sunyard.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sunyard.util.PropertiesUtil;

public class log4jListener implements ServletContextListener {

	public static final String log4jdirkey = "log4jdir";

	public void contextDestroyed(ServletContextEvent servletcontextevent) {
		System.getProperties().remove(log4jdirkey);

	}

	public void contextInitialized(ServletContextEvent servletcontextevent) {
		//String log4jdir = servletcontextevent.getServletContext().getRealPath("/");
		//log4jdir = Properties.getString("logPath");
		System.setProperty(log4jdirkey, PropertiesUtil.getString("logPath"));

	}

}
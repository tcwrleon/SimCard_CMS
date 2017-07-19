package com.sunyard.util;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


public class SpringUtil {

	public static WebApplicationContext getWebApplicationContext(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();  
		return wac;
	}
	
	public static DDUtil getCache(){
		return (DDUtil)getBean("dDUtil");
	}
	
	public static Object getBean(String name){
        return getWebApplicationContext().getBean(name);
    }
	
	public static <T> T getBean(Class<T> cls){
		return getWebApplicationContext().getBean(cls);
	}
}

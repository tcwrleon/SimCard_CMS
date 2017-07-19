package com.sunyard.security;

import javax.annotation.Resource;

import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sunyard.service.system.UserService;


/**
 * 这个类主要是用户登录验证
 */
public class MyAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private static final String USERNAME = "user_name";
	private static final String PASSWORD = "user_pwd";
	//登录成功后跳转的地址
	private String successUrl;
	//登录失败后跳转的地址
	private String errorUrl;
	@Resource
	private UserService userService;
	
	/**
	 * 自定义表单参数的name属性，默认是 j_username 和 j_password
	 * 定义登录成功和失败的跳转地址
	 */
	public void init() {
		this.setUsernameParameter(USERNAME);
		this.setPasswordParameter(PASSWORD);
		// 验证成功，跳转的页面
		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setDefaultTargetUrl(successUrl);
		this.setAuthenticationSuccessHandler(successHandler);

		// 验证失败，跳转的页面
		SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
		failureHandler.setDefaultFailureUrl(errorUrl);
		this.setAuthenticationFailureHandler(failureHandler);
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getErrorUrl() {
		return errorUrl;
	}

	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}
}

package com.sunyard.security;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

/**
 * @author mumu
 * 这个类主要是权限访问验证
 * 会通过AbstractSecurityInterceptor拦截器拦截，
 * 
 * AbstractSecurityInterceptor有三个派生类：
 * FilterSecurityInterceptor，负责处理FilterInvocation，实现对URL资源的拦截。
 * MethodSecurityInterceptor，负责处理MethodInvocation，实现对方法调用的拦截。
 * AspectJSecurityInterceptor，负责处理JoinPoint，主要是用于对切面方法(AOP)调用的拦截。
 * 
 * 其中会调用FilterInvocationSecurityMetadataSource的方法来获取被拦截url所需的全部权限，
 * 在调用授权管理器AccessDecisionManager，这个授权管理器会通过spring的全局缓存SecurityContextHolder获取用户的权限信息，
 * 还会获取被拦截的url和被拦截url所需的全部权限，然后根据所配的策略（有：一票决定，一票否定，少数服从多数等），
 * 如果权限足够，则返回，权限不够则报错并调用权限不足页面
 * 
 */
@Service
public class MySecurityFilter extends AbstractSecurityInterceptor implements Filter {
	@Resource
	private MySecurityMetadataSource securityMetadataSource;
	@Resource
	private MyAccessDecisionManager accessDecisionManager;
	@Resource
	private AuthenticationManager myAuthenticationManager; 
	
	@PostConstruct
	public void init(){
		super.setAuthenticationManager(myAuthenticationManager);
		super.setAccessDecisionManager(accessDecisionManager);
	}
	
	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		FilterInvocation fi = new FilterInvocation(request, response, chain);
		invoke(fi);
	}
	
	private void invoke(FilterInvocation fi) throws IOException, ServletException {
		/*super.beforeInvocation(fi)源码解析：
		 * 1 获取请求资源的权限:Collection attributes = obtainSecurityMetadataSource().getAttributes(fi);
		 * 2 调用MyAccessDecisionManager类decide方法判断用户是否够权限
		 * Authentication authenticated = authenticateIfRequired()
		 * 		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();or
		 * 		authentication = this.authenticationManager.authenticate(authentication);
		 * 		SecurityContextHolder.getContext().setAuthentication(authentication);
		 * this.accessDecisionManager.decide(authenticated, object, attributes)
		 * 
		 * */
		
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
	
	public void destroy() {
		
	}

	@Override
	public Class<? extends Object> getSecureObjectClass() {
		//下面的MyAccessDecisionManager的supports方面必须放回true,否则会提醒类型错误
		return FilterInvocation.class;
	}
}
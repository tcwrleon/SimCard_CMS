package com.sunyard.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import com.sunyard.entity.system.ResourceEntity;
import com.sunyard.entity.system.RoleEntity;
import com.sunyard.enums.PLATFORMTYPE;
import com.sunyard.service.system.ResourceService;
import com.sunyard.service.system.RoleService;
import com.sunyard.util.StringUtil;

/**
 * @author mumu
 * 加载资源与角色的对应关系
 */

@Service
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	/*
	 * FilterInvocationSecurityMetadataSource extends SecurityMetadataSource
	 * */
	@Resource
	private ResourceService resourceService;
	@Resource
	private RoleService roleService;
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
	private static Logger logger = Logger.getLogger(MySecurityMetadataSource.class);
	private static String prefix = "/";
	
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}
	
	/**
	 * @PostConstruct是Java EE 5引入的注解，
	 * Spring允许开发者在受管Bean中使用它。当DI容器实例化当前受管Bean时，
	 * @PostConstruct注解的方法会被自动触发，从而完成一些初始化工作，
	 * 
	 * 加载所有资源与角色的关系
	 */
	@PostConstruct
	private void loadResourceDefine() {
		if (resourceMap == null) {
			resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
			//加载所有管理台角色
			RoleEntity roleEntity = new RoleEntity();
			roleEntity.setPlatform_type(PLATFORMTYPE.Background.getCode());
			List<RoleEntity> roles = roleService.queryAll(roleEntity);
			
			for(RoleEntity role : roles){
				String roleId = role.getRole_id();
				ConfigAttribute ca = new SecurityConfig(roleId);
				
				List<ResourceEntity> resources = resourceService.loadResourcesByRoleId(roleId);
				//根据角色Id加载所有的资源
				String resUrl = "";
				for(ResourceEntity res : resources){
					
					if(StringUtil.isEmpty(res.getUrl())) continue;
					if(resUrl.equals(prefix + res.getUrl())) continue;
					resUrl = prefix + res.getUrl();
					
					//资源url做resourceMap的key,角色Id做value
					if(resourceMap.containsKey(resUrl)){
						Collection<ConfigAttribute> value = resourceMap.get(resUrl);
						value.add(ca);
						resourceMap.put(resUrl, value);
					}else{
						Collection<ConfigAttribute> cas = new ArrayList<ConfigAttribute>();
						cas.add(ca);
						resourceMap.put(resUrl, cas);
					}
				}
			}
			logger.info("资源与角色的关系=" + resourceMap);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.springframework.security.access.SecurityMetadataSource#getAttributes(java.lang.Object)
	 * 返回所请求资源所需要的角色
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		if(resourceMap == null) {
			loadResourceDefine();
		}
		if(requestUrl.indexOf("?")>-1){
			requestUrl=requestUrl.substring(0,requestUrl.indexOf("?"));
		}
		Collection<ConfigAttribute> configAttributes = resourceMap.get(requestUrl);
		return configAttributes;
	}


}
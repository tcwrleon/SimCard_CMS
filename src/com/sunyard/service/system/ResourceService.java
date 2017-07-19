package com.sunyard.service.system;

import java.util.List;

import com.sunyard.entity.system.ResourceEntity;
import com.sunyard.service.BaseService;

/**
 * @author mumu
 *
 */
public interface ResourceService extends BaseService<ResourceEntity>  {
	
	/**
	 * 根据用户名加载其资源（权限）
	 * @param username
	 * @return
	 */
	List<ResourceEntity> getUserResourcesByName(String username);
	
	/**
	 * 根据角色加载其资源（权限）
	 * @param roleId
	 * @return
	 */
	List<ResourceEntity> loadResourcesByRoleId(String roleId);
}

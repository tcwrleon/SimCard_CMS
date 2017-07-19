package com.sunyard.service.system;

import java.util.List;
import java.util.Map;

import com.sunyard.entity.system.RoleEntity;
import com.sunyard.service.BaseService;

/**
 * @author mumu
 *
 */
public interface RoleService extends BaseService<RoleEntity>{

	/**
	 * 权限分配
	 * @param roleId 要分配权限的角色编号
	 * @param checkedId 待分配权限编号
	 */
	void assignRoleResources(String roleId, String checkedId);

	/**
	 * 角色资源列表导出
	 * @param roleEntity
	 * @return
	 */
	List<Map<String, Object>> queryRoleResources(RoleEntity roleEntity);
	
}

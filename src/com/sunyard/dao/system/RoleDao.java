package com.sunyard.dao.system;

import java.util.List;
import java.util.Map;

import com.sunyard.dao.BaseDao;
import com.sunyard.entity.system.RoleEntity;

/**
 * @author mumu
 *
 */
public interface RoleDao extends BaseDao<RoleEntity> {

	/**
	 * 根据角色编号批量删除角色资源关系表中记录
	 * @param list 角色编号集合
	 */
	void deleteRoleResources(List<String> list);

	/**
	 * 根据角色编号批量删除用户角色关系表中的记录 
	 * @param list
	 */
	void deleteRoleUsers(List<String> list);

	/**
	 * 批量插入角色资源关系表
	 * @param map
	 */
	void addRoleResources(Map<String, Object> map);
	
	/**
	 * 查询角色资源列表
	 * @param role
	 * @return
	 */
	List<Map<String,Object>> queryRoleResources(RoleEntity role);

}

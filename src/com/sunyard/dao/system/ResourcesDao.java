package com.sunyard.dao.system;

import java.util.List;

import com.sunyard.dao.BaseDao;
import com.sunyard.entity.system.ResourceEntity;

public interface ResourcesDao extends BaseDao<ResourceEntity> {

	List<ResourceEntity> getUserResourcesByName(String username);

	List<ResourceEntity> loadResourcesByRoleId(String roleId);

	void deleteResourceRoles(List<String> list);

}

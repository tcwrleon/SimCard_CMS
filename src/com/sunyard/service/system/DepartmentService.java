package com.sunyard.service.system;

import java.util.List;

import com.sunyard.entity.system.DepartmentEntity;
import com.sunyard.service.BaseService;

public interface DepartmentService extends BaseService<DepartmentEntity> {
	/**
	 * 生成机构树
	 * @param prefix
	 * @return
	 */
	public List<DepartmentEntity> createDepartmentTree(String prefix);
	
}

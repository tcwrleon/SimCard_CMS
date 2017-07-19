package com.sunyard.service.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.dao.system.RoleDao;
import com.sunyard.entity.system.RoleEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.log.annotation.OperLoggable;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.RoleService;
import com.sunyard.util.Common;
import com.sunyard.util.DDUtil;
import com.sunyard.util.StringUtil;

/**
 * 角色管理业务层实现类
 * @author mumu
 *
 */
@Service(value="roleService")
@Transactional
public class RoleServiceImpl implements RoleService{
	@Resource
	private RoleDao roleDao;
	
	/* (non-Javadoc)
	 * @see com.sunyard.service.BaseService#query(com.sunyard.pulgin.PageView, java.lang.Object)
	 * 分页查询
	 * 对结果集某些字段进行翻译
	 */
	@Override
	public PageView query(PageView pageView, RoleEntity t) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("paging", pageView);
		map.put("t", t);
		List<RoleEntity> list = roleDao.query(map);  
		for(RoleEntity item : list){
			item.setPlatform_type(DDUtil.getContent(DICTKEY.K_PLATTYPE.toString(), item.getPlatform_type()));//翻译平台类型
		}
		pageView.setResult(list);
		return pageView;
	}

	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#queryAll(java.lang.Object)
	 * 根据条件查出所有信息
	 */
	@Override
	public List<RoleEntity> queryAll(RoleEntity t) {
		return roleDao.queryAll(t);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#delete(java.lang.String)
	 * 角色批量删除
	 * 先删除角色资源关系表中的记录；然后删除用户角色关系表中的记录
	 * 最后批量删除角色表的记录
	 */
	@Override
	@OperLoggable(module="角色管理", description="删除")
	public void delete(String id) throws Exception {
		List<String> list = StringUtil.toList(id);
		roleDao.deleteRoleResources(list);//删除角色资源关系表中的记录
		roleDao.deleteRoleUsers(list);//删除用户角色关系表中的记录
		roleDao.deleteBatch(list);//批量删除角色表的记录
	}

	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#update(java.lang.Object)
	 */
	@Override
	@OperLoggable(module="角色管理", description="修改")
	public void update(RoleEntity t) throws Exception {
		roleDao.update(t);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#getById(java.lang.String)
	 */
	@Override
	public RoleEntity getById(String id) {
		return roleDao.getById(id);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#add(java.lang.Object)
	 */
	@Override
	@OperLoggable(module="角色管理", description="新增")
	public void add(RoleEntity t) throws Exception {
		roleDao.add(t);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.RoleService#assignRoleResources(java.lang.String, java.lang.String)
	 * 权限分配
	 * 先删除角色资源表已存在的记录；
	 * 根据分配权限编号批量插入角色资源表
	 */
	@Override
	@OperLoggable(module="角色管理",description="权限分配")
	public void assignRoleResources(String roleId, String checkedId) {
		roleDao.deleteRoleResources(StringUtil.toList(roleId));//删除角色资源表已存在的记录
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("roleId", roleId);
		map.put("list", Common.removeSameItem(StringUtil.toList(checkedId)));//清除相同的项
		roleDao.addRoleResources(map);//插入角色资源表
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.RoleService#queryRoleResources(com.sunyard.entity.system.RoleEntity)
	 * 角色资源列表查询
	 */
	@Override
	public List<Map<String, Object>> queryRoleResources(RoleEntity roleEntity) {
		return roleDao.queryRoleResources(roleEntity);
	}
	
}

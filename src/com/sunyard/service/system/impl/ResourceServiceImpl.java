package com.sunyard.service.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.dao.system.ResourcesDao;
import com.sunyard.entity.system.ResourceEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.log.annotation.OperLoggable;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.ResourceService;
import com.sunyard.util.DDUtil;
import com.sunyard.util.StringUtil;



@Service(value="resourceService")
@Transactional
public class ResourceServiceImpl implements ResourceService{
	@Resource
	private ResourcesDao resourcesDao;

	
	/* (non-Javadoc)
	 * @see com.sunyard.service.system.ResourceService#getUserResourcesByName(java.lang.String)
	 * 根据用户名加载其资源（权限）
	 */
	@Override
	public List<ResourceEntity> getUserResourcesByName(String username) {
		return resourcesDao.getUserResourcesByName(username);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.ResourceService#loadResourcesByRoleId(java.lang.String)
	 * 根据角色加载其资源（权限）
	 */
	@Override
	public List<ResourceEntity> loadResourcesByRoleId(String roleId) {
		return resourcesDao.loadResourcesByRoleId(roleId);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.BaseService#query(com.sunyard.pulgin.PageView, java.lang.Object)
	 * 分页查询
	 * 对查询结果进行字段翻译
	 */
	@Override
	public PageView query(PageView pageView, ResourceEntity t) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("paging", pageView);
		map.put("t", t);
		List<ResourceEntity> list = resourcesDao.query(map);
		for(ResourceEntity item : list){
			item.setPlatform_type(DDUtil.getContent(DICTKEY.K_PLATTYPE.toString(), item.getPlatform_type()));
			item.setType(DDUtil.getContent(DICTKEY.K_RESTYPE.toString(), item.getType()));
			item.setValid(DDUtil.getContent(DICTKEY.K_RESSTT.toString(), item.getValid()));
		}
		pageView.setResult(list);
		return pageView;
	}

	@Override
	public List<ResourceEntity> queryAll(ResourceEntity t) {
		return resourcesDao.queryAll(t);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#delete(java.lang.String)
	 * 资源批量删除
	 * 先删除角色与资源关系表中的记录（批量）
	 * 在删除资源表中的记录
	 */
	@Override
	@OperLoggable(module="权限管理", description="删除")
	public void delete(String id) throws Exception {
		List<String> list = StringUtil.toList(id);
		resourcesDao.deleteResourceRoles(list);//删除角色与资源关系表记录
		resourcesDao.deleteBatch(list);
		
	}

	@Override
	@OperLoggable(module="权限管理", description="修改")
	public void update(ResourceEntity t) throws Exception {
		resourcesDao.update(t);
	}

	@Override
	public ResourceEntity getById(String id) {
		return resourcesDao.getById(id);
	}

	@Override
	@OperLoggable(module="权限管理", description="修改")
	public void add(ResourceEntity t) throws Exception {
		resourcesDao.add(t);
	}


	

}

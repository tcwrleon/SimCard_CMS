package com.sunyard.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.dao.system.DepartmentDao;
import com.sunyard.entity.system.DepartmentEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.enums.DPTSTATUS;
import com.sunyard.log.annotation.OperLoggable;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.DepartmentService;
import com.sunyard.util.DDUtil;
import com.sunyard.util.StringUtil;

/**
 * 部门管理业务层实现类
 * @author mumu
 *
 */
@Service(value="departmentService")
@Transactional
public class DepartmentServiceImpl implements DepartmentService{
	@Resource
	private DepartmentDao departmentDao;

	/* 分页查询功能
	 * 1分页查询 2对结果处理（翻译中文）
	 * @see com.sunyard.service.BaseService#query(com.sunyard.pulgin.PageView, java.lang.Object)
	 */
	@Override
	public PageView query(PageView pageView, DepartmentEntity dptEntity) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("paging", pageView);
		map.put("t", dptEntity);
		List<DepartmentEntity> list = departmentDao.query(map);
		for(DepartmentEntity item : list){
			item.setDepartment_level(DDUtil.getContent(DICTKEY.K_DPTLVL.toString(), item.getDepartment_level()));
			item.setDepartment_stt(DDUtil.getContent(DICTKEY.K_DPTSTT.toString(), item.getDepartment_stt()));
		}
		pageView.setResult(list);
		return pageView;
	}

	@Override
	public List<DepartmentEntity> queryAll(DepartmentEntity dptEntity) {
		return departmentDao.queryAll(dptEntity);
	}

	@Override
	@OperLoggable(module="部门管理", description="删除")
	public void delete(String id) throws Exception {
		String[] idArr = StringUtil.toArr(id);
		for(String item : idArr){
			departmentDao.delete(item);
		}
		
	}

	@Override
	@OperLoggable(module="部门管理", description="修改")
	public void update(DepartmentEntity t) throws Exception {
		 departmentDao.update(t);
	}

	@Override
	public DepartmentEntity getById(String id) {
		return departmentDao.getById(id);
	}

	@Override
	@OperLoggable(module="部门管理", description="新增")
	public void add(DepartmentEntity dptEntity) throws Exception {
		departmentDao.add(dptEntity);
		
	}

	/**
	 * 生成机构树
	 * 1 查询所有有效机构 ；2遍历1得到顶级部门和其他部门集合 ；3遍历顶级部门递归其子部门
	 * @param prefix
	 * @return
	 */
	@Override
	public List<DepartmentEntity> createDepartmentTree (String prefix){
		DepartmentEntity department = new DepartmentEntity();
		//department.setDepartment_stt(DPTSTATUS.Activate.getCode());//激活状态
		List<DepartmentEntity> list = this.queryAll(department);
		List<DepartmentEntity> topList = new ArrayList<DepartmentEntity>();//顶级部门集合
		List<DepartmentEntity> otherList = new ArrayList<DepartmentEntity>();//顶级以外部门集合
		for(DepartmentEntity item : list){
			if(StringUtil.isEmpty(item.getDepartment_pid())){
				topList.add(item);
			}else{
				otherList.add(item);
			}
		}
		
		List<DepartmentEntity> treeList = new ArrayList<DepartmentEntity>();
		for(DepartmentEntity topItem : topList){
			treeList.add(topItem);
			getChildrenByParentId(topItem.getDepartment_id(),treeList,otherList,prefix,1);
		}
		
		return treeList;
	}
	
	/**
	 * 递归生成机构树
	 * 1 拼接前缀字符串； 2 递归子机构
	 * @param parentId 父ID
	 * @param treeList 树形列表集合
	 * @param otherList 遍历集合
	 * @param prefix 前缀
	 * @param level 递归层次
	 */
	private void getChildrenByParentId(String parentId, List<DepartmentEntity> treeList, 
			List<DepartmentEntity> otherList, String prefix, int level) {
		
		String prestr = "";
		if(!StringUtil.isEmpty(prefix)){ //根据递归层次拼接前缀字符串
			for(int i=0;i<level;i++){
				prestr = prestr + prefix;
			}
		}
		
		for(DepartmentEntity subItem : otherList){
			if(subItem.getDepartment_pid().equals(parentId)){
				subItem.setDepartment_name(prestr + subItem.getDepartment_name());
				treeList.add(subItem);
				getChildrenByParentId(subItem.getDepartment_id(), treeList, otherList,prefix,level+1);
			}
		}
		
	}
}

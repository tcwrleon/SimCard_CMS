package com.sunyard.service.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.dao.system.SysParamDao;
import com.sunyard.entity.system.SysParamEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.log.annotation.OperLoggable;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.SysParamService;
import com.sunyard.util.CacheUtil;
import com.sunyard.util.DDUtil;
import com.sunyard.util.StringUtil;
import com.sunyard.util.SysParamUtil;

/**
 * @author mumu
 *
 */
@Service(value="sysParamService")
@Transactional
public class SysParamServiceImpl implements SysParamService{
	@Resource
	private SysParamDao sysParamDao;
	
	/* (non-Javadoc)
	 * @see com.sunyard.service.BaseService#query(com.sunyard.pulgin.PageView, java.lang.Object)
	 * 分页查询
	 */
	@Override
	public PageView query(PageView pageView, SysParamEntity t) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("paging", pageView);
		map.put("t", t);
		
		List<SysParamEntity> list = sysParamDao.query(map);
		
		for(SysParamEntity item : list){
			item.setParam_type(DDUtil.getContent(DICTKEY.K_PARAMTYPE.toString(), item.getParam_type()));//结果集翻译
		}
		pageView.setResult(list);
		
		return pageView;
	}

	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#queryAll(java.lang.Object)
	 * 按条件查询所有
	 */
	@Override
	public List<SysParamEntity> queryAll(SysParamEntity t) {
		return sysParamDao.queryAll(t);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#delete(java.lang.String)
	 * 批量删除
	 */
	@Override
	@OperLoggable(module="系统参数管理", description="删除")
	public void delete(String id) throws Exception {
		sysParamDao.deleteBatch(StringUtil.toList(id));
		CacheUtil.remove(SysParamUtil.CACHE_SYSPARAM_MAP);
	}

	@Override
	@OperLoggable(module="系统参数管理", description="修改")
	public void update(SysParamEntity t) throws Exception {
		sysParamDao.update(t);
		CacheUtil.remove(SysParamUtil.CACHE_SYSPARAM_MAP);
	}

	@Override
	public SysParamEntity getById(String id) {
		return sysParamDao.getById(id);
	}

	@Override
	@OperLoggable(module="系统参数管理", description="新增")
	public void add(SysParamEntity t) throws Exception {
		sysParamDao.add(t);
		CacheUtil.remove(SysParamUtil.CACHE_SYSPARAM_MAP);
	}
	
}

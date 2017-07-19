package com.sunyard.service.system.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.dao.system.OperLogDao;
import com.sunyard.entity.system.OperLogEntity;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.OperLogService;
/**
 * @author mumu
 * 用户操作日志业务层
 */
@Transactional
@Service(value="operLogService")
public class OperLogServiceImpl implements OperLogService {
	@Resource
	private OperLogDao operLogDao;
	
	/* (non-Javadoc)
	 * @see com.sunyard.service.ReportService#query(com.sunyard.pulgin.PageView, java.util.Map)
	 * 分页查询
	 */
	@Override
	public PageView query(PageView pageView, Map<String, Object> param) {
		param.put("paging", pageView);
		List<OperLogEntity> list = operLogDao.query(param);
		pageView.setResult(list);
		return pageView;
	}
	
	/* (non-Javadoc)
	 * @see com.sunyard.service.ReportService#queryAll(java.util.Map)
	 * 查询全部
	 */
	@Override
	public List<OperLogEntity> queryAll(Map<String, Object> map) {
		return operLogDao.query(map);
	}

	
	/* (non-Javadoc)
	 * @see com.sunyard.service.system.OperLogService#add(com.sunyard.entity.system.OperLogEntity)
	 * 新增日志
	 */
	@Override
	public void add(OperLogEntity t) throws Exception {
		operLogDao.add(t);
	}

}

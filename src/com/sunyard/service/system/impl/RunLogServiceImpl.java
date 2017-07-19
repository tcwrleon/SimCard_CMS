package com.sunyard.service.system.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.dao.system.RunLogDao;
import com.sunyard.entity.system.RunLogEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.RunLogService;
import com.sunyard.util.DDUtil;

/**
 * 系统运行日志业务层
 * @author mumu
 *
 */
@Transactional
@Service(value="runLogService")
public class RunLogServiceImpl implements RunLogService {
	@Resource
	private RunLogDao runLogDao;
	
	/* (non-Javadoc)
	 * @see com.sunyard.service.ReportService#query(com.sunyard.pulgin.PageView, java.util.Map)
	 * 分页查询
	 */
	@Override
	public PageView query(PageView pageView, Map<String, Object> param) {
		param.put("paging", pageView);
		List<RunLogEntity> list = runLogDao.query(param);
		for(RunLogEntity item : list){
			item.setSystem_type(DDUtil.getContent(DICTKEY.K_PLATTYPE.toString(), item.getSystem_type()));
		}
		pageView.setResult(list);
		return pageView;
	}


	/* (non-Javadoc)
	 * @see com.sunyard.service.ReportService#queryAll(java.util.Map)
	 */
	@Override
	public List<RunLogEntity> queryAll(Map<String, Object> map) {
		return runLogDao.query(map);
	}
	
	/* (non-Javadoc)
	 * @see com.sunyard.service.system.RunLogService#add(com.sunyard.entity.system.RunLogEntity)
	 */
	@Override
	public void add(RunLogEntity t) throws Exception {
		runLogDao.add(t);
	}
}

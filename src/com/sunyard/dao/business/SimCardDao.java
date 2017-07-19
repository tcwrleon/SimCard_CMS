package com.sunyard.dao.business;

import java.util.List;
import java.util.Map;

import com.sunyard.dao.BaseDao;
import com.sunyard.entity.business.SimCardEntity;

public interface SimCardDao extends BaseDao<SimCardEntity>{
	public List<Map<String, Object>> querySimCardInfo(Map<String, Object> map) throws Exception;

	public List<SimCardEntity> queryforExport(Map<String,String> param) throws Exception;
}

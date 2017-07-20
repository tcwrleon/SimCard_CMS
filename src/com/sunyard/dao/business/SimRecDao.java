package com.sunyard.dao.business;

import com.sunyard.dao.BaseDao;
import com.sunyard.entity.business.SmsRecEntity;

import java.util.List;
import java.util.Map;

public interface SimRecDao extends BaseDao<SmsRecEntity>{

	public List<Map<String, Object>> querySimCardSMS(Map<String, Object> param) throws Exception;

	public void deleteSMS(String id) throws Exception;

}

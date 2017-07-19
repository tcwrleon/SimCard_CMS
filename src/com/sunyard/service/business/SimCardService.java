package com.sunyard.service.business;

import java.util.List;
import java.util.Map;

import com.sunyard.entity.business.SimCardEntity;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.BaseService;

public interface SimCardService extends BaseService<SimCardEntity>{
	public PageView querySimCardInfo(PageView pageView, Map<String,String> param) throws Exception;

	public List<SimCardEntity> queryforExport(Map<String,String> param) throws Exception;

}

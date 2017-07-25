package com.sunyard.service.business;

import com.sunyard.entity.business.SmsRecEntity;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.BaseService;

import java.util.List;
import java.util.Map;

public interface SmsRecService extends BaseService<SmsRecEntity>{

	public PageView querySmsRecInfo(PageView pageView, Map<String,String> param) throws Exception;

    public String queryCode(String simnum) throws Exception;

    public void deleteSMS (String time)throws Exception;
}

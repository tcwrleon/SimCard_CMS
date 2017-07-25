package com.sunyard.service.business.impl;

import com.sunyard.datasource.AccessManager;
import com.sunyard.dao.business.SimRecDao;
import com.sunyard.datasource.CustomerContextHolder;
import com.sunyard.entity.business.SmsRecEntity;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.business.SmsRecService;
import com.sunyard.util.PhoneUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value="smsRecService")
@Transactional
public class SmsRecServiceImpl implements SmsRecService {

	@Resource
	private SimRecDao simRecDao;

	@Override
	public PageView querySmsRecInfo(PageView pageView, Map<String, String> param) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("paging", pageView);
		map.put("t", param);
		List<Map<String,Object>> list = simRecDao.querySimCardSMS(map);
		System.out.println("list===="+list);
		pageView.setResult(list);
		return pageView;
	}

	@Override
	public String queryCode(String simnum) throws Exception {
		String content = simRecDao.queryCode(simnum);
		return PhoneUtil.getCode(content);
	}

	@Override
    public void deleteSMS(String id) throws Exception {
		simRecDao.deleteSMS(id);
    }


    @Override
	public PageView query(PageView pageView, SmsRecEntity smsRecEntity) {
		return null;
	}

	@Override
	public List<SmsRecEntity> queryAll(SmsRecEntity smsRecEntity) {
		return null;
	}

	@Override
	public void delete(String id) throws Exception {

	}

	@Override
	public void update(SmsRecEntity smsRecEntity) throws Exception {

	}

	@Override
	public SmsRecEntity getById(String id) {
		return null;
	}

	@Override
	public void add(SmsRecEntity smsRecEntity) throws Exception {

	}
}

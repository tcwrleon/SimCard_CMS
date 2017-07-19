package com.sunyard.service.business.impl;

import com.sunyard.access.AccessManager;
import com.sunyard.dao.business.SimCardDao;
import com.sunyard.entity.business.SimCardEntity;
import com.sunyard.entity.business.SmsRecEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.business.SimCardService;
import com.sunyard.service.business.SmsRecService;
import com.sunyard.util.DDUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value="smsRecService")
@Transactional
public class SmsRecServiceImpl implements SmsRecService {

	@Override
	public PageView querySmsRecInfo(PageView pageView, Map<String, String> param) throws Exception{
		List<Map<String,String>> list = AccessManager.getInstance().querySimCardInfo(pageView,param);
		System.out.println("list===="+list);
		/*for(Map<String,String> item : list){
			if(item.get("operators")!=null) item.put("operators", DDUtil.getContent(DICTKEY.SIM_OPERATORS.toString(), item.get("operators").toString()));
		}*/
		pageView.setResult(list);
		return pageView;
	}

    @Override
    public void deleteSMS(String id) throws Exception {
        AccessManager.getInstance().deleteSMS(id);
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

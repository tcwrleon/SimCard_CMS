package com.sunyard.service.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.dao.business.SimCardDao;
import com.sunyard.entity.business.SimCardEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.business.SimCardService;
import com.sunyard.util.DDUtil;
import com.sunyard.util.StringUtil;

@Service(value="simCardService")
@Transactional
public class SimCardServiceImpl implements SimCardService {
	@Resource
	private SimCardDao simCardDao;

	@Override
	public PageView querySimCardInfo(PageView pageView, Map<String, String> param) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("paging", pageView);
		map.put("t", param);
		List<Map<String,Object>> list = simCardDao.querySimCardInfo(map);
		System.out.println("list===="+list);
		for(Map<String,Object> item : list){

			if(item.get("operators")!=null) item.put("operators", DDUtil.getContent(DICTKEY.SIM_OPERATORS.toString(), item.get("operators").toString()));
			if(item.get("author")!=null) item.put("author", DDUtil.getContent(DICTKEY.SIM_AUTHOR.toString(), item.get("author").toString()));
			if(item.get("final_at")!=null) item.put("final_at", DDUtil.getContent(DICTKEY.SIM_FINAL_AT.toString(), item.get("final_at").toString()));
			if(item.get("final_action")!=null) item.put("final_action", DDUtil.getContent(DICTKEY.SIM_FINAL_ACTION.toString(), item.get("final_action").toString()));

		}
		pageView.setResult(list);
		return pageView;
	}

	@Override
	public List<SimCardEntity> queryforExport(Map<String, String> param) throws Exception {

		List<SimCardEntity> list = simCardDao.queryforExport(param);
		System.out.println("list===="+list);
		for(SimCardEntity item : list){

			if(item.getOperators()!=null) item.setOperators(DDUtil.getContent(DICTKEY.SIM_OPERATORS.toString(), item.getOperators()));
			if(item.getAuthor()!=null) item.setAuthor(DDUtil.getContent(DICTKEY.SIM_AUTHOR.toString(), item.getAuthor()));
			if(item.getFinal_at()!=null) item.setFinal_at(DDUtil.getContent(DICTKEY.SIM_FINAL_AT.toString(), item.getFinal_at()));
			if(item.getFinal_action()!=null) item.setFinal_action(DDUtil.getContent(DICTKEY.SIM_FINAL_ACTION.toString(), item.getFinal_action()));

		}
		return list;
	}


	@Override
	public PageView query(PageView pageView, SimCardEntity t) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<SimCardEntity> queryAll(SimCardEntity t) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void delete(String ids) throws Exception {
		String[] sim_id = ids.split(",");
		for(String id : sim_id){
            simCardDao.delete(id);
		}
	}



	@Override
	public void update(SimCardEntity t) throws Exception {
        simCardDao.update(t);
	}



	@Override
	public SimCardEntity getById(String id) {
		return simCardDao.getById(id);
	}



	@Override
	public void add(SimCardEntity t) throws Exception {
        simCardDao.add(t);
	}


}

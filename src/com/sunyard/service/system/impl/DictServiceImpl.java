package com.sunyard.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.base.model.Consts;
import com.sunyard.dao.system.DictDao;
import com.sunyard.entity.system.DictEntity;
import com.sunyard.entity.system.DictMapEntity;
import com.sunyard.log.annotation.OperLoggable;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.DictService;
import com.sunyard.util.DDUtil;
import com.sunyard.util.StringUtil;

/**
 * @author mumu
 *
 */
@Service(value="dictService")
@Transactional
public class DictServiceImpl implements DictService{
	@Resource
	private DictDao dictDao;
	
	/* (non-Javadoc)
	 * @see com.sunyard.service.BaseService#query(com.sunyard.pulgin.PageView, java.lang.Object)
	 * 分页查询
	 */
	@Override
	public PageView query(PageView pageView, DictEntity t) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("paging", pageView);
		map.put("t", t);
		List<DictEntity> list = dictDao.query(map);
		pageView.setResult(list);
		return pageView;
	}

	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#queryAll(java.lang.Object)
	 * 查询满足条件所有数据字典
	 */
	@Override
	public List<DictEntity> queryAll(DictEntity t) {
		return dictDao.queryAll(t);
	}
	
	/* (non-Javadoc)
	 * @see com.sunyard.service.system.DictService#getDictMapByKey(java.lang.String)
	 * 由主键获取主键对应数据字典名称
	 */
	@Override
	public DictMapEntity getDictMapByKey(String sun_key) {
		return dictDao.getDictMapByKey(sun_key);
	}
	
	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#delete(java.lang.String)
	 * 批量删除数据字典
	 * 分割值和说明字符串，组成DictEntity对象的集合同时删除对应缓存；批量删除数据字典表；
	 * 根据主键还剩；批量删除数据字典映射表
	 */
	@Override
	@OperLoggable(module="数据字典设置", description="删除")
	public void delete(String id) throws Exception {
		List<String> list = StringUtil.toList(id);
		List<DictEntity> dicts = new ArrayList<DictEntity>();
		Set<String> sunKeys = new HashSet<String>();//sun_key集合
		
		for(String str : list){
			DictEntity dict = new DictEntity();
			String[] arr = StringUtil.toArr(str, "#");
			String sun_key = arr[0];
			String val = arr[1];
			sunKeys.add(sun_key);
			dict.setSun_key(sun_key);
			dict.setVal(val);
			dicts.add(dict);
			DDUtil.removeFromCache(sun_key);//删除ddcache中的缓存
			DDUtil.removeFromCache(sun_key, val);
		}
		
		dictDao.deleteBatchDict(dicts);//批量删除
		
		Iterator<String> keyIter = sunKeys.iterator();
		while(keyIter.hasNext()){
			List<DictEntity> remain = this.getAllDdContentByKey(keyIter.next());
			if(remain == null || remain.size() == 0){
				
			}else{
				keyIter.remove();
			}
		}
		
		if(sunKeys.size() > 0){//批量删除数据字典映射表
			dictDao.deleteBatchDictMap(new ArrayList<String>(sunKeys));
		}
	}

	@Override
	public void update(DictEntity t) throws Exception {
		
	}

	@Override
	public DictEntity getById(String id) {
		return null;
	}

	@Override
	public void add(DictEntity t) throws Exception {
		
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.DictService#getAllDdContentSpec(java.lang.String, java.lang.String)
	 * 使用ehcache缓存注解，key（主键+值）存在从缓存取，否则从数据库中取
	 */
	@Override
	@Cacheable(value = "ddCache",key = "T(java.lang.String).valueOf(#sun_key).concat('-').concat(#val)")
	public String getAllDdContentSpec(String sun_key, String val) {
		return dictDao.getAllDdContentSpec(sun_key,val);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.DictService#getAllDdContentByKey(java.lang.String)
	 * 使用ehcache缓存注解，key（主键）存在从缓存取，否则从数据库中取
	 */
	@Override
	@Cacheable(value = "ddCache",key = "#sun_key")
	public List<DictEntity> getAllDdContentByKey(String sun_key) {
		return dictDao.getAllDdContentByKey(sun_key);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.DictService#addDict(com.sunyard.entity.system.DictMapEntity, java.lang.String, java.lang.String)
	 * 批量添加数据字典
	 * 校验待添加值在数据库是否已存在；参数组装成DictEntity对象；
	 * 根据主键先删除com_dictmap记录，然后再新增；
	 * 批量插入com_dict;
	 * 清除DDutil缓存
	 */
	@Override
	@OperLoggable(module="数据字典设置", description="新增")
	public String addDict(DictMapEntity dictMapEntity, String dictValues,
			String dictDescs) {
		boolean hasDict = false;
		List<String> vals = StringUtil.toList(dictValues);
		List<String> descs = StringUtil.toList(dictDescs);
		List<DictEntity> dicts = getAllDdContentByKey(dictMapEntity.getSun_key());
		//校验待添加值在数据库是否已存在
		for(DictEntity item: dicts){
			if(vals.contains(item.getVal())){
				hasDict = true;
				break;
			}
		}
		
		if(hasDict) return Consts.ERROR_CODE;
		//参数组装成DictEntity对象
		List<DictEntity> addList = new ArrayList<DictEntity>();
		for(int i = 0; i < vals.size(); i++){
			DictEntity obj = new DictEntity();
			obj.setVal(vals.get(i));
			obj.setPrompt(descs.get(i));
			obj.setSun_key(dictMapEntity.getSun_key());
			addList.add(obj);
		}
		
		dictDao.deleteDictMap(dictMapEntity);
		dictDao.addDictMap(dictMapEntity);
		
		dictDao.addBatchDict(addList);
		//清除DDutil缓存
		removeFromCache(dictMapEntity.getSun_key());
		return Consts.SUCCESS_CODE;
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.DictService#updateDict(com.sunyard.entity.system.DictMapEntity, java.lang.String, java.lang.String)
	 * 批量更新数据字典
	 * 先将参数组装成DictEntity对象；
	 * 先更新com_dictmap表；批量更新com_dict表
	 * 清除已存在缓存
	 */
	@Override
	@OperLoggable(module="数据字典设置", description="修改")
	public String updateDict(DictMapEntity dictMapEntity, String dictValues,
			String dictDescs) {
		List<String> vals = StringUtil.toList(dictValues);
		List<String> descs = StringUtil.toList(dictDescs);
		List<DictEntity> list = new ArrayList<DictEntity>();
		//将参数组装成DictEntity对象
		for(int i = 0; i < vals.size(); i++){
			DictEntity obj = new DictEntity();
			obj.setVal(vals.get(i));
			obj.setPrompt(descs.get(i));
			obj.setSun_key(dictMapEntity.getSun_key());
			list.add(obj);
		}
		
		dictDao.updateDictMap(dictMapEntity);
		dictDao.updateBatchDict(list);
		//清除DDutil缓存
		removeFromCache(dictMapEntity.getSun_key());
		
		return Consts.SUCCESS_CODE;
	}

	/**
	 * 清除DDutil缓存
	 * 清除所有sun_key和sun_key+val为key缓存
	 * @param sun_key
	 */
	private void removeFromCache(String sun_key){
		DDUtil.removeFromCache(sun_key);
		List<DictEntity> dicts = getAllDdContentByKey(sun_key);
		for(DictEntity dict : dicts){
			DDUtil.removeFromCache(dict.getSun_key(), dict.getVal());
		}
	}


}

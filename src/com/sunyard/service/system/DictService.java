package com.sunyard.service.system;

import java.util.List;

import com.sunyard.entity.system.DictEntity;
import com.sunyard.entity.system.DictMapEntity;
import com.sunyard.service.BaseService;

/**
 * @author mumu
 *
 */
public interface DictService extends BaseService<DictEntity> {

	/**
	 * 由主键和值获取中文名称
	 * @param sun_key
	 * @param val
	 * @return
	 */
	String getAllDdContentSpec(String sun_key, String val);

	/**
	 * 由主键获取所有值和说明
	 * @param sun_key
	 * @return
	 */
	List<DictEntity> getAllDdContentByKey(String sun_key);

	/**
	 * 数据字典批量添加
	 * @param dictMapEntity 
	 * @param dictValues 值的字符串
	 * @param dictDescs  说明的字符串
	 * @return
	 */
	String addDict(DictMapEntity dictMapEntity, String dictValues,
			String dictDescs);

	/**
	 * 由主键获取主键对应数据字典名称
	 * @param sun_key
	 * @return
	 */
	DictMapEntity getDictMapByKey(String sun_key);

	/**
	 * 批量更新数据字典
	 * @param dictMapEntity
	 * @param dictValues
	 * @param dictDescs
	 * @return
	 */
	String updateDict(DictMapEntity dictMapEntity, String dictValues,
			String dictDescs);

	
}

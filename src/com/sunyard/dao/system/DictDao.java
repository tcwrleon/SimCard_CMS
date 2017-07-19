package com.sunyard.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sunyard.dao.BaseDao;
import com.sunyard.entity.system.DictEntity;
import com.sunyard.entity.system.DictMapEntity;

public interface DictDao extends BaseDao<DictEntity> {

	String getAllDdContentSpec(@Param("sun_key") String sun_key, @Param("val") String val);

	List<DictEntity> getAllDdContentByKey(String sun_key);

	void deleteDictMap(DictMapEntity dictMapEntity);

	void addDictMap(DictMapEntity dictMapEntity);

	void addBatchDict(List<DictEntity> addList);

	DictMapEntity getDictMapByKey(String sun_key);

	void updateDictMap(DictMapEntity dictMapEntity);

	void updateBatchDict(List<DictEntity> list);

	void deleteBatchDict(List<DictEntity> dicts);

	void deleteBatchDictMap(List<String> list);

}

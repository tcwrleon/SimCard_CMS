package com.sunyard.dao;

import java.util.List;
import java.util.Map;

import com.sunyard.base.Base;


public interface BaseDao<T> extends Base<T> {
	public List<T> query(Map<String,Object> map);
	void deleteBatch(List<String> list);
	

}

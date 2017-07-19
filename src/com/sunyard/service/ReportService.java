package com.sunyard.service;

import java.util.List;
import java.util.Map;

import com.sunyard.pulgin.PageView;

/**
 * 报表公共业务层
 * @author mumu
 * 
 * @param <T>
 */
public interface ReportService<T> {
	/**
	 * 支持分页查询
	 * @param pageView
	 * @param param
	 * @return
	 */
	public PageView query(PageView pageView, Map<String, Object> param);
	
	/**
	 * 查询全部，供导出功能使用
	 * @param map
	 * @return
	 */
	public List<T> queryAll(Map<String, Object> map);
}

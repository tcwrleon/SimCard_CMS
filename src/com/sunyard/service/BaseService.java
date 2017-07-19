package com.sunyard.service;

import com.sunyard.base.Base;
import com.sunyard.pulgin.PageView;

public interface BaseService<T> extends Base<T> {
	public PageView query(PageView pageView,T t);
}

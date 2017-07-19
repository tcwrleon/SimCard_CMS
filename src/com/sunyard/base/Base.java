package com.sunyard.base;

import java.util.List;

public interface Base<T> {
	public List<T> queryAll(T t);
	public void delete(String id) throws Exception;
	public void update(T t) throws Exception;
	public T getById(String id);
	public void add(T t) throws Exception;
}

package com.sunyard.entity.system;

import java.util.List;

/**
 * 报表查询页面显示信息
 * @author mumu
 *
 */
public class InitQueryPageEntity {
	private List<String> queryListTitles;     //查询列表标题
	private List<String> queryListContents;   //查询列表内容对应字段
	private String queryPageUrl;        //查询页面URL
	private String queryPageTitle;      //查询页面标题
	private String queryConditionHtml;//非日期查询条件HTML
	private Object otherQueryParam;     //非日期查询条件
	
	public String getQueryPageUrl() {
		return queryPageUrl;
	}
	public void setQueryPageUrl(String queryPageUrl) {
		this.queryPageUrl = queryPageUrl;
	}
	public String getQueryPageTitle() {
		return queryPageTitle;
	}
	public void setQueryPageTitle(String queryPageTitle) {
		this.queryPageTitle = queryPageTitle;
	}
	public String getQueryConditionHtml() {
		return queryConditionHtml;
	}
	public void setQueryConditionHtml(String queryConditionHtml) {
		this.queryConditionHtml = queryConditionHtml;
	}
	public Object getOtherQueryParam() {
		return otherQueryParam;
	}
	public void setOtherQueryParam(Object otherQueryParam) {
		this.otherQueryParam = otherQueryParam;
	}
	public List<String> getQueryListTitles() {
		return queryListTitles;
	}
	public void setQueryListTitles(List<String> queryListTitles) {
		this.queryListTitles = queryListTitles;
	}
	public List<String> getQueryListContents() {
		return queryListContents;
	}
	public void setQueryListContents(List<String> queryListContents) {
		this.queryListContents = queryListContents;
	}
	
}


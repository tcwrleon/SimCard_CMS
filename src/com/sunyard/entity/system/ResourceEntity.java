package com.sunyard.entity.system;

import java.io.Serializable;

public class ResourceEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String privilege_id;
	private String privilege_name;
	private String parent_id;
	private String url;
	private String type; //资源类型 0模块   1菜单   2按钮
	private int sort; //排列顺序
	private String valid;//资源状态 0 启用  1禁用 
	private String platform_type; //平台类型 0 管理台  1 互联网端 
	
	public String getPrivilege_id() {
		return privilege_id;
	}
	public void setPrivilege_id(String privilege_id) {
		this.privilege_id = privilege_id;
	}
	public String getPrivilege_name() {
		return privilege_name;
	}
	public void setPrivilege_name(String privilege_name) {
		this.privilege_name = privilege_name;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String getPlatform_type() {
		return platform_type;
	}
	public void setPlatform_type(String platform_type) {
		this.platform_type = platform_type;
	}
	
}

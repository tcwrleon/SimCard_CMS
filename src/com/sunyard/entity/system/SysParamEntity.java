package com.sunyard.entity.system;

public class SysParamEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String param_type;
	private String prod_type;
	private String prod_id;
	private String param_code;//参数ID
	private String param_name;//参数名称
	private String param_value;//值
	private String param_desc;//说明
	
	public String getParam_type() {
		return param_type;
	}
	public void setParam_type(String param_type) {
		this.param_type = param_type;
	}
	public String getProd_type() {
		return prod_type;
	}
	public void setProd_type(String prod_type) {
		this.prod_type = prod_type;
	}
	public String getProd_id() {
		return prod_id;
	}
	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}
	public String getParam_code() {
		return param_code;
	}
	public void setParam_code(String param_code) {
		this.param_code = param_code;
	}
	public String getParam_name() {
		return param_name;
	}
	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}
	public String getParam_value() {
		return param_value;
	}
	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}
	public String getParam_desc() {
		return param_desc;
	}
	public void setParam_desc(String param_desc) {
		this.param_desc = param_desc;
	}
}


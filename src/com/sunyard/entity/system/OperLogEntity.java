package com.sunyard.entity.system;

public class OperLogEntity {
	
	private String log_id;
	private String user_id;
	private String user_name;
	private String oper_date;
	private String user_ip;
	private String module_name;
	private String class_name;
	private String method_name;
	private String oper_param;
	private String oper_desc;
	private String oper_result;
	private String error_info;
	
	public String getLog_id() {
		return log_id;
	}
	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getOper_date() {
		return oper_date;
	}
	public void setOper_date(String oper_date) {
		this.oper_date = oper_date;
	}
	public String getUser_ip() {
		return user_ip;
	}
	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}
	public String getModule_name() {
		return module_name;
	}
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getMethod_name() {
		return method_name;
	}
	public void setMethod_name(String method_name) {
		this.method_name = method_name;
	}
	public String getOper_param() {
		return oper_param;
	}
	public void setOper_param(String oper_param) {
		this.oper_param = oper_param;
	}
	public String getOper_desc() {
		return oper_desc;
	}
	public void setOper_desc(String oper_desc) {
		this.oper_desc = oper_desc;
	}
	public String getOper_result() {
		return oper_result;
	}
	public void setOper_result(String oper_result) {
		this.oper_result = oper_result;
	}
	public String getError_info() {
		return error_info;
	}
	public void setError_info(String error_info) {
		this.error_info = error_info;
	}
}

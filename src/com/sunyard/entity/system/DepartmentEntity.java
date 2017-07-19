package com.sunyard.entity.system;

public class DepartmentEntity {

	private String department_id;
	private String department_name;
	private String department_level;
	private String department_pid;
	private String department_pid_name;
	private String department_inner_id;
	private String department_desc;
	private String department_stt;
	
	public String getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}
	public String getDepartment_name() {
		return department_name;
	}
	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}
	public String getDepartment_level() {
		return department_level;
	}
	public void setDepartment_level(String department_level) {
		this.department_level = department_level;
	}
	public String getDepartment_pid() {
		return department_pid;
	}
	public void setDepartment_pid(String department_pid) {
		this.department_pid = department_pid;
	}
	public String getDepartment_inner_id() {
		return department_inner_id;
	}
	public void setDepartment_inner_id(String department_inner_id) {
		this.department_inner_id = department_inner_id;
	}
	public String getDepartment_desc() {
		return department_desc;
	}
	public void setDepartment_desc(String department_desc) {
		this.department_desc = department_desc;
	}
	public String getDepartment_stt() {
		return department_stt;
	}
	public void setDepartment_stt(String department_stt) {
		this.department_stt = department_stt;
	}
	@Override
	public String toString() {
		return "机构代码=" + getDepartment_id() + "机构名称=" + getDepartment_name() + "上级机构=" + getDepartment_pid();
	}
	public String getDepartment_pid_name() {
		return department_pid_name;
	}
	public void setDepartment_pid_name(String department_pid_name) {
		this.department_pid_name = department_pid_name;
	}
	
}


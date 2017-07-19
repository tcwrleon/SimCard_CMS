package com.sunyard.entity.system;

import java.io.Serializable;

/**
 * @author mumu
 *
 */
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 9043704646309653134L;
	private String user_id;
	private String user_name;
	private String user_pwd;
	private String user_type;
	private String user_state;
	private String create_date;
	private String pwd_modify_date;
	private String user_desc;
	private String department_id;
	private String department_name;
	private String initpwd;
	private String user_phone;
	private String user_email;
	private String user_addr;
	private String user_mobile;
	private String user_certno;
	private String cert_type;
	private String modi_time;
	private String modi_user;
	private int pwd_error_num;
	
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

	public String getUser_pwd() {
		return user_pwd;
	}

	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public String getUser_state() {
		return user_state;
	}

	public void setUser_state(String user_state) {
		this.user_state = user_state;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getPwd_modify_date() {
		return pwd_modify_date;
	}

	public void setPwd_modify_date(String pwd_modify_date) {
		this.pwd_modify_date = pwd_modify_date;
	}

	public String getUser_desc() {
		return user_desc;
	}

	public void setUser_desc(String user_desc) {
		this.user_desc = user_desc;
	}

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	public String getInitpwd() {
		return initpwd;
	}

	public void setInitpwd(String initpwd) {
		this.initpwd = initpwd;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_addr() {
		return user_addr;
	}

	public void setUser_addr(String user_addr) {
		this.user_addr = user_addr;
	}

	public String getUser_mobile() {
		return user_mobile;
	}

	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}

	public String getUser_certno() {
		return user_certno;
	}

	public void setUser_certno(String user_certno) {
		this.user_certno = user_certno;
	}

	public String getCert_type() {
		return cert_type;
	}

	public void setCert_type(String cert_type) {
		this.cert_type = cert_type;
	}

	public String getModi_time() {
		return modi_time;
	}

	public void setModi_time(String modi_time) {
		this.modi_time = modi_time;
	}

	public String getModi_user() {
		return modi_user;
	}

	public void setModi_user(String modi_user) {
		this.modi_user = modi_user;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public int getPwd_error_num() {
		return pwd_error_num;
	}

	public void setPwd_error_num(int pwd_error_num) {
		this.pwd_error_num = pwd_error_num;
	}

	@Override
	public String toString() {
		return "用户ID=" + getUser_id() + ",用户名称=" + getUser_name();
	}
}

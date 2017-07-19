package com.sunyard.entity.system;

import java.io.Serializable;

/**
 * @author mumu
 *
 */
public class PwdHistoryEntity implements Serializable {

	private static final long serialVersionUID = 9043704646309653134L;
	private String flow_id;
	private String login_pwd;
	private String create_time;
	private String user_name;
	private String platform_type;
	
	public String getFlow_id() {
		return flow_id;
	}
	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}
	public String getLogin_pwd() {
		return login_pwd;
	}
	public void setLogin_pwd(String login_pwd) {
		this.login_pwd = login_pwd;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getPlatform_type() {
		return platform_type;
	}
	public void setPlatform_type(String platform_type) {
		this.platform_type = platform_type;
	}

}

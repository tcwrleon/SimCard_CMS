package com.sunyard.entity.business;

public class SimCardEntity {

	private String sim_id;
	private String mobile;
	private String user_name;
	private String id_card;
	private String service_pwd;
    private String operators;
	private String province;
	private String city;
	private String author;
	private String final_at;
	private String final_action;
	private String final_update_time;
	private String remark;

    public String getOperators() {
        return operators;
    }

    public void setOperators(String operators) {
        this.operators = operators;
    }

    public String getSim_id() {
		return sim_id;
	}

	public void setSim_id(String sim_id) {
		this.sim_id = sim_id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getId_card() {
		return id_card;
	}

	public void setId_card(String id_card) {
		this.id_card = id_card;
	}

	public String getService_pwd() {
		return service_pwd;
	}

	public void setService_pwd(String service_pwd) {
		this.service_pwd = service_pwd;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

    public String getFinal_at() {
        return final_at;
    }

    public void setFinal_at(String final_at) {
        this.final_at = final_at;
    }

    public String getFinal_action() {
		return final_action;
	}

	public void setFinal_action(String final_action) {
		this.final_action = final_action;
	}

	public String getFinal_update_time() {
		return final_update_time;
	}

	public void setFinal_update_time(String final_update_time) {
		this.final_update_time = final_update_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}

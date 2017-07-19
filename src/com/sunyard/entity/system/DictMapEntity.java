package com.sunyard.entity.system;

public class DictMapEntity implements java.io.Serializable {
	private static final long serialVersionUID = 3730900107712307203L;
	private String sun_key;
	private String prompt;

	public String getSun_key() {
		return sun_key;
	}

	public void setSun_key(String sun_key) {
		this.sun_key = sun_key;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	
	@Override
	public String toString() {
		return "主键：" + getSun_key()+ "||数据字典名称：" + getPrompt();
	}

}


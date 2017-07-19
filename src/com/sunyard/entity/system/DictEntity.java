package com.sunyard.entity.system;

public class DictEntity implements java.io.Serializable {
	private static final long serialVersionUID = 3730900107712307203L;
	private String sun_key;
	private String val;
	private String prompt;
	private int sort;
	private DictMapEntity dictMap;

	public String getSun_key() {
		return sun_key;
	}

	public void setSun_key(String sun_key) {
		this.sun_key = sun_key;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}


	
	public DictMapEntity getDictMap() {
		return dictMap;
	}

	public void setDictMap(DictMapEntity dictMap) {
		this.dictMap = dictMap;
	}
	
	@Override
	public String toString() {
		return "主键：" + getSun_key()+ "||值：" + getVal() + "||值的中文：" + getPrompt();
	}


	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}
}


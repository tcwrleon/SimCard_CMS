
package com.sunyard.enums;

/**
 * @author mumu
 * 用户状态
 * 0普通 1授权
 */
public enum USERTYPE {
	General("0") ,Authorize("1");
	
	private String code;
	
	private USERTYPE(String code){
		this.code= code;
	}
	
	public String getCode(){
		return code;
	}
}

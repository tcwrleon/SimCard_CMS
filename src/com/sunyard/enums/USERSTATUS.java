
package com.sunyard.enums;

/**
 * @author mumu
 * 用户状态
 * 0正常 1锁定
 */
public enum USERSTATUS {
	Normal("0") ,Lock("1");
	
	private String code;
	
	private USERSTATUS(String code){
		this.code= code;
	}
	
	public String getCode(){
		return code;
	}
}


package com.sunyard.enums;

/**
 * @author mumu
 * 用户状态
 * 0身份证 1护照
 */
public enum CERTTYPE {
	IdCard("0") ,Passport("1");
	
	private String code;
	
	private CERTTYPE(String code){
		this.code= code;
	}
	
	public String getCode(){
		return code;
	}
}

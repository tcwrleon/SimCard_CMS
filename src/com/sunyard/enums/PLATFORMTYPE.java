
package com.sunyard.enums;

/*
 * 平台类型
 * 	0--管理台
	1--互联网端
 */
public enum PLATFORMTYPE {
	Background("0") ,Internet("1");
	
	private String code;
	
	private PLATFORMTYPE(String code){
		this.code= code;
	}
	
	public String getCode(){
		return code;
	}
}


package com.sunyard.enums;

/*
 * 资源状态
 * 	0--启用
	1--禁用
 */
public enum RESOURCESTT {
	Valid("0") ,Invalid("1");
	
	private String code;
	
	private RESOURCESTT(String code){
		this.code= code;
	}
	
	public String getCode(){
		return code;
	}
}

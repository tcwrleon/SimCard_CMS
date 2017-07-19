
package com.sunyard.enums;

/**
 * @author mumu
 * 登录密码未修改标志位
 * 默认0未修改,1已修改
 */
public enum INITPWD {
	UnModi("0"),HasModi("1");
	
	private String code;
	
	private INITPWD(String code){
		this.code= code;
	}
	
	public String getCode(){
		return code;
	}
}

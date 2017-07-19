
package com.sunyard.enums;

/*
 * 资源类型
 * 	0--模块
	1--菜单
	2--按钮
 */
public enum RESOURCETYPE {
	Module("0") ,Menu("1"),Button("2");
	
	private String code;
	
	private RESOURCETYPE(String code){
		this.code= code;
	}
	
	public String getCode(){
		return code;
	}
}


package com.sunyard.enums;

/*
 * 部门（机构）状态
 * 	0--冻结
	1--激活
	2--逻辑删除
 */
public enum DPTSTATUS {
	Frozen("0") ,Activate("1"),LogicDelete("2");
	
	private String code;
	
	private DPTSTATUS(String code){
		this.code= code;
	}
	
	public String getCode(){
		return code;
	}
}


package com.sunyard.enums;

/*
 * 部门（机构）级别
 * 	0--总行
	1--分行
	2--支行
 */
public enum DPTLEVEL {
	HeadOffice("0") ,Branch("1"),Subbranch ("2");
	
	private String code;
	
	private DPTLEVEL(String code){
		this.code= code;
	}
	
	public String getCode(){
		return code;
	}
}

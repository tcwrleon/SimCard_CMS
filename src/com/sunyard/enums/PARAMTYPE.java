
package com.sunyard.enums;

/**
 * @author mumu
 * 系统参数类型
 * 0 系统级别，1产品类级别，2单产品级别
 */
public enum PARAMTYPE {
	System("0"),ProdCategory("1"),ProdSingle("2");
	
	private String code;
	
	private PARAMTYPE(String code){
		this.code= code;
	}
	
	public String getCode(){
		return code;
	}
}

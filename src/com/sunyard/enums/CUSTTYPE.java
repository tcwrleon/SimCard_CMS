
package com.sunyard.enums;

/**
 * @author mumu
 * 互联网端客户类型
 * 0永明客户经理 1授权代表 2在线客服
 */
public enum CUSTTYPE {
	YongMingUser("0") ,AuthorizeUser("1"),OnlineCust("2");
	
	private String code;
	
	private CUSTTYPE(String code){
		this.code= code;
	}
	
	public String getCode(){
		return code;
	}
}

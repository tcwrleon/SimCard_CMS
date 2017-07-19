
package com.sunyard.enums;

/**
 * 0 - 未激活
 * 1 - 已激活
 * 2 - 作业暂停
 * 3 - 作业成功
 * 4 - 作业失败
 * 5 - 作业中断
 * Z - 正在处理 
 *
 */

public enum DEALSTATUS {
	
	UnActivated("0"),Activated("1"),Pending("2"),Success("3"),Failure("4"),Abort("5"),Processing("Z");
	
	private String code;
	
	private DEALSTATUS(String code){
		this.code=code;
	}
	
	public String getCode(){
		return code;
	}
	
}

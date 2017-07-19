package com.sunyard.entity.system;

public class RunLogEntity {
	private String runlog_id;
	private String run_time;
	private String system_type;//管理台    互联网端
	private String system_action;//系统启动  系统关闭
	
	public String getRunlog_id() {
		return runlog_id;
	}
	public void setRunlog_id(String runlog_id) {
		this.runlog_id = runlog_id;
	}
	public String getRun_time() {
		return run_time;
	}
	public void setRun_time(String run_time) {
		this.run_time = run_time;
	}
	public String getSystem_type() {
		return system_type;
	}
	public void setSystem_type(String system_type) {
		this.system_type = system_type;
	}
	public String getSystem_action() {
		return system_action;
	}
	public void setSystem_action(String system_action) {
		this.system_action = system_action;
	}
}

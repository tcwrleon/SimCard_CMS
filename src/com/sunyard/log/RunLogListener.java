package com.sunyard.log;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sunyard.entity.system.RunLogEntity;
import com.sunyard.enums.PLATFORMTYPE;
import com.sunyard.service.system.RunLogService;
import com.sunyard.util.DateUtil;
import com.sunyard.util.SpringUtil;

/**
 * 系统运行日志（启动与关闭）
 * @author mumu
 *
 */
public class RunLogListener implements ServletContextListener{
	private RunLogService runLogService;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		saveLog("系统关闭");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		runLogService = (RunLogService)SpringUtil.getBean("runLogService");
		saveLog("系统启动");
	}
	
	private void saveLog(String runDesc){
		RunLogEntity runLog = new RunLogEntity();
		runLog.setRun_time(DateUtil.todayStr()+ " " + DateUtil.curTimeStr());//当前时间
		runLog.setSystem_type(PLATFORMTYPE.Background.getCode());//管理台
		runLog.setSystem_action(runDesc);
		try {
			runLogService.add(runLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

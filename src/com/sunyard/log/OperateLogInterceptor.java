package com.sunyard.log;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

import com.opensymphony.xwork2.ActionContext;
import com.sunyard.base.model.Consts;
import com.sunyard.entity.system.OperLogEntity;
import com.sunyard.entity.system.UserEntity;
import com.sunyard.log.annotation.OperLoggable;
import com.sunyard.service.system.OperLogService;
import com.sunyard.util.Common;
import com.sunyard.util.DateUtil;
import com.sunyard.util.ParamUtil;

/**
 * 记录用户操作轨迹（日志）
 * @author mumu
 *
		*/
@Aspect
@Service
public class OperateLogInterceptor {
	@Resource
	private OperLogService operLogService;
	private Logger logger = Logger.getLogger(OperateLogInterceptor.class);
	
	@Around(value="@annotation(annotation)")
	public Object wrapMethod(final ProceedingJoinPoint point,final OperLoggable annotation) throws Throwable {
		Map<String,Object> session =  (Map<String,Object>)ActionContext.getContext().getSession();
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
		OperLogEntity operLog = new OperLogEntity(); 
		String className = point.getTarget().getClass().getSimpleName();
		String methodName = point.getSignature().getName();
		
		UserEntity user = (UserEntity)session.get(Consts.USER_SESSION);
		if(user != null){
			logger.info("用户名=" + user.getUser_name() + "，用户编号=" + user.getUser_id());
			operLog.setUser_name(user.getUser_name());
			operLog.setUser_id(user.getUser_id());
		}
		
		logger.info("注解模块="+ annotation.module() + ",描述=" + annotation.description());
		if(annotation != null){
			operLog.setModule_name(annotation.module());
			operLog.setOper_desc(annotation.description());
		}
		
		operLog.setUser_ip(Common.toIpAddr(request));
		operLog.setClass_name(className);
		operLog.setMethod_name(methodName);
		operLog.setOper_param(ParamUtil.param2Map(request).toString());
		operLog.setOper_date(DateUtil.todayStr()+ " " + DateUtil.curTimeStr());
		
		Object result = null;
		try {
			result = point.proceed();
			operLog.setOper_result("操作成功");
			operLog.setError_info("");
		}catch (Exception e) {
			operLog.setError_info(getStackTraceAsString(e));
			operLog.setOper_result("操作失败");
		}finally{
			saveOperLogExecutor(operLog);
		}
		return result;
	}
	
	private void saveOperLogExecutor(OperLogEntity log){
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(new SaveLogThread(log));
		executor.shutdown();
		logger.info("插入操作日志之前");
	}
	
	private class SaveLogThread implements Runnable{
		private OperLogEntity log;
		
		public SaveLogThread(OperLogEntity log) {
			this.log = log;
		}
		@Override
		public void run() {
			try {
				operLogService.add(log);
				logger.info("插入操作日志之后");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private String getStackTraceAsString(Throwable e) {
		if (e == null){
			return "";
		}
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
}

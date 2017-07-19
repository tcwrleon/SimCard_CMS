package com.sunyard.interceptor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.sunyard.base.model.Consts;

public class ExceptionInterceptor extends MethodFilterInterceptor {

	private static final long serialVersionUID = 6905417537239674995L;

	@Override
	protected String doIntercept(ActionInvocation actioninvocation) {
		String result = null; // Action的返回值
		try {
			// 运行被拦截的Action,期间如果发生异常会被catch住
			result = actioninvocation.invoke();
			return result;
		} catch (Exception e) {
			String errorMsg = "系统发生异常！";
			String errorShow="系统发生异常！";
			
			if(e instanceof DataAccessException) {//数据库操作异常
				DataAccessException ee = (DataAccessException) e;
				ee.printStackTrace();
				if (ee.getMessage() != null|| !"".equals(ee.getMessage().trim())) {
					errorMsg = ee.getMessage().trim();
				}
				errorShow="数据库操作异常！";
			}else if (e instanceof NullPointerException) {//空指针异常
				NullPointerException ee = (NullPointerException) e;
				ee.printStackTrace();
				if (ee.getMessage() != null|| !"".equals(ee.getMessage().trim())) {
					errorMsg = ee.getMessage().trim();
				}
				errorShow="空指针异常！";
			}else if (e instanceof IOException) {//IO异常
				IOException ee = (IOException) e;
				ee.printStackTrace();
				if (ee.getMessage() != null|| !"".equals(ee.getMessage().trim())) {
					errorMsg = ee.getMessage().trim();
				}
				errorShow="读写异常！";
			}else if (e instanceof ArithmeticException) {//数学运算异常
				ArithmeticException ee = (ArithmeticException) e;
				ee.printStackTrace();
				if (ee.getMessage() != null|| !"".equals(ee.getMessage().trim())) {
					errorMsg = ee.getMessage().trim();
				}
				errorShow="数学运算异常";
			}else if (e instanceof ArrayIndexOutOfBoundsException) {//数组下标越界
				ArrayIndexOutOfBoundsException ee = (ArrayIndexOutOfBoundsException) e;
				ee.printStackTrace();
				if (ee.getMessage() != null|| !"".equals(ee.getMessage().trim())) {
					errorMsg = ee.getMessage().trim();
				}
				errorShow="数组下标越界";
			}else if (e instanceof IllegalArgumentException) {//方法参数错误
				IllegalArgumentException ee = (IllegalArgumentException) e;
				ee.printStackTrace(); 
				if (ee.getMessage() != null|| !"".equals(ee.getMessage().trim())) {
					errorMsg = ee.getMessage().trim();
				}
				errorShow="方法参数错误";
			}else if (e instanceof ClassCastException) {//类转换异常
				ClassCastException ee = (ClassCastException) e;
				ee.printStackTrace(); 
				if (ee.getMessage() != null|| !"".equals(ee.getMessage().trim())) {
					errorMsg = ee.getMessage().trim();
				}
				errorShow="类转换异常";
			}else if (e instanceof SecurityException) {//违背安全原则异常
				SecurityException ee = (SecurityException) e;
				ee.printStackTrace();
				if (ee.getMessage() != null|| !"".equals(ee.getMessage().trim())) {
					errorMsg = ee.getMessage().trim();
				}
				errorShow="违背安全原则异常";
			}else if (e instanceof FileNotFoundException) {//违背安全原则异常
				SecurityException ee = (SecurityException) e;
				ee.printStackTrace();
				if (ee.getMessage() != null|| !"".equals(ee.getMessage().trim())) {
					errorMsg = ee.getMessage().trim();
				}
				errorShow="文件不存在";
			}else if (e instanceof SQLException) {//操作数据库异常
				SQLException ee = (SQLException) e;
				ee.printStackTrace();
				if (ee.getMessage() != null|| !"".equals(ee.getMessage().trim())) {
					errorMsg = ee.getMessage().trim();
				}
			}else if (e instanceof RuntimeException) {// 未知的运行时异常
				RuntimeException re = (RuntimeException) e;
				re.printStackTrace();
			}else {
				e.printStackTrace();
			}
			
			HttpServletRequest request = (HttpServletRequest) actioninvocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
			// 发送错误消息到页面
			request.setAttribute(Consts.TIP_MSG, errorShow);

			//log4j记录日志
			Logger logger = LoggerFactory.getLogger(this.getClass());
			if (e.getCause() != null) {
				logger.error(errorMsg, e);
			} else {
				logger.error(errorMsg, e);
			}

			return Consts.ERROR;
		}
	}
}

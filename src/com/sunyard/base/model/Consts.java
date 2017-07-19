package com.sunyard.base.model;

import com.sunyard.util.PropertiesUtil;

/**
 * @author mumu
 * 用来保存config配置文件产量
 */
public class Consts {
	
	/**
	 * 公共跳转页面：错误页面
	 */
	public static final String ERROR =  "error";
	/**
	 * 公共跳转页面：错误页面（弹出层页面）
	 */
	public static final String LAYER_ERROR =  "layerError";
	
	/**
	 * 公共跳转页面：成功页面
	 */
	public static final String SUCCESS =  "success";
	
	/**
	 * 公共跳转页面：成功页面(弹出层)
	 */
	public static final String LAYER_SUCCESS =  "layerSuccess";
	
	/**
	 * 公共跳转页面：报表查询页面
	 */
	public static final String REPORT =  "report";
	
	/**
	 * 页面常量：提示语key
	 */
	public static final String TIP_MSG =  "msg";
	
	/**
	 * 页面常量：会话用户的Key
	 */
	public static final String USER_SESSION =  "userSession";
	
	/**
	 * 公共跳转页面：登陆页面
	 */
	public static final String LOGIN =  "login";
	
	/**
	 * 页面常量：参数常量
	 */
	public static final String PARAM_COVER = "paramCover";
	
	/**
	 * 页面常量：返回错误码
	 */
	public static final String ERROR_CODE = "999999";
	
	/**
	 * 页面常量：返回成功码
	 */
	public static final String SUCCESS_CODE = "000000";
	
	/**
	 * 系统参数：预设分页记录
	 */
	public static final String PAGE_SIZE = "PAGE_SIZE";
	
	/**
	 * 系统参数：查询前N条记录
	 */
	public static final String QUERY_NUM = "QUERY_NUM";
	
	/**
	 * 系统参数：日期间隔（单位：月）
	 */
	public static final String MONTH_INV = "MONTH_INV";
	
	/**
	 * 系统参数：密码错误次数
	 */
	public static final String PWD_ERROR_TIMES = "PWD_ERROR_TIMES";
	
	/**
	 * 系统参数：默认登录密码
	 */
	public static final String DEFAULT_LOGIN_PWD = "DEFAULT_LOGIN_PWD";
	
	/**
	 * 系统参数:配置法律文件上传路径-默认c:user
	 */
	public static final String UPLOAD_PATH = "UPLOAD_PATH";
	
	/**
	 * 系统参数：登录密码有效期（天）
	 */
	public static final String PWD_VALID_PERIOD = "PWD_VALID_PERIOD";
	
	/**
	 * 配置文件常量：日期格式
	 */
	public static final String DATE_FORMATTER = PropertiesUtil.getString("DATE_FORMATTER");
		
	/**
	 * 配置超级管理员的用户名
	 */
	public static final String ADMIN_USER = PropertiesUtil.getString("ADMIN_USER");
	
	/**
	 * 配置文件常量：树的前缀
	 */
	public static final String TREE_PREFIX = PropertiesUtil.getString("TREE_PREFIX"); 
	
	/**
	 * 配置法律Excel上传路径：默认c:excel
	 */
	public static String EXCEL_UPLOAD_PATH = PropertiesUtil.getString("EXCEL_UPLOAD_PATH");

	/**
	 * 配置法律ZIP_PATH路径
	 */
	public static String LOG_PATH = PropertiesUtil.getString("logPath");

	/**
	 * 配置法律ZIP_PATH路径
	 */
	public static String ZIP_PATH = PropertiesUtil.getString("zipPath");


	/**
	 * 配置法律CMS_ZIP_FILE路径
	 */
	public static String CMS_ZIP_FILE = PropertiesUtil.getString("cmszipFile");


	/**
	 * 配置法律WEB_LOG_PATH路径
	 */
	public static String WEB_LOG_PATH = PropertiesUtil.getString("weblogPath");

	/**
	 * 配置法律WEB_LOG_PATH路径
	 */
	public static String WEB_ZIP_FILE = PropertiesUtil.getString("webzipFile");

	/**
	 * 交易代码：清算开始
	 */
	public static final String BATCH_START = "t00038";

	/**
	 * 交易代码：清算结束
	 */
	public static final String BATCH_END = "t00039";

	/**
	 * 交易代码：产品终止
	 */
	public static final String PROD_END = "t00045";

	/**
	 * 交易代码：债权转让失效
	 */
	public static final String INVALID = "t00044";

	/**
	 * 交易代码：债权转让失效
	 */
	public static final String FILING = "t00047";

}

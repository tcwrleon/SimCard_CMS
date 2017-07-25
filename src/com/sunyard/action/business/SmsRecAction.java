package com.sunyard.action.business;

import com.sunyard.action.UpdownloadAction;
import com.sunyard.base.model.Consts;
import com.sunyard.datasource.CustomerContextHolder;
import com.sunyard.entity.business.SmsRecEntity;
import com.sunyard.pulgin.PagePlugin;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.business.SmsRecService;
import com.sunyard.util.PhoneUtil;
import com.sunyard.util.ParamUtil;
import com.sunyard.util.StringUtil;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
@Scope("prototype")
public class SmsRecAction extends UpdownloadAction{
	HttpServletRequest request = ServletActionContext.getRequest();
	@Resource
	private SmsRecService smsRecService;
	private SmsRecEntity smsRecEntity;
	
	private String[] keys = {};

	/**
	 * 查询最新一条手机号的验证码
	 *
	 * @return
	 * @throws Exception
	 */
	public String queryCode() throws Exception {
		String simnum = ParamUtil.get(request, "simnum");
		String checkCode = "";
		PrintWriter out = response.getWriter();

		if(PhoneUtil.isChinaCMCCPhoneNum(simnum)){
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_CATPOOLCMCC);
			checkCode = smsRecService.queryCode(simnum);
		}else if(PhoneUtil.isChinaCTCCPhoneNum(simnum)){
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_CATPOOLCTCC);
			checkCode = smsRecService.queryCode(simnum);
		}else if(PhoneUtil.isChinaCUCCPhoneNum(simnum)){
			CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_CATPOOLCUCC);
			checkCode = smsRecService.queryCode(simnum);
		}

		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_MAIN);
		if(StringUtil.isEmpty(checkCode)) checkCode = Consts.CODE_NONE;
		out.write(checkCode);
		return null;
	}


	/**
	 * 查询移动短信
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String toQueryCMCC() throws Exception {
        Map<String,String> map = new HashMap<String,String>();
        String simnum = ParamUtil.get(request, "simnum");

        map.put("simnum", simnum);
        logger.info("map====="+map);
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_CATPOOLCMCC);
		PagePlugin.setDialect("access");
        PageView page = smsRecService.querySmsRecInfo(getPageView(), map);
		PagePlugin.setDialect("mysql");  //手动重置方言
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_MAIN);
        this.getDirtMap(keys);
        request.setAttribute("page", page);
        request.setAttribute("simCardInfo", map);
        getBtnHtmlByMenuId();//初始化页面操作按钮

        return "queryCMCC";
	}

	/**
	 * 查询电信短信
	 *
	 * @return
	 * @throws Exception
	 */
	public String toQueryCTCC() throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		String simnum = ParamUtil.get(request, "simnum");

		map.put("simnum", simnum);
		logger.info("map====="+map);
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_CATPOOLCTCC);
		PagePlugin.setDialect("access");
		PageView page = smsRecService.querySmsRecInfo(getPageView(), map);
		PagePlugin.setDialect("mysql");  //手动重置方言
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_MAIN);
		this.getDirtMap(keys);
		request.setAttribute("page", page);
		request.setAttribute("simCardInfo", map);
		getBtnHtmlByMenuId();//初始化页面操作按钮

		return "queryCTCC";
	}

	/**
	 * 查询联通短信
	 *
	 * @return
	 * @throws Exception
	 */
	public String toQueryCUCC() throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		String simnum = ParamUtil.get(request, "simnum");

		map.put("simnum", simnum);
		logger.info("map====="+map);
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_CATPOOLCUCC);
		PagePlugin.setDialect("access");
		PageView page = smsRecService.querySmsRecInfo(getPageView(), map);
		PagePlugin.setDialect("mysql");  //手动重置方言
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_MAIN);
		this.getDirtMap(keys);
		request.setAttribute("page", page);
		request.setAttribute("simCardInfo", map);
		getBtnHtmlByMenuId();//初始化页面操作按钮

		return "queryCUCC";
	}

	/**
	 * 删除功能
	 * @return
	 */
	public String deleteCMCC() throws Exception{
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_CATPOOLCMCC);
		PagePlugin.setDialect("access");
        String[] ids = ParamUtil.get(request, "selectedId").split(",");
        for(String id : ids) {
            smsRecService.deleteSMS(id);
        }
		PagePlugin.setDialect("mysql");  //手动重置方言
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_MAIN);
        request.setAttribute("_backUrl", "smsRecAction_toQueryCMCC");
        request.setAttribute(Consts.TIP_MSG, "删除成功！");
        return Consts.SUCCESS;

	}

	/**
	 * 删除功能
	 * @return
	 */
	public String deleteCTCC() throws Exception{
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_CATPOOLCTCC);
		PagePlugin.setDialect("access");
        String[] ids = ParamUtil.get(request, "selectedId").split(",");
        for(String id : ids) {
            smsRecService.deleteSMS(id);
        }
		PagePlugin.setDialect("mysql");  //手动重置方言
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_MAIN);
        request.setAttribute("_backUrl", "smsRecAction_toQueryCTCC");
        request.setAttribute(Consts.TIP_MSG, "删除成功！");
        return Consts.SUCCESS;

	}


	/**
	 * 删除功能
	 * @return
	 */
	public String deleteCUCC() throws Exception{
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_CATPOOLCUCC);
		PagePlugin.setDialect("access");
		String[] ids = ParamUtil.get(request, "selectedId").split(",");
		for(String id : ids) {
			smsRecService.deleteSMS(id);
		}
		PagePlugin.setDialect("mysql");  //手动重置方言
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_MAIN);
		request.setAttribute("_backUrl", "smsRecAction_toQueryCUCC");
		request.setAttribute(Consts.TIP_MSG, "删除成功！");
		return Consts.SUCCESS;

	}

	public SmsRecEntity getSmsRecEntity() {
		return smsRecEntity;
	}

	public void setSmsRecEntity(SmsRecEntity smsRecEntity) {
		this.smsRecEntity = smsRecEntity;
	}
}

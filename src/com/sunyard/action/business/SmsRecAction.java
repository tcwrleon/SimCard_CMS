package com.sunyard.action.business;

import com.sunyard.action.UpdownloadAction;
import com.sunyard.base.model.Consts;
import com.sunyard.entity.business.SmsRecEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.business.SmsRecService;
import com.sunyard.util.ExcelUtil;
import com.sunyard.util.ParamUtil;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
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
	 * 查询平台信息
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String toQuery() throws Exception {
        Map<String,String> map = new HashMap<String,String>();
        String simnum = ParamUtil.get(request, "simnum");

        map.put("simnum", simnum);
        logger.info("map====="+map);
        PageView page = smsRecService.querySmsRecInfo(getPageView(), map);
        this.getDirtMap(keys);
        request.setAttribute("page", page);
        request.setAttribute("simCardInfo", map);
        getBtnHtmlByMenuId();//初始化页面操作按钮

        return "query";
	}

	/**
	 * 删除功能
	 * @return
	 */
	public String delete() throws Exception{
        String[] ids = ParamUtil.get(request, "selectedId").split(",");
        for(String id : ids) {
            smsRecService.deleteSMS(id);
        }
        request.setAttribute("_backUrl", "smsRecAction_toQuery");
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

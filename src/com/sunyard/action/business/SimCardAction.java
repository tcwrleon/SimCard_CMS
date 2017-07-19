package com.sunyard.action.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.sunyard.util.ExcelUtil;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sunyard.action.UpdownloadAction;
import com.sunyard.base.model.Consts;
import com.sunyard.entity.business.SimCardEntity;
import com.sunyard.entity.system.UserEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.business.SimCardService;
import com.sunyard.util.ParamUtil;

@Controller
@Scope("prototype")
public class SimCardAction extends UpdownloadAction{
	HttpServletRequest request = ServletActionContext.getRequest();
	@Resource
	private SimCardService simCardService;
	private SimCardEntity simCardEntity;
	
	private String[] keys = {DICTKEY.SIM_OPERATORS.toString(),DICTKEY.SIM_AUTHOR.toString(),
			DICTKEY.SIM_FINAL_AT.toString(),DICTKEY.SIM_FINAL_ACTION.toString()};

	/**
	 * 查询平台信息
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String toQuery() throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		String sim_id = ParamUtil.get(request, "sim_id");
		String operators = ParamUtil.get(request, "operators");

		map.put("sim_id", sim_id);
		map.put("operators", operators);
		logger.info("map====="+map);
		PageView page = simCardService.querySimCardInfo(getPageView(), map);
		this.getDirtMap(keys);
		request.setAttribute("page", page);
		request.setAttribute("smsRecInfo", map);
		getBtnHtmlByMenuId();//初始化页面操作按钮

		return "query";
	}
	
	/**
	 * 跳转到新增页面
	 * @return
	 * @throws Exception
	 */
	public String toAdd() throws Exception{
		this.getDirtMap(keys);
		return "add";
	}

	/**
	 * 新增方法
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception{
		simCardService.add(simCardEntity);
		request.setAttribute("_backUrl", "simCardAction_toQuery");
		request.setAttribute(Consts.TIP_MSG, "新增成功！");
		return Consts.SUCCESS;
	}

	/**
	 * 跳转到详情页面
	 * @return
	 * @throws Exception
	 */
	public String toDetail() throws Exception{
		SimCardEntity simCardInfo = simCardService.getById(ParamUtil.get(request, "selectedId"));
		this.getDirtMap(keys);
		request.setAttribute("simCardInfo", simCardInfo);
		request.setAttribute("flag", "look");
		return "look";
	}	
	/**
	 * 跳转到修改页面
	 * @return
	 * @throws Exception
	 */
	public String toModi() throws Exception{
		SimCardEntity simCardInfo = simCardService.getById(ParamUtil.get(request, "selectedId"));
		this.getDirtMap(keys);
		request.setAttribute("simCardInfo", simCardInfo);
		request.setAttribute("flag", "modi");
		return "modi";
	}	

	/**
	 * 修改功能
	 * @return
	 * @throws Exception
	 */
	public String modi() throws Exception{
        simCardService.update(simCardEntity);
        request.setAttribute("_backUrl", "simCardAction_toQuery");
        request.setAttribute(Consts.TIP_MSG, "修改成功！");
        return Consts.SUCCESS;
	}

	/**
	 * 删除功能
	 * @return
	 */
	public String delete() throws Exception{
        simCardService.delete(ParamUtil.get(request, "selectedId"));
        request.setAttribute("_backUrl", "simCardAction_toQuery");
        request.setAttribute(Consts.TIP_MSG, "删除成功！");
        return Consts.SUCCESS;

	}

	/**
	 * 导出Excel
	 *
	 * @return
	 * @throws Exception
	 */
	public void doExport() throws Exception {
        Map<String,String> map = new HashMap<String,String>();
        String sim_id = ParamUtil.get(request, "sim_id");
        String operators = ParamUtil.get(request, "operators");
        map.put("sim_id", sim_id);
        map.put("operators", operators);

		List<SimCardEntity> simCardList = simCardService.queryforExport(map);
        Map<String,String> params = new HashMap<String, String>();

		ExcelUtil.excelExport(simCardList,"sim_id,mobile,user_name,id_card,service_pwd,operators,province,city","sim卡编号,手机号,姓名,身份证号,服务密码,运营商,省份,城市", params, response,"sim卡信息");
	}

    public SimCardEntity getSimCardEntity() {
        return simCardEntity;
    }

    public void setSimCardEntity(SimCardEntity simCardEntity) {
        this.simCardEntity = simCardEntity;
    }
}

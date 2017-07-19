package com.sunyard.action.system;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sunyard.action.BaseAction;
import com.sunyard.base.model.Consts;
import com.sunyard.entity.system.SysParamEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.enums.PARAMTYPE;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.SysParamService;
import com.sunyard.util.ParamUtil;
import com.sunyard.util.StringUtil;


/**
 * @author mumu
 */
@Controller
@Scope("prototype")
public class SysParamAction extends BaseAction{
	private SysParamEntity sysParamEntity;
	@Resource
	private SysParamService sysParamService;
	private static final String SELECTEDID = "selectedId";
	private String[] keys = {DICTKEY.K_PARAMTYPE.toString(),DICTKEY.K_PRODTYPE.toString()};
	private static final String NullMsg = "参数类别、参数ID、参数名称和值不能为空！";
	private static final String ProdSingleNullMsg = "产品代码不能为空！";
	/**
	 * 非空校验
	 * @return
	 */
	private boolean isSysParamNull(){
		if(sysParamEntity == null) return true;
		
		if(StringUtil.isEmpty(sysParamEntity.getParam_type()) || StringUtil.isEmpty(sysParamEntity.getParam_code()) ||
				StringUtil.isEmpty(sysParamEntity.getParam_name()) || StringUtil.isEmpty(sysParamEntity.getParam_value())){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据参数类别重置相关属性
	 */
	private boolean resetPropertyByParamType(){
		if(PARAMTYPE.System.getCode().equals(sysParamEntity.getParam_type())){//系统级别
			sysParamEntity.setProd_type("");
			sysParamEntity.setProd_id("");
		}else if(PARAMTYPE.ProdCategory.getCode().equals(sysParamEntity.getParam_type())){//产品类级别
			sysParamEntity.setProd_id("");
		}else if(PARAMTYPE.ProdSingle.getCode().equals(sysParamEntity.getParam_type())){//单产品级别
			sysParamEntity.setProd_type("");
			if(StringUtil.isEmpty(sysParamEntity.getProd_id())) return false;
		}
		return true;
	}
	/**
	 * 查询
	 * 支持分页查询
	 * @return
	 * @throws Exception
	 */
	public String toQuery() throws Exception{
		if(sysParamEntity == null){
			sysParamEntity = new SysParamEntity();
		}
		
		PageView page = sysParamService.query(getPageView(), sysParamEntity); //分页查询
		
		request.setAttribute("page", page);
		request.setAttribute("back", sysParamEntity);//查询条件反显
		getDirtMap(keys);//初始化查询条件的下拉框内容
		getBtnHtmlByMenuId();
		return "query";
	}
	
	/**
	 * 跳转到新增页面
	 * 初始化页面下拉框内容
	 * @return
	 * @throws Exception
	 */
	public String toAdd() throws Exception{
		getDirtMap(keys);
		return "add";
	}
	
	/**
	 * 新增
	 * 非空校验;参数ID唯一性校验；插入数据库
	 * @return
	 * @throws Exception 
	 */
	public String add() throws Exception{
		if(isSysParamNull()){
			request.setAttribute(Consts.TIP_MSG,NullMsg);
			return Consts.ERROR;
		}
		
		if(!resetPropertyByParamType()){
			request.setAttribute(Consts.TIP_MSG,ProdSingleNullMsg);
			return Consts.ERROR;
		}
		
		SysParamEntity sysParam = sysParamService.getById(sysParamEntity.getParam_code());
		if(sysParam != null){
			request.setAttribute(Consts.TIP_MSG, "参数ID为" + sysParamEntity.getParam_code() + "已存在！");
			return Consts.ERROR;
		}
		
		sysParamService.add(sysParamEntity);
		request.setAttribute(Consts.TIP_MSG, "新增成功！");
		return Consts.LAYER_SUCCESS;
	}
	
	/**
	 * 跳转到修改页面
	 * 设置修改标识
	 * @return
	 * @throws Exception
	 */
	public String toModi() throws Exception{
		SysParamEntity sysParam = sysParamService.getById(ParamUtil.get(request, SELECTEDID));
		request.setAttribute("obj", sysParam);
		request.setAttribute("modiFlag", "modi");
		return toAdd();
	}
	
	/**
	 * 修改
	 * 非空校验；更新数据库
	 * @return
	 * @throws Exception
	 */
	public String modi() throws Exception{
		if(isSysParamNull()){
			request.setAttribute(Consts.TIP_MSG,NullMsg);
			return Consts.ERROR;
		}
		
		if(!resetPropertyByParamType()){
			request.setAttribute(Consts.TIP_MSG,ProdSingleNullMsg);
			return Consts.ERROR;
		}
		
		sysParamService.update(sysParamEntity);
		request.setAttribute(Consts.TIP_MSG, "修改成功！");
		return Consts.LAYER_SUCCESS;
	}
	
	/**
	 * 删除
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception{
		sysParamService.delete(ParamUtil.get(request, SELECTEDID));
		request.setAttribute(Consts.TIP_MSG, "删除成功！");
		return Consts.SUCCESS;
	}
	
	public SysParamEntity getSysParamEntity() {
		return sysParamEntity;
	}

	public void setSysParamEntity(SysParamEntity sysParamEntity) {
		this.sysParamEntity = sysParamEntity;
	}
	
	
}

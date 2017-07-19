package com.sunyard.action.system;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sunyard.action.BaseAction;
import com.sunyard.base.model.Consts;
import com.sunyard.entity.system.DictEntity;
import com.sunyard.entity.system.DictMapEntity;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.DictService;
import com.sunyard.util.Common;
import com.sunyard.util.ParamUtil;
import com.sunyard.util.StringUtil;

/**
 * @author mumu
 */
@Controller
@Scope("prototype")
public class DictAction extends BaseAction{
	private DictEntity dictEntity;
	private DictMapEntity dictMapEntity;
	@Resource
	private DictService dictService;
	
	/**
	 * 查询功能
	 * 支持分页模糊查询
	 * @return
	 */
	public String toQuery(){
		if(dictEntity == null){
			dictEntity = new DictEntity();
		}
		PageView page = dictService.query(getPageView(), dictEntity);//分页查询
		request.setAttribute("page", page);
		request.setAttribute("back", dictEntity);
		getBtnHtmlByMenuId();
		return "query";
	}
	
	/**
	 * 跳转到新增页面
	 * @return
	 */
	public String toAdd(){
		return "add";
	}
	
	/**
	 * 新增功能
	 * 先非空校验；值是否重复校验；插入数据库
	 * @return
	 */
	public String add(){
		String dictValues = ParamUtil.get(request, "val_str");
		String dictDescs = ParamUtil.get(request, "dict_desc_str");
		//非空校验
		if(dictMapEntity == null || StringUtil.isEmpty(dictMapEntity.getPrompt()) || 
				StringUtil.isEmpty(dictMapEntity.getSun_key()) || StringUtil.isEmpty(dictValues) || 
				StringUtil.isEmpty(dictDescs)){
			request.setAttribute(Consts.TIP_MSG, "数据字典名称、主键、值和值说明不能为空！");
			return Consts.ERROR;
		}
		//值是否重复校验
		if(StringUtil.toList(dictValues).size() != Common.removeSameItem(StringUtil.toList(dictValues)).size()){
			request.setAttribute(Consts.TIP_MSG, "输入值有重复！");
			return Consts.ERROR;
		}
		//新增
		String returnCode = dictService.addDict(dictMapEntity,dictValues,dictDescs);
		if(returnCode.equals(Consts.ERROR_CODE)){
			request.setAttribute(Consts.TIP_MSG, "输入值数据库已存在！");
			return Consts.ERROR;
		}else{
			request.setAttribute(Consts.TIP_MSG, "新增成功！");
			return Consts.LAYER_SUCCESS;
		}
	}
	
	/**
	 * 跳转到修改页面
	 * 先获取要修改的主键值；根据主键获得数据字典的信息，反显在修改页面上
	 * @return
	 */
	public String toModi(){
		String sun_key = StringUtil.toArr(ParamUtil.get(request, "selectedId"), "#")[0];
		List<DictEntity> list = dictService.getAllDdContentByKey(sun_key);
		DictMapEntity dictMap = dictService.getDictMapByKey(sun_key);
		request.setAttribute("dicts", list);
		request.setAttribute("dictmap", dictMap);
		return "modi";
	}
	
	/**
	 * 修改功能
	 * 先非空校验；更新数据库
	 * @return
	 */
	public String modi(){
		String dictValues = ParamUtil.get(request, "val_str");
		String dictDescs = ParamUtil.get(request, "dict_desc_str");
		//非空校验
		if(dictMapEntity == null || StringUtil.isEmpty(dictMapEntity.getPrompt()) || 
				StringUtil.isEmpty(dictMapEntity.getSun_key()) || StringUtil.isEmpty(dictValues) || 
				StringUtil.isEmpty(dictDescs)){
			request.setAttribute(Consts.TIP_MSG, "数据字典名称、主键、值和值说明不能为空！");
			return Consts.ERROR;
		}
		
		dictService.updateDict(dictMapEntity,dictValues,dictDescs);
		request.setAttribute(Consts.TIP_MSG, "修改成功！");
		return Consts.LAYER_SUCCESS;
	}
	
	/**
	 * 删除功能
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception{
		dictService.delete(ParamUtil.get(request, "selectedId"));
		request.setAttribute(Consts.TIP_MSG, "删除成功！");
		return Consts.SUCCESS; 
	}
	
	public DictEntity getDictEntity() {
		return dictEntity;
	}

	public void setDictEntity(DictEntity dictEntity) {
		this.dictEntity = dictEntity;
	}

	public DictMapEntity getDictMapEntity() {
		return dictMapEntity;
	}

	public void setDictMapEntity(DictMapEntity dictMapEntity) {
		this.dictMapEntity = dictMapEntity;
	}
}

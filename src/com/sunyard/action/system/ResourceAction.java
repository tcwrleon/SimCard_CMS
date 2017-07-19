package com.sunyard.action.system;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sunyard.action.BaseAction;
import com.sunyard.base.model.Consts;
import com.sunyard.entity.system.ResourceEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.ResourceService;
import com.sunyard.util.ParamUtil;
import com.sunyard.util.StringUtil;


/**
 * @author mumu
 *
 */
@Controller
@Scope("prototype")
public class ResourceAction extends BaseAction{
	private ResourceEntity resourceEntity;
	@Resource
	private ResourceService resourceService;
	private static final String SELECTED_ID = "selectedId";
	
	/**
	 * 设置页面下拉框内容
	 * @throws Exception 
	 */
	private void setPageSelectOption() {
		try {
			getDirtMap(new String[]{DICTKEY.K_RESSTT.toString(),DICTKEY.K_PLATTYPE.toString(),
					DICTKEY.K_RESTYPE.toString()});
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 非空校验
	 * @return
	 */
	private boolean isResourceNull(){
		if(StringUtil.isEmpty(resourceEntity.getPrivilege_id()) || StringUtil.isEmpty(resourceEntity.getPrivilege_name()) ||
				StringUtil.isEmpty(resourceEntity.getPlatform_type()) || StringUtil.isEmpty(resourceEntity.getType()) ||
				StringUtil.isEmpty(resourceEntity.getValid())) {
			return true;
		}
		
		return false;
		
	}
	
	/**
	 * 查询功能
	 * 支持模糊查询；分页查询
	 * @return
	 */
	public String toQuery(){
		if(resourceEntity == null) {
			resourceEntity = new ResourceEntity();
		}
		PageView page = resourceService.query(getPageView(), resourceEntity); //分页
		request.setAttribute("page", page);
		request.setAttribute("back", resourceEntity);//查询条件页面反显
		getBtnHtmlByMenuId();
		setPageSelectOption();
		return "query";
	}
	
	/**
	 * 调转到新增页面
	 * 初始化页面内容
	 * @return
	 */
	public String toAdd(){
		setPageSelectOption();
		return "add";
	}
	
	/**
	 * 资源新增
	 * 非空校验
	 * 资源编号唯一校验
	 * 插入数据库
	 * @return
	 * @throws Exception 
	 */
	public String add() throws Exception{
		if(isResourceNull()){
			request.setAttribute(Consts.TIP_MSG, "权限编号、名称、类型、状态和平台类型不能为空！");
			return Consts.ERROR;
		}
		
		ResourceEntity res = resourceService.getById(resourceEntity.getPrivilege_id());
		if(res != null){
			request.setAttribute(Consts.TIP_MSG, "权限编号为" + resourceEntity.getPrivilege_id() + "已存在！");
			return Consts.ERROR;
		}
		
		resourceService.add(resourceEntity);
		request.setAttribute(Consts.TIP_MSG, "新增成功！");
		return Consts.SUCCESS;
	}
	
	/**
	 * 跳转到修改页面
	 * 初始化页面
	 * 设置修改标识
	 * @return
	 */
	public String toModi(){
		ResourceEntity res = resourceService.getById(ParamUtil.get(request, SELECTED_ID));
		request.setAttribute("obj", res);
		request.setAttribute("modiFlag", "modi");
		return toAdd();
	}
	
	/**
	 * 资源修改
	 * 非空校验
	 * 更新数据库
	 * @return
	 * @throws Exception
	 */
	public String modi() throws Exception{
		if(isResourceNull()){
			request.setAttribute(Consts.TIP_MSG, "权限编号、名称、类型、状态和平台类型不能为空！");
			return Consts.ERROR;
		}
		resourceService.update(resourceEntity);
		request.setAttribute(Consts.TIP_MSG, "修改成功！");
		return Consts.SUCCESS;
	}
	
	/**
	 * 资源删除
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception{
		resourceService.delete(ParamUtil.get(request, SELECTED_ID));
		request.setAttribute(Consts.TIP_MSG, "删除成功！");
		return Consts.SUCCESS;
	}
	
	public ResourceEntity getResourceEntity() {
		return resourceEntity;
	}

	public void setResourceEntity(ResourceEntity resourceEntity) {
		this.resourceEntity = resourceEntity;
	}
	

}

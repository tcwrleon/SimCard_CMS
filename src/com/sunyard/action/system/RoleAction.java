package com.sunyard.action.system;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sunyard.action.BaseAction;
import com.sunyard.base.model.Consts;
import com.sunyard.entity.system.ResourceEntity;
import com.sunyard.entity.system.RoleEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.enums.RESOURCESTT;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.ResourceService;
import com.sunyard.service.system.RoleService;
import com.sunyard.util.ExcelUtil;
import com.sunyard.util.ParamUtil;
import com.sunyard.util.StringUtil;


/**
 * 角色控制层
 * @author mumu
 */
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction{
	private RoleEntity roleEntity;
	@Resource
	private RoleService roleService;
	@Resource
	private ResourceService resourceService;
	
	private static final String SELECTEDID = "selectedId";
	private String[] keys = {DICTKEY.K_PLATTYPE.toString()};
	
	/**
	 * 非空校验
	 * @return
	 */
	private boolean isRoleNull(){
		if(StringUtil.isEmpty(roleEntity.getRole_id()) || StringUtil.isEmpty(roleEntity.getRole_name()) ||
				StringUtil.isEmpty(roleEntity.getPlatform_type())){
			return true;
		}
		return false;
	}
	
	/**
	 * 角色查询
	 * 支持分页查询、模糊查询
	 * 初始化列表页面下拉框内容
	 * @return
	 * @throws Exception
	 */
	public String toQuery() throws Exception{
		if(roleEntity == null){
			roleEntity = new RoleEntity();
		}
		PageView page = roleService.query(getPageView(), roleEntity); //分页查询
		
		request.setAttribute("page", page);
		request.setAttribute("back", roleEntity);//查询条件反显
		getBtnHtmlByMenuId();//初始化页面操作按钮
		getDirtMap(keys);//初始化查询条件的下拉框内容
		return "query";
	}
	
	/**
	 * 角色资源列表导出
	 * @throws IOException 
	 * @throws WriteException 
	 * @throws RowsExceededException 
	 */
	public void doExport() throws RowsExceededException, WriteException, IOException{
		List<Map<String,Object>> list = roleService.queryRoleResources(roleEntity);
		
		Map<String,String> dictMap = new HashMap<String,String>();
		dictMap.put("platform_type", DICTKEY.K_PLATTYPE.toString());
		dictMap.put("type", DICTKEY.K_RESTYPE.toString());
		
		ExcelUtil.excelExport(list, "platform_type,role_id,role_name,type,privilege_id,privilege_name,parent_id",
				"平台类型,角色编号,角色名称,权限类型,权限编号,权限名称,父权限", dictMap, response, "角色权限列表");
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
	 * 角色新增
	 * 非空校验
	 * 角色编号唯一性校验
	 * 插入数据库
	 * @return
	 * @throws Exception 
	 */
	public String add() throws Exception{
		if(isRoleNull()){
			request.setAttribute(Consts.TIP_MSG, "角色编号、角色名称和平台类型不能为空！");
			return Consts.ERROR;
		}
		
		RoleEntity role = roleService.getById(roleEntity.getRole_id());
		if(role != null){
			request.setAttribute(Consts.TIP_MSG, "角色编号为" + roleEntity.getRole_id() + "已存在！");
			return Consts.ERROR;
		}
		
		roleService.add(roleEntity);
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
		RoleEntity role = roleService.getById(ParamUtil.get(request, SELECTEDID));
		request.setAttribute("obj", role);
		request.setAttribute("modiFlag", "modi");
		return toAdd();
	}
	
	/**
	 * 角色修改
	 * 非空校验；更新数据库
	 * @return
	 * @throws Exception
	 */
	public String modi() throws Exception{
		if(isRoleNull()){
			request.setAttribute(Consts.TIP_MSG, "角色编号、角色名称和平台类型不能为空！");
			return Consts.ERROR;
		}
		roleService.update(roleEntity);
		request.setAttribute(Consts.TIP_MSG, "修改成功！");
		return Consts.LAYER_SUCCESS;
	}
	
	/**
	 * 角色删除
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception{
		roleService.delete(ParamUtil.get(request, SELECTEDID));
		request.setAttribute(Consts.TIP_MSG, "删除成功！");
		return Consts.SUCCESS;
	}
	
	/**
	 * 跳转到权限分配页面
	 * 加载所有有效的权限；加载该角色已有的权限
	 * 拼接成json数据
	 * @return
	 */
	public String toAssign(){
		RoleEntity role = roleService.getById(ParamUtil.get(request, SELECTEDID));
		
		ResourceEntity res = new ResourceEntity();
		res.setPlatform_type(role.getPlatform_type());//平台类型：管理台或者是互联网端
		res.setValid(RESOURCESTT.Valid.getCode());//权限状态：有效的
		
		List<ResourceEntity> allRes = resourceService.queryAll(res);//查询所有权限
		List<ResourceEntity> roleRes = resourceService.loadResourcesByRoleId(role.getRole_id());//根据角色ID查询其权限
		
		StringBuffer sb = new StringBuffer();
		sb.append("var data = [];");
		for (ResourceEntity r : allRes) {
			boolean flag = false;
			for (ResourceEntity ur : roleRes) {//该角色已分配权限
				/*if (ur.getPrivilege_id().equals(r.getParent_id())) {*/
				if (ur.getPrivilege_id().equals(r.getPrivilege_id())) {
					sb.append("data.push({ fid: '"
							+ r.getPrivilege_id() + "', pfid: '"
							+ r.getParent_id()
							+ "', fname: '" + r.getPrivilege_name()
							+ "',ischecked: true});");//设置选中状态
					flag = true;
				}
			}
			if (!flag) {
				sb.append("data.push({ fid: '"
						+ r.getPrivilege_id() + "', pfid: '"
						+ r.getParent_id()
						+ "', fname: '" + r.getPrivilege_name()
						+ "'});");

			}
		}
		
		logger.info("权限树=" + sb);
		request.setAttribute("resources", sb);
		return "assign";
	}
	
	/**
	 * 权限分配
	 * 校验权限参数不为空
	 * @return
	 */
	public String resAssign(){
		String roleId = ParamUtil.get(request, SELECTEDID);
		String checkedId = ParamUtil.get(request, "checkedId");
		if(StringUtil.isEmpty(checkedId)){
			request.setAttribute(Consts.TIP_MSG, "没有分配权限！");
			return Consts.ERROR;
		}
		roleService.assignRoleResources(roleId,checkedId);
		request.setAttribute(Consts.TIP_MSG, "权限分配成功！");
		return Consts.LAYER_SUCCESS;
	}
	
	public RoleEntity getRoleEntity() {
		return roleEntity;
	}

	public void setRoleEntity(RoleEntity roleEntity) {
		this.roleEntity = roleEntity;
	}
}

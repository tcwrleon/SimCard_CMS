package com.sunyard.action.system;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sunyard.action.BaseAction;
import com.sunyard.base.model.Consts;
import com.sunyard.entity.system.DepartmentEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.DepartmentService;
import com.sunyard.util.ParamUtil;
import com.sunyard.util.StringUtil;
import com.sunyard.util.TreeUtil;

/**
 * @author mumu
 * 机构（部门）管理：查、增、改、删和机构树
 */
@Controller
@Scope("prototype")
public class DepartmentAction extends BaseAction{
	
	private DepartmentEntity dptEntity;
	@Resource
	private DepartmentService departmentService;
	private static final String SELECTEDID = "selectedId";
	private static final String NullMsg = "部门编号和部门名称不能为空！";
	
	/**
	 * 非空校验
	 * @return
	 */
	private boolean isDepartmentNull(){
		if( dptEntity == null) return true;
		if(StringUtil.isEmpty(dptEntity.getDepartment_id()) || StringUtil.isEmpty(dptEntity.getDepartment_name())){
			return true;
		}
		return false;
	}
	
	/**
	 * 查询功能
	 * 1 分页查询；2展现机构树
	 * @return
	 */
	public String toQuery(){
		if(dptEntity == null){
			dptEntity = new DepartmentEntity();
		}
		PageView page = departmentService.query(getPageView(), dptEntity);
		request.setAttribute("page", page);
		request.setAttribute("back", dptEntity);
		getBtnHtmlByMenuId();
		request.setAttribute("departmentTree", TreeUtil.showDepartmentTree(departmentService.createDepartmentTree("")));
		return "query";
	}
	
	/**
	 * 跳转到新增页面
	 * 1 生成页面下拉框数据
	 * @return
	 * @throws Exception
	 */
	public String toAdd() throws Exception{
		String[] paramArr = {DICTKEY.K_DPTLVL.toString(),DICTKEY.K_DPTSTT.toString()};
		this.getDirtMap(paramArr);
		//request.setAttribute("departmentTree", departmentService.createDepartmentTree(Consts.TREE_PREFIX));
		request.setAttribute("departmentTree", TreeUtil.showDepartmentTree(departmentService.createDepartmentTree("")));
		return "add";
	}
	
	/**
	 * 新增功能
	 * 1 非空校验 2 insert
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception{
		if(isDepartmentNull()){
			request.setAttribute(Consts.TIP_MSG, NullMsg);
			return Consts.ERROR;
		}
		
		DepartmentEntity department = departmentService.getById(dptEntity.getDepartment_id());
		if(department != null){
			request.setAttribute(Consts.TIP_MSG, "部门编号为" + department.getDepartment_id() + "已存在！");
			return Consts.ERROR;
		}
		
		departmentService.add(dptEntity);
		request.setAttribute(Consts.TIP_MSG, "添加成功！");
		return Consts.LAYER_SUCCESS;
	}
	
	/**
	 * 跳转到修改页面
	 * 1 修改页面反显数据获取 2 入口标志
	 * @return
	 * @throws Exception
	 */
	public String toModi() throws Exception{
		DepartmentEntity obj = departmentService.getById(ParamUtil.get(request, SELECTEDID));
		logger.info("要修改机构对象=" + obj);
		request.setAttribute("obj", obj);
		request.setAttribute("modiFlag", "modi");//入口标志
		return toAdd();
	}
	
	/**
	 * 修改功能
	 * 1 非空校验；2 update
	 * @return
	 * @throws Exception 
	 */
	public String modi() throws Exception{
		if(isDepartmentNull()){
			request.setAttribute(Consts.TIP_MSG, NullMsg);
			return Consts.ERROR;
		}
		
		departmentService.update(dptEntity);
		request.setAttribute(Consts.TIP_MSG, "修改成功！");
		return Consts.LAYER_SUCCESS;
	}
	
	/**
	 * 删除功能
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception{
		departmentService.delete(ParamUtil.get(request, SELECTEDID));
		request.setAttribute(Consts.TIP_MSG, "删除成功！");
		return Consts.SUCCESS;
	}

	public DepartmentEntity getDptEntity() {
		return dptEntity;
	}

	public void setDptEntity(DepartmentEntity dptEntity) {
		this.dptEntity = dptEntity;
	}

}

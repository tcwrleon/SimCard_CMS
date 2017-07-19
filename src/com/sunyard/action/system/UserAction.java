package com.sunyard.action.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sunyard.action.BaseAction;
import com.sunyard.base.model.Consts;
import com.sunyard.entity.system.RoleEntity;
import com.sunyard.entity.system.UserEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.enums.PLATFORMTYPE;
import com.sunyard.enums.USERSTATUS;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.DepartmentService;
import com.sunyard.service.system.RoleService;
import com.sunyard.service.system.UserService;
import com.sunyard.util.ExcelUtil;
import com.sunyard.util.ParamUtil;
import com.sunyard.util.StringUtil;
import com.sunyard.util.TreeUtil;

/**
 * @author mumu
 *
 */
@Controller
@Scope("prototype")
public class UserAction extends BaseAction{
	
	private UserEntity userEntity;
	@Resource
	private UserService userService;
	@Resource
	private DepartmentService departmentService;
	@Resource
	private RoleService roleService;
	//是否校验用户编号重复
	private boolean isCheckUserId = true;
	//是否校验用户名重复
	private boolean isCheckUserName = true;
	private static final String SELECTED_ID = "selectedId";
	
	/**
	 * 获取用户对象 Struts 与页面input name 进行绑定
	 * @return
	 */
	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
	}
	
	/**
	 * 获取机构树并放在request
	 */
	private void setDepartmentmTree(){
		//request.setAttribute("departmentTree", departmentService.createDepartmentTree(Consts.TREE_PREFIX));
		request.setAttribute("departmentTree", TreeUtil.showDepartmentTree(departmentService.createDepartmentTree("")));
	}
	
	/**
	 * 新增和更新前的校验
	 * 非空校验；用户编号唯一校验；用户名唯一校验
	 * @return
	 */
	private boolean preCommitCheck(){
		if(!checkUserNotNull()) {
			request.setAttribute(Consts.TIP_MSG, "用户编号、用户名称、用户状态和用户类型不能为空！");
			return true;
		}
		
		if(this.isCheckUserId){
			if(!checkUserIdNotRepeat()){
				request.setAttribute(Consts.TIP_MSG, "用户编号[ "+ userEntity.getUser_id() + " ]已存在，请重新输入！");
				return true;
			}
		}
		
		if(this.isCheckUserName){
			if(!checkUserNameNotRepeat()){
				request.setAttribute(Consts.TIP_MSG, "用户名[ "+ userEntity.getUser_name() + " ]已存在，请重新输入！");
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 非空校验
	 * @return
	 */
	private boolean checkUserNotNull(){
		if(userEntity == null) return false;
		
		if(StringUtil.isEmpty(userEntity.getUser_name()) || StringUtil.isEmpty(userEntity.getUser_state()) 
				|| StringUtil.isEmpty(userEntity.getUser_type()) || StringUtil.isEmpty(userEntity.getUser_id())) 
			return false;
		
		return true;
	}
	
	/**
	 * 用户名唯一校验
	 * @return
	 */
	private boolean checkUserNameNotRepeat(){
		List<UserEntity> users = userService.usersByUsername(userEntity.getUser_name());
		if(users == null || users.size() == 0) return true;
		return false;
	} 
	
	/**
	 * 用户编号唯一校验
	 * @return
	 */
	private boolean checkUserIdNotRepeat(){
		UserEntity user = userService.getById(userEntity.getUser_id());
		if(user == null) return true;
		return false;
	}
	
	/**
	 * 用户列表
	 * 支持分页查询和模糊查询
	 * @return
	 * @throws Exception
	 */
	public String toQuery() throws Exception{
		if(userEntity == null){
			userEntity = new UserEntity();
		}
		PageView page = userService.query(getPageView(), userEntity);//分页查询
		request.setAttribute("page", page);
		this.getDirtMap(new String[]{DICTKEY.K_USERSTT.toString()});
		request.setAttribute("back", userEntity);
		getBtnHtmlByMenuId();
		setDepartmentmTree();
		return "query";
	}
	
	/**
	 * 导出用户角色列表
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws IOException
	 */
	public void doExport() throws RowsExceededException, WriteException, IOException{
		List<Map<String,Object>> list = userService.queryUserRoles(userEntity);
		ExcelUtil.excelExport(list,"user_id,user_name,role_id,role_name","用户编号,用户名,角色编号,角色名称", null, response,"用户角色列表");
	}
	
	/**
	 * 跳转到新增页面
	 * @return
	 * @throws Exception
	 */
	public String toAdd() throws Exception{
		this.getDirtMap(new String[]{DICTKEY.K_CERTTYPE.toString(),DICTKEY.K_USERSTT.toString(),DICTKEY.K_USERTYPE.toString()});
		setDepartmentmTree();
		return "add";
	}
	
	/**
	 * 用户新增
	 * 添加之前要校验：非空校验；用户编号和用户名是否重复校验
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception{
		if(preCommitCheck()) return Consts.ERROR;
		userService.add(userEntity);
		request.setAttribute(Consts.TIP_MSG, "新增成功！");
		return Consts.LAYER_SUCCESS;
	}
	
	/**
	 * 跳转到修改页面
	 * 根据用户编号查询该用户信息；到页面上反显
	 * @return
	 * @throws Exception
	 */
	public String toModi() throws Exception{
		UserEntity obj = userService.getById(ParamUtil.get(request, SELECTED_ID));
		request.setAttribute("obj", obj);
		request.setAttribute("modiFlag", "modi");//入口标志
		return toAdd();
	}
	
	/**
	 * 用户更新
	 * 先确定是否要校验用户名和用户编号唯一；校验；最后更新数据库
	 * @return
	 * @throws Exception
	 */
	public String modi() throws Exception{
		this.isCheckUserId = false;
		if(userService.getById(userEntity.getUser_id()).getUser_name().equals(userEntity.getUser_name())) 
			this.isCheckUserName = false;
		
		if(preCommitCheck()) return Consts.ERROR;
		userService.update(userEntity);
		request.setAttribute(Consts.TIP_MSG, "修改成功！");
		return Consts.LAYER_SUCCESS;
	}
	
	/**
	 * 用户删除
	 * @return
	 * @throws Exception
	 */
	public String delete() throws Exception{
		userService.delete(ParamUtil.get(request, SELECTED_ID));
		request.setAttribute(Consts.TIP_MSG, "删除成功！");
		return Consts.SUCCESS;
	}
	
	/**
	 * 用户锁定
	 * @return
	 */
	public String lock(){
		String returnCode = userService.updateUserState(USERSTATUS.Lock.getCode(),ParamUtil.get(request, SELECTED_ID));
		if(returnCode.equals(Consts.SUCCESS_CODE)){
			request.setAttribute(Consts.TIP_MSG, "锁定成功！");
			return Consts.SUCCESS;
		}else{
			request.setAttribute(Consts.TIP_MSG, "用户已锁定，不能再次锁定！");
			return Consts.ERROR;
		}
		
	}
	
	/**
	 * 用户解锁
	 * @return
	 */
	public String unlock(){
		String returnCode = userService.updateUserState(USERSTATUS.Normal.getCode(),ParamUtil.get(request, SELECTED_ID));
		if(returnCode.equals(Consts.SUCCESS_CODE)){
			request.setAttribute(Consts.TIP_MSG, "解锁成功！");
			return Consts.SUCCESS;
		}else{
			request.setAttribute(Consts.TIP_MSG, "用户为正常状态，不能解锁！");
			return Consts.ERROR;
		}
	}
	
	/**
	 * 跳转到密码修改页面
	 * @return
	 */
	public String toModiPwd(){
		return "pwdModi";
	}
	
	/**
	 * 密码修改
	 * 服务端校验；更改数据库
	 * @return
	 */
	public String modiPwd(){
		String oldPwd = ParamUtil.get(request, "old_password");
		String newPwd = ParamUtil.get(request, "new_password");
		String newPwdConfirm = ParamUtil.get(request, "new_password_confirm");
		if(StringUtil.isEmpty(oldPwd) || StringUtil.isEmpty(newPwd) || StringUtil.isEmpty(newPwdConfirm)){
			request.setAttribute(Consts.TIP_MSG, "旧密码和新密码不能为空！");
			return Consts.ERROR;
		}
		
		if(oldPwd.equals(newPwd)){
			request.setAttribute(Consts.TIP_MSG, "旧密码和新密码相同！");
			return Consts.ERROR;
		}
		
		if(!newPwd.equals(newPwdConfirm)){
			request.setAttribute(Consts.TIP_MSG, "新密码与密码确认不一致！");
			return Consts.ERROR;
		}
		
		String msg = userService.updateUserPwd(oldPwd,newPwd,ParamUtil.get(request, SELECTED_ID));
		if(Consts.SUCCESS_CODE.equals(msg)){
			if(!StringUtil.isEmpty(ParamUtil.get(request, "pwdExpired"))){//来自登录密码过期入口
				request.setAttribute(Consts.TIP_MSG, "密码修改成功，请重新登录！");
				return Consts.SUCCESS;
			}
			request.setAttribute(Consts.TIP_MSG, "密码修改成功！");
			return Consts.LAYER_SUCCESS;
		}else{
			request.setAttribute(Consts.TIP_MSG, msg);
			return Consts.ERROR;
		}
		
	}
	
	/**
	 * 重置密码
	 * @return
	 */
	public String setDefultPwd(){
		userService.setDefultPwd(ParamUtil.get(request, SELECTED_ID));
		request.setAttribute(Consts.TIP_MSG, "密码重置成功！");
		return Consts.SUCCESS;
	}
	
	/**
	 * 跳转到角色分配页面
	 * 先查询用户信息；该用户已分配角色；最后获得未分配角色
	 * 反显到页面上
	 * @return
	 */
	public String toRoleAssign(){
		UserEntity user = userService.getById(ParamUtil.get(request, SELECTED_ID));
		List<RoleEntity> userRoles = userService.rolesByUserId(user.getUser_id());
		
		RoleEntity role = new RoleEntity();
		role.setPlatform_type(PLATFORMTYPE.Background.getCode());
		List<RoleEntity> allRoles = roleService.queryAll(role);
		
		List<RoleEntity> copyAllRoles = new ArrayList<RoleEntity>(Arrays.asList( new RoleEntity[allRoles.size()]));
		Collections.copy(copyAllRoles, allRoles);//拷贝一份，这里应该是浅拷贝，不过够用
		copyAllRoles.removeAll(userRoles);//求差集
		logger.info("已分配角色=" + userRoles);
		logger.info("可分配的角色=" + copyAllRoles);
		
		request.setAttribute("userInfo", user);
		request.setAttribute("userRoles", userRoles);
		request.setAttribute("assignableRoles", copyAllRoles);
		return "roleAssign";
	}
	
	/**
	 * 角色分配
	 * @return
	 */
	public String assignRole(){
		String rolesId = ParamUtil.get(request, "rolesId");
		if(StringUtil.isEmpty(rolesId)){
			request.setAttribute(Consts.TIP_MSG, "未分配角色");
			return Consts.ERROR;
		}
		
		userService.assignRole(ParamUtil.get(request, SELECTED_ID),rolesId);
		request.setAttribute(Consts.TIP_MSG, "角色分配成功！");
		return Consts.SUCCESS;
	}

}

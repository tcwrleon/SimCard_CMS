package com.sunyard.action.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Controller;

import com.sunyard.action.BaseAction;
import com.sunyard.base.model.Consts;
import com.sunyard.entity.system.ResourceEntity;
import com.sunyard.entity.system.UserEntity;
import com.sunyard.enums.RESOURCETYPE;
import com.sunyard.enums.USERSTATUS;
import com.sunyard.service.system.ResourceService;
import com.sunyard.service.system.UserService;
import com.sunyard.util.Common;
import com.sunyard.util.StringUtil;
import com.sunyard.util.SysParamUtil;
import com.sunyard.util.TreeUtil;

/**
 * @author mumu
 *
 */
@Controller
@Scope("prototype")
public class LoginAction extends BaseAction {
	@Resource
	private ResourceService resourceService;
	@Resource
	private UserService userService;
	private static final String login = "login";

	@Resource
	//Spring security 自带加密方式
	private StandardPasswordEncoder passwordEncoder;

	/**
	 * 跳转到登录页面
	 * @return
	 */
	public String goToLoginPage(){
		return login;
	}
	
	/**
	 * 退出系统
	 * @return
	 */
	public String logout(){
		return login;
	}
	
	/**
	 * 用户登录校验失败跳转页面
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String errorToLogin(){
		request.setAttribute("error", "true");
		AuthenticationException errObj = (AuthenticationException)session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		
		logger.info("异常信息：" + errObj.getClass().getSimpleName() + errObj.getMessage());
		String className = errObj.getClass().getSimpleName();
		String userName = errObj.getAuthentication().getPrincipal().toString();
		if("BadCredentialsException".equals(className)){//密码不正确
			updatePwdErrorTime(userName);
		}else if("CredentialsExpiredException".equals(className)){//用户登录密码已过期
			return redirectPwdModiPage(userName);
		}
		
		return login;
	}
	
	/**
	 * 更新登陆密码错误次数
	 * 超过N次，锁定用户
	 * @param userName
	 */
	private void updatePwdErrorTime(String userName){
		userService.updatePwdErrorNum(userName);
		UserEntity user = userService.usersByUsername(userName).get(0);
		if(user.getPwd_error_num() >= SysParamUtil.getSysParamValueInt(Consts.PWD_ERROR_TIMES)){
			if(!USERSTATUS.Lock.getCode().equals(user.getUser_state())){
				userService.updateUserState(USERSTATUS.Lock.getCode(), user.getUser_id());
			}
		}
	}
	
	/**
	 * 跳转到密码修改页面
	 * @param userName
	 * @return
	 */
	private String redirectPwdModiPage(String userName){
		UserEntity user = userService.usersByUsername(userName).get(0);
		request.setAttribute("selectedId", user.getUser_id());
		request.setAttribute("pwdExpired", "true");
		return "pwdModi";
	}
	/**
	 * 登录后首页
	 * 加载用户权限;权限按资源类型分组;用户信息放入会话中
	 * @return
	 * @throws Exception
	 */
	public String successToMain() throws Exception{
		String userName = Common.getAuthenticatedUsername();
		if(StringUtil.isEmpty(userName)) return login;
		
		List<ResourceEntity> userResources = resourceService.getUserResourcesByName(userName);
		List<ResourceEntity> moduleList = new ArrayList<ResourceEntity>();
		List<ResourceEntity> menuList = new ArrayList<ResourceEntity>();
		List<ResourceEntity> btnList = new ArrayList<ResourceEntity>();
		for(ResourceEntity item : userResources){
			String resourceType = item.getType();
			if(RESOURCETYPE.Module.getCode().equals(resourceType)){//模块
				moduleList.add(item);
			}else if(RESOURCETYPE.Button.getCode().equals(resourceType)){//按钮
				btnList.add(item);
			}else{
				menuList.add(item);//菜单
			}
		}
		session.setAttribute("moduleList", moduleList);
		session.setAttribute("menuList", menuList);
		session.setAttribute("btnList", btnList);
		//密码错误次数清零
		userService.clearPwdErrTimes(userName);
		//用户信息放在会话
		session.setAttribute(Consts.USER_SESSION, userService.usersByUsername(userName).get(0));
		//生成菜单树
		showChildren();
		return "main";
	}
	
	
	/**
	 * 生成菜单树（根据模块编号）
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String showChildren() throws Exception{
		String privilegeId = request.getParameter("privilege_id");
		String privilegeName = "";
		
		//初始化资源名称
		List<ResourceEntity> moduleList = (List<ResourceEntity>)session.getAttribute("moduleList");
		if(moduleList == null || moduleList.size() == 0) return login;
		
		if(Common.isEmpty(privilegeId)){
			privilegeId = moduleList.get(0).getPrivilege_id();
			privilegeName = moduleList.get(0).getPrivilege_name();
		}else{
			for(ResourceEntity module : moduleList){
				if(privilegeId.equals(module.getPrivilege_id())){
					privilegeName = module.getPrivilege_name();
				}
			}
		}
		
		// 根据privilege_id 查找其所有的子权限
		List<ResourceEntity> children = new ArrayList<ResourceEntity>();
		getChildrenByParent(privilegeId, children);
		
		String childrenList = TreeUtil.showLeftTree(children, privilegeId);
		
		request.setAttribute("childrenList", childrenList);
		request.setAttribute("title", privilegeName);
		return "main";
	}

	/**
	 * 权限递归树
	 * @param privilegeId
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	private void getChildrenByParent(String privilegeId,List<ResourceEntity> list) {
		List<ResourceEntity> subList = (List<ResourceEntity>)session.getAttribute("menuList");
		for(ResourceEntity item : subList){
			if(privilegeId.equals(item.getParent_id())){
				list.add(item);
				getChildrenByParent(item.getPrivilege_id(), list);
			}
		}
	}

	/**
	 * 验证用户名、密码
	 * @throws Exception
	 */
	public void checkUser() throws Exception{
		String userName = request.getParameter("username");
		String userPwd = request.getParameter("userpwd");
		String role_id = userService.queryUserRoleId(userName);
		if(StringUtil.isEmpty(userName) || StringUtil.isEmpty(userPwd) || userService.queryUserPwd(userName) == null) 
			this.returnMsg("40000", "用户验证失败！",role_id);

		if(!passwordEncoder.matches(userPwd , userService.queryUserPwd(userName))){
			String remsg = "用户验证失败！";
			this.returnMsg("40000", remsg, role_id);
		}		

		session.setAttribute("username", userName);
		session.setAttribute("userpwd", userPwd);
		this.returnMsg("10000", "用户验证通过！", role_id);
	}

	/**
	 * 登录后首页(POST方式)
	 * 加载用户权限;权限按资源类型分组;用户信息放入会话中
	 * @return
	 * @throws Exception
	 */
	public String successToMainByPost() throws Exception{
		String userName = request.getParameter("username");
		String userPwd = request.getParameter("userpwd");
		if(StringUtil.isEmpty(userName) || StringUtil.isEmpty(userPwd)) return login;
		if(!passwordEncoder.matches(userPwd , userService.queryUserPwd(userName))){
			String msg = "密码不正确！";
			request.setAttribute("msg",msg);
			return Consts.ERROR;
		}
/*
		String userName = (String) session.getAttribute("username");
		if(StringUtil.isEmpty(userName)){
			request.setAttribute("msg","用户名为空！");
			return Consts.ERROR;
		}*/
		logger.info("userName=="+userName);
		List<ResourceEntity> userResources = resourceService.getUserResourcesByName(userName);
		List<ResourceEntity> moduleList = new ArrayList<ResourceEntity>();
		List<ResourceEntity> menuList = new ArrayList<ResourceEntity>();
		List<ResourceEntity> btnList = new ArrayList<ResourceEntity>();
		for(ResourceEntity item : userResources){
			String resourceType = item.getType();
			if(RESOURCETYPE.Module.getCode().equals(resourceType)){//模块
				moduleList.add(item);
			}else if(RESOURCETYPE.Button.getCode().equals(resourceType)){//按钮
				btnList.add(item);
			}else{
				menuList.add(item);//菜单
			}
		}
		session.setAttribute("moduleList", moduleList);
		session.setAttribute("menuList", menuList);
		session.setAttribute("btnList", btnList);
		//密码错误次数清零
		userService.clearPwdErrTimes(userName);
		//用户信息放在会话
		session.setAttribute(Consts.USER_SESSION, userService.usersByUsername(userName).get(0));
		//生成菜单树
		showChildren();
		return "main";
		
		
	}
	
	/**
	 * 返回信息
	 * @param recode
	 * @param remsg
	 * @throws IOException
	 */
	protected void returnMsg(String recode, String remsg, String role_id) throws IOException{
		JSONObject object = new JSONObject();
		object.accumulate("recode", recode);
		object.accumulate("rsmsg", remsg);
		object.accumulate("role_id", role_id);
		sendMsg(object);
	}	
	/**
	 * 发送json消息--JSONObject
	 */
	protected void sendMsg(JSONObject json) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		writer.write(json.toString());
		writer.close();
	}
}

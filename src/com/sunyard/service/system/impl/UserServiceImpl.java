package com.sunyard.service.system.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunyard.base.model.Consts;
import com.sunyard.dao.system.UserDao;
import com.sunyard.entity.system.PwdHistoryEntity;
import com.sunyard.entity.system.RoleEntity;
import com.sunyard.entity.system.UserEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.enums.INITPWD;
import com.sunyard.enums.PLATFORMTYPE;
import com.sunyard.enums.USERSTATUS;
import com.sunyard.log.annotation.OperLoggable;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.system.UserService;
import com.sunyard.util.Common;
import com.sunyard.util.DDUtil;
import com.sunyard.util.DateUtil;
import com.sunyard.util.StringUtil;
import com.sunyard.util.SysParamUtil;

/**
 * @author mumu
 *
 */
@Service(value="userService")
@Transactional
public class UserServiceImpl implements UserService{
	@Resource
	private UserDao userDao;
	
	@Resource
	//Spring security 自带加密方式 
	private StandardPasswordEncoder passwordEncoder; 

	/* (non-Javadoc)
	 * @see com.sunyard.service.BaseService#query(com.sunyard.pulgin.PageView, java.lang.Object)
	 * 分页查询
	 * 对查询结果集某些字段进行翻译 
	 */
	@Override
	public PageView query(PageView pageView, UserEntity t) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("paging", pageView);
		map.put("t", t);
		List<UserEntity> list = userDao.query(map);
		
		for(UserEntity item : list){
			item.setUser_state(DDUtil.getContent(DICTKEY.K_USERSTT.toString(), item.getUser_state()));
			item.setUser_type(DDUtil.getContent(DICTKEY.K_USERTYPE.toString(), item.getUser_type()));
			item.setCert_type(DDUtil.getContent(DICTKEY.K_CERTTYPE.toString(), item.getCert_type()));
		}
		pageView.setResult(list);
		return pageView;
	}

	@Override
	public List<UserEntity> queryAll(UserEntity t) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#delete(java.lang.String)
	 * 用户删除之前先删除用户和角色关系表中记录
	 */
	@Override
	@OperLoggable(module="用户管理", description="删除")
	public void delete(String id) throws Exception {
		List<String> list = StringUtil.toList(id);
		userDao.deleteUserRole(list);
		userDao.deleteBatch(list);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#update(java.lang.Object)
	 */
	@Override
	@OperLoggable(module="用户管理", description="更新")
	public void update(UserEntity t) throws Exception {
		setModiTimeAndUser(t);
		userDao.update(t);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#getById(java.lang.String)
	 */
	@Override
	public UserEntity getById(String id) {
		return userDao.getById(id);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.base.Base#add(java.lang.Object)
	 * 用户新增
	 * 1插入用户表；2密码插入密码历史表
	 */
	@Override
	@OperLoggable(module="用户管理", description="新增")
	public void add(UserEntity t) throws Exception {
		t.setUser_pwd(passwordEncoder.encode(SysParamUtil.getSysParamValueStr(Consts.DEFAULT_LOGIN_PWD)));//对默认密码加密
		t.setCreate_date(DateUtil.todayStr() + " " + DateUtil.curTimeStr());
		t.setPwd_modify_date(DateUtil.todayStr() + " " + DateUtil.curTimeStr());
		t.setInitpwd(INITPWD.UnModi.getCode());//标识是否为默认密码
		setModiTimeAndUser(t);
		userDao.add(t);
		addPwdHistory(t);//密码插入密码历史表
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.UserService#updateUserState(java.lang.String, java.lang.String)
	 * 更新用户状态（批量处理）若是解锁，错误密码次数清零
	 */
	@Override
	@OperLoggable(module="用户管理", description="更新用户状态")
	public String updateUserState(String stt,String id){
		List<String> ids = StringUtil.toList(id);
		List<String> userStts = userDao.getUserState(ids);
		for(String userStt : userStts){
			if(stt.equals(userStt)){
				return Consts.ERROR_CODE;
			}
		}
		
		//解锁时 错误密码次数清零
		if(USERSTATUS.Normal.getCode().equals(stt)){
			userDao.setPwdErrorNumZeroBatch(ids);
		}
		
		UserEntity t = new UserEntity();
		setModiTimeAndUser(t);
		t.setUser_state(stt);
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("t", t);
		map.put("list", ids);
		userDao.updateUserStt(map);
		return Consts.SUCCESS_CODE;
	}
	
	/**
	 * 设置修改人和修改时间
	 * @param t
	 */
	private void setModiTimeAndUser(UserEntity t){
		t.setModi_time(DateUtil.todayStr() + " " + DateUtil.curTimeStr());
		t.setModi_user(Common.getAuthenticatedUsername());
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.UserService#usersByUsername(java.lang.String)
	 */
	@Override
	public List<UserEntity> usersByUsername(String username) {
		return userDao.usersByUsername(username);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.UserService#rolesByUserId(java.lang.String)
	 */
	@Override
	public List<RoleEntity> rolesByUserId(String userId) {
		return userDao.rolesByUserId(userId);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.UserService#updateUserPwd(java.lang.String, java.lang.String, java.lang.String)
	 * 修改密码
	 * 1校验旧密码是否正确；2校验新密码不能与最近N次密码相同；3更新密码；4新密码插入密码历史表中
	 */
	@Override
	@OperLoggable(module="用户管理", description="修改登录密码")
	public String updateUserPwd(String oldPwd, String newPwd, String userId) {
		String msg = "";
		UserEntity user = getById(userId);
		String userName = user.getUser_name();
		if(!passwordEncoder.matches(oldPwd, user.getUser_pwd())){
			msg = "旧密码不正确！";
			return msg;
		}
		
		List<String> hisPwds = userDao.getPwdHistory(SysParamUtil.getSysParamValueInt(Consts.QUERY_NUM),PLATFORMTYPE.Background.getCode(),userName);
		for(String pwd : hisPwds){
			if(passwordEncoder.matches(newPwd, pwd)){
				msg = "新密码不能与最近"+ SysParamUtil.getSysParamValueInt(Consts.QUERY_NUM) + "次密码相同！";
				return msg;
			}
		}
		
		user.setUser_pwd(passwordEncoder.encode(newPwd));
		user.setInitpwd(INITPWD.HasModi.getCode());//标识登录密码已修改
		user.setPwd_modify_date(DateUtil.todayStr() + " " + DateUtil.curTimeStr());
		setModiTimeAndUser(user);
		
		userDao.updateUserPwd(user);
		addPwdHistory(user);//新密码插入密码历史表中
		return Consts.SUCCESS_CODE;
	}
	
	/**
	 * 插入密码历史表
	 * @param user
	 */
	private void addPwdHistory(UserEntity user){
		PwdHistoryEntity pwdHis = new PwdHistoryEntity();
		
		pwdHis.setUser_name(user.getUser_name());
		pwdHis.setLogin_pwd(user.getUser_pwd());
		pwdHis.setCreate_time(user.getPwd_modify_date());
		pwdHis.setPlatform_type(PLATFORMTYPE.Background.getCode());
		
		userDao.addPwdHistory(pwdHis);
	}
	/* (non-Javadoc)
	 * @see com.sunyard.service.system.UserService#setDefultPwd(java.lang.String)
	 */
	@Override
	@OperLoggable(module="用户管理", description="重置登录密码")
	public void setDefultPwd(String id) {
		UserEntity user = new UserEntity();
		setModiTimeAndUser(user);
		user.setUser_pwd(passwordEncoder.encode(SysParamUtil.getSysParamValueStr(Consts.DEFAULT_LOGIN_PWD)));
		user.setInitpwd(INITPWD.UnModi.getCode());//重置为未修改状态
		user.setPwd_modify_date(DateUtil.todayStr() + " " + DateUtil.curTimeStr());
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("t", user);
		map.put("list", StringUtil.toList(id));
		userDao.setDefultPwd(map);
		
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.UserService#assignRole(java.lang.String, java.lang.String)
	 * 先删除用户以前角色
	 * 然后批量插入分配角色
	 */
	@Override
	@OperLoggable(module="用户管理", description="角色分配")
	public void assignRole(String userId, String rolesId) {
		userDao.deleteUserRole(Arrays.asList(userId));
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("list", StringUtil.toList(rolesId));
		userDao.insertUserRoles(map);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.UserService#updatePwdErrorNum(java.lang.String)
	 * 根据用户名更新密码错误次数
	 */
	@Override
	public void updatePwdErrorNum(String userName) {
		userDao.updatePwdErrorNum(userName);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.UserService#clearPwdErrTimes(java.lang.String)
	 * 密码错误次数清零
	 */
	@Override
	public void clearPwdErrTimes(String userName) {
		userDao.setPwdErrorNumZero(userName);
	}

	/* (non-Javadoc)
	 * @see com.sunyard.service.system.UserService#queryUserRoles(com.sunyard.entity.system.UserEntity)
	 * 查询用户角色列表
	 */
	@Override
	public List<Map<String, Object>> queryUserRoles(UserEntity userEntity) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("t", userEntity);
		map.put("platform_type", PLATFORMTYPE.Background.getCode());
		return userDao.queryUserRoles(map);
	}

	@Override
	public String queryUserPwd(String userName) {
		return userDao.queryUserPwd(userName);
	}

	@Override
	public String queryUserRoleId(String userName) {
		return userDao.queryUserRoleId(userName);
	}


}

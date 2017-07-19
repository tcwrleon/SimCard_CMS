package com.sunyard.service.system;

import java.util.List;
import java.util.Map;

import com.sunyard.entity.system.RoleEntity;
import com.sunyard.entity.system.UserEntity;
import com.sunyard.service.BaseService;

/**
 * @author mumu
 *
 */
public interface UserService extends BaseService<UserEntity>{
	
	/**
	 * 更新用户状态 锁定或者正常
	 * @param stt 用户状态
	 * @param id 用户编号集合
	 */
	String updateUserState(String stt,String id);
	
	/**
	 * 根据用户名查询用户信息
	 * @param username
	 * @return 用户对象集合
	 */
	List<UserEntity> usersByUsername(String username);
	
	/**
	 * 根据用户编号查询已分配角色信息
	 * @param userId
	 * @return 角色对象集合
	 */
	List<RoleEntity> rolesByUserId(String userId);
	
	/**
	 * 修改用户密码
	 * @param oldPwd 旧密码
	 * @param newPwd 新密码
	 * @param userId 
	 * @return 返回提示信息
	 */
	String updateUserPwd(String oldPwd, String newPwd, String userId);
	
	/**
	 * 密码重置
	 * @param id 用户编号
	 */
	void setDefultPwd(String id);
	
	/**
	 * 角色分配
	 * @param userId
	 * @param rolesId 角色集字符串，使用“,”分割
	 */
	void assignRole(String userId, String rolesId);

	/**
	 * 更新用户输入错误密码次数
	 * @return
	 */
	void updatePwdErrorNum(String userName);

	/**
	 * 密码错误次数清零
	 * @param userName
	 */
	void clearPwdErrTimes(String userName);

	/**
	 * 查询用户角色
	 * @param userEntity
	 * @return
	 */
	List<Map<String, Object>> queryUserRoles(UserEntity userEntity);

	/**
	 * 查询用户加密后的密码
	 * @param userName
	 */
	String queryUserPwd(String userName);
	
	/**
	 * 查询用户的role_id
	 * @param userName
	 * @return
	 */
	String queryUserRoleId(String userName);
}

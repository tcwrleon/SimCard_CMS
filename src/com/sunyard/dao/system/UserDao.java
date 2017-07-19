package com.sunyard.dao.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.sunyard.dao.BaseDao;
import com.sunyard.entity.system.PwdHistoryEntity;
import com.sunyard.entity.system.RoleEntity;
import com.sunyard.entity.system.UserEntity;

public interface UserDao extends BaseDao<UserEntity> {

	void updateUserStt(Map<String, Object> map);

	void deleteUserRole(List<String> list);

	List<UserEntity> usersByUsername(String username);

	List<RoleEntity> rolesByUserId(String userId);

	boolean updateUserPwd(UserEntity user);

	void setDefultPwd(Map<String, Object> map);

	void insertUserRoles(Map<String, Object> map);
	
	List<String> getUserState(List<String> list);

	void addPwdHistory(PwdHistoryEntity pwdHis);

	List<String> getPwdHistory(@Param("queryNum") int queryNum, @Param("platType") String platType,
			@Param("userName") String userName);

	void updatePwdErrorNum(String userName);
	
	void setPwdErrorNumZero(String userName);

	List<Map<String, Object>> queryUserRoles(Map<String,Object> map);
	
	void setPwdErrorNumZeroBatch(List<String> list);

	String queryUserPwd(String userName);
	
	String queryUserRoleId(String userName);//获取role_id
}

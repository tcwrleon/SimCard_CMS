package com.sunyard.security;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sunyard.base.model.Consts;
import com.sunyard.entity.system.ResourceEntity;
import com.sunyard.entity.system.RoleEntity;
import com.sunyard.entity.system.UserEntity;
import com.sunyard.enums.USERSTATUS;
import com.sunyard.service.system.ResourceService;
import com.sunyard.service.system.UserService;
import com.sunyard.util.DateUtil;
import com.sunyard.util.SysParamUtil;

/**
 * @author mumu
 * 用户验证实际类
 * 1登录验证 ；2通过加载该用户角色
 */
@Service
public class MyUserDetailServiceImpl implements UserDetailsService {
	@Resource
	private UserService userService;
	@Resource
	private ResourceService resourceService;
	private final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
	protected Logger logger = Logger.getLogger(MyUserDetailServiceImpl.class);
	
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 * 验证用户是否存在
	 */
	@SuppressWarnings("deprecation")
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {		
		List<UserEntity> users =  userService.usersByUsername(username);
		if(users == null ||users.size() == 0){
			logger.info((new StringBuilder()).append("Query returned no results for user '").append(username).append("'").toString());
			throw new UsernameNotFoundException(messages.getMessage("MyUserDetailServiceImpl.notFound", new Object[] { username },"用户名{0}不存在"), username);	
		}
		
		UserEntity user = users.get(0);
		boolean accountNonExpired = true;
		boolean accountNonLocked = true;
		boolean credentialsNonExpired = true;
		boolean enabled = true;
		if(USERSTATUS.Lock.getCode().equals(user.getUser_state())){//判断用户是否被锁定
			accountNonLocked = false;
			enabled = false;
		}
		
		//密码有效期校验(按月被屏蔽，现在按天数来校验)
		/*if(DateUtil.compareDate(DateUtil.addOrSubMonth(DateUtil.parseDate(user.getPwd_modify_date()),Consts.PWD_VALID_PERIOD), DateUtil.today()) == -1){
			credentialsNonExpired = false;
		}*/
		try {
			if(DateUtil.daysBetween(user.getPwd_modify_date(), DateUtil.todayStr()) > SysParamUtil.getSysParamValueInt(Consts.PWD_VALID_PERIOD)){
				credentialsNonExpired = false;
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		User userDetails = new User(user.getUser_name(), user.getUser_pwd(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, loadUserAuthorities(user));
		return userDetails;
	}
	
	
	/**
	 * 加载该用户角色
	 * @param user
	 * @return
	 */
	@SuppressWarnings({ "deprecation" })
	private Set<GrantedAuthority> loadUserAuthorities(UserEntity user){
		String username = user.getUser_name();
		List<RoleEntity> userRoles = userService.rolesByUserId(user.getUser_id());
		if(userRoles == null || userRoles.size() == 0){
			logger.info((new StringBuilder()).append("User '").append(username).append("' has no authorities and will be treated as 'not found'").toString());
				throw new UsernameNotFoundException(messages.getMessage("MyUserDetailServiceImpl.noAuthority", new Object[] { username },"没有为用户{0}指定角色"), username);
		}
		
		List<ResourceEntity> resources = resourceService.getUserResourcesByName(username);
		if(resources == null || resources.size() == 0){
			logger.info((new StringBuilder()).append("User '").append(username).append("' has no authorities and will be treated as 'not found'").toString());
			throw new UsernameNotFoundException(messages.getMessage("MyUserDetailServiceImpl.noAuthority", new Object[] { username },"没有为用户{0}指定权限"), username);
		}
		
		Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
		for(RoleEntity role : userRoles){
			authSet.add(new SimpleGrantedAuthority(role.getRole_id()));
		}
		return authSet;
	}
}
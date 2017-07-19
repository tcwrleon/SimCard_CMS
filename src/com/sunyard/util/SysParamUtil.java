package com.sunyard.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sunyard.entity.system.SysParamEntity;
import com.sunyard.service.system.SysParamService;

/**
 * @author mumu
 * 系统参数缓存工具类
 */
@Service
public class SysParamUtil {
	public static final String CACHE_SYSPARAM_MAP = "sysParamMap";
	@Resource
	private SysParamService sysParamService;
	private static SysParamService staticSysParamService;
	
	@PostConstruct
	private void cacheSysParam(){
		staticSysParamService = sysParamService;
		CacheUtil.remove(CACHE_SYSPARAM_MAP);
		List<SysParamEntity> list = staticSysParamService.queryAll(new SysParamEntity());
		if(list == null) {
			list = new ArrayList<SysParamEntity>();
		}
		CacheUtil.put(CACHE_SYSPARAM_MAP, list);
		
	}
	/**
	 * 根据参数编号获取值
	 * @param paramCode
	 * @return
	 */
	public static String getSysParamValueStr(String paramCode){
		if(!StringUtil.isEmpty(paramCode)){
			for(SysParamEntity sysParam : getSysParamList()){
				if(paramCode.equals(sysParam.getParam_code())){
					return sysParam.getParam_value();
				}
			}
		}
		return "";
	}
	
	public static int getSysParamValueInt(String paramCode){
		String value = getSysParamValueStr(paramCode);
		if(!StringUtil.isEmpty(value)){
			return Integer.parseInt(value);
		}
		return 0;
	}
	/**
	 * 获取所有系统参数
	 * @return
	 */
	private static List<SysParamEntity> getSysParamList(){
		@SuppressWarnings("unchecked")
		List<SysParamEntity> sysParamList = (List<SysParamEntity>)CacheUtil.get(CACHE_SYSPARAM_MAP);//从缓存取
		if (sysParamList==null){
			sysParamList = staticSysParamService.queryAll(new SysParamEntity());
			CacheUtil.put(CACHE_SYSPARAM_MAP, sysParamList);//放进缓存
		}
		if (sysParamList == null){
			sysParamList = new ArrayList<SysParamEntity>();
		}
		return sysParamList;
	}
}

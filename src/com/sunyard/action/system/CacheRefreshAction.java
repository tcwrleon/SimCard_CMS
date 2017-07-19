package com.sunyard.action.system;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sunyard.action.BaseAction;
import com.sunyard.base.model.Consts;
import com.sunyard.pulgin.ReflectHelper;
import com.sunyard.security.MySecurityMetadataSource;
import com.sunyard.util.CacheUtil;
import com.sunyard.util.DDUtil;
import com.sunyard.util.SpringBeanFactoryUtil;
import com.sunyard.util.SysParamUtil;

/**
 * @author mumu
 * 缓存刷新
 */
@Controller
@Scope("prototype")
public class CacheRefreshAction extends BaseAction{
	@Resource
    private SpringBeanFactoryUtil beanFactoryUtil;
	/**
	 * 跳转刷新页面
	 * @return
	 */
	public String toQuery(){
		return "query";
	}
	
    /**
     * 提交刷新
     * @return
     */
    public String doCacheRefresh(){
    	List<String> checks = Arrays.asList(request.getParameterValues("refresh"));
    	logger.info(checks);
    	if(checks.contains("dict")){
    		logger.info("刷新数据字典缓存");
    		reloadDictCache();
    	}
    	if(checks.contains("sysParam")){
    		logger.info("刷新系统参数缓存");
    		CacheUtil.remove(SysParamUtil.CACHE_SYSPARAM_MAP);
    	}
    	if(checks.contains("roleResources")){
    		logger.info("刷新业务权限缓存");
    		reloadResourceDefine();
    	}
    	
    	request.setAttribute(Consts.TIP_MSG, "缓存刷新成功");
    	return Consts.SUCCESS;
    }
    
    /**
     * 刷新数据字典
     */
    private void reloadDictCache(){
    	DDUtil ddUtil = beanFactoryUtil.getBean(DDUtil.class);
    	ReflectHelper.invoke(ddUtil, "putAllInCache");
    }
    
    /**
     * 刷新业务权限
     */
    private void reloadResourceDefine(){
    	MySecurityMetadataSource metadata = (MySecurityMetadataSource)beanFactoryUtil.getBean("mySecurityMetadataSource");
    	ReflectHelper.setValueByFieldName(metadata, "resourceMap", null);
    	ReflectHelper.invoke(metadata, "loadResourceDefine");
    }

}

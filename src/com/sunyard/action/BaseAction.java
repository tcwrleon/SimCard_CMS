package com.sunyard.action;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.sunyard.base.model.Consts;
import com.sunyard.base.model.ParamCover;
import com.sunyard.entity.system.ResourceEntity;
import com.sunyard.pulgin.PageView;
import com.sunyard.util.DDUtil;
import com.sunyard.util.ParamUtil;
import com.sunyard.util.StringUtil;

/**
 * 实现HttpServletRequest等实例化
 * 分页对象实例化
 * 参数传递
 * 缓存取数据字典放在HttpServletRequest中
 * @author mumu
 *
 */
public class BaseAction implements ServletRequestAware,ServletResponseAware  {
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ServletContext application;
	protected HttpSession session;
	protected Logger logger = Logger.getLogger(BaseAction.class);
	protected PageView pageView = null;
	
	/* (non-Javadoc)
	 * @see org.apache.struts2.interceptor.ServletRequestAware#setServletRequest(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		session = request.getSession();
		application = session.getServletContext();
		paramCover(request);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.struts2.interceptor.ServletResponseAware#setServletResponse(javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	/**
	 * 分页对象实例化
	 * @return
	 */
	protected PageView getPageView() {
		int pageNo = ParamUtil.getInt(request, "pageNo", 1);
		pageView = new PageView(pageNo);
		return pageView;
	}
	
	/**
	 * 参数传递
	 * @param request
	 */
	private void paramCover(HttpServletRequest request){
		request.setAttribute(Consts.PARAM_COVER, new ParamCover(request));
	}
	
	/**
	 * 从缓存获取数据字典
	 * @param paramArr 要查询的key数组
	 * @throws Exception
	 */
	protected void getDirtMap(String[] paramArr) throws Exception{
		for(int i=0;i<paramArr.length;i++){
			String param = paramArr[i];
			request.setAttribute(param, DDUtil.getDDContentsByKey(param));
		}
	}
	
	/**
	 * 根据菜单编号获取列表页面操作按钮
	 * @return
	 */
	protected void getBtnHtmlByMenuId(){
		getBtnHtmlByMenuId("",false);
	}
	
	/**根据菜单编号获取列表页面操作按钮
	 * @param htmlFormat 按钮样式
	 * @param flag true来自报表查询页面；false 为普通查询页面
	 */
	@SuppressWarnings("unchecked")
	protected void getBtnHtmlByMenuId(String htmlFormat,boolean flag){
		if(StringUtil.isEmpty(htmlFormat)){
			htmlFormat = "<a class='button' onclick='{0}()'>{1}</a>&nbsp;";
		}
		
		StringBuffer html = new StringBuffer();
		String menuId = getMenuId();
		
		if(!StringUtil.isEmpty(menuId)){
			List<ResourceEntity> btns = (List<ResourceEntity>)session.getAttribute("btnList");
			for(ResourceEntity btn : btns){
				if(menuId.equals(btn.getParent_id())){
					if(flag){
						html.append(StringUtil.getMessage(htmlFormat, '"'+ btn.getUrl() + '"',btn.getPrivilege_name()));	
					}else{
						html.append(StringUtil.getMessage(htmlFormat,  btn.getUrl(),btn.getPrivilege_name()));
					}
				}
			}
		}
		request.setAttribute("btnHtml", html.toString());
	}
	
	/**
	 * 获取菜单编号
	 * @return
	 */
	private String getMenuId(){
		String menuId = ParamUtil.get(request, "menuId");
		if(!StringUtil.isEmpty(menuId)){
			session.setAttribute("curMenuId", menuId);
		}else{
			menuId = (String)session.getAttribute("curMenuId");
		}
		return menuId;
	}
	
	
}

package com.sunyard.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sunyard.base.model.Consts;
import com.sunyard.entity.system.DictEntity;
import com.sunyard.entity.system.HtmlEntity;
import com.sunyard.entity.system.InitQueryPageEntity;
import com.sunyard.pulgin.PageView;
import com.sunyard.service.ReportService;
import com.sunyard.util.DDUtil;
import com.sunyard.util.DateUtil;
import com.sunyard.util.ParamUtil;
import com.sunyard.util.StringUtil;

/**
 * 报表公共控制类
 * @author mumu
 *
 * @param <T>
 */
public abstract class ReportAction<T> extends BaseAction {
	//初始查询页面对象
	protected InitQueryPageEntity initQueryPage = new InitQueryPageEntity();
	//报表页面按钮样式
	private String btnHtmlFormat = "<a class='button' onclick='doExport({0})'>{1}</a>&nbsp;";
	protected String inputHtml = "<li><label>{0}：</label> <input class='text' type='text' value='{1}' name='{2}'/></li>";
	protected String headSelectHtml = "<li><label>{0}：</label> <select class='select' name='{1}' id='{2}'>";
	protected String tailSelectHtml = "</select></li>";
	protected String optionHtml = "<option value ='{0}'>{1}</option>";
	protected String setSelectValueJs = "<script type=\"text/javascript\">$('#{0}').val({1});</script>";
	
	/**
	 * 查询功能
	 * @param reportService
	 * @return
	 */
	protected String query(ReportService<T> reportService){
		PageView page = reportService.query(getPageView(), generateQueryParam());
		
		request.setAttribute("page", page); //结果集
		request.setAttribute("dateMap", initDateParam());//反显日期条件
		request.setAttribute("initQueryPageInfo", initQueryPage);//初始化查询页面信息
		getBtnHtmlByMenuId(btnHtmlFormat,true);//查询页面按钮初始化
		return Consts.REPORT;
	}
	
	/**
	 * 设置初始化页面信息的属性（抽象类）
	 */
	protected abstract void setInitQueryPageEntityProperties();
	
	/**
	 * 初始化日期查询条件，默认截止日期为当天，起始日期向前推三个月
	 * @return
	 */
	private Map<String,Object> initDateParam(){
		String beginTime= ParamUtil.get(request, "start_time");
		String endTime = ParamUtil.get(request, "end_time");
		Map<String,Object> dateMap = new HashMap<String,Object>(); 
		if(StringUtil.isEmpty(beginTime) && StringUtil.isEmpty(endTime)) {
			beginTime = DateUtil.getMonth();
			endTime = DateUtil.todayStr();
		}
		dateMap.put("start_time", beginTime);
		dateMap.put("end_time", endTime);
		return dateMap;
	}

	/**
	 * 生成所有查询条件参数，供查询和导出使用
	 * @return
	 */
	protected Map<String,Object> generateQueryParam(){
		setInitQueryPageEntityProperties();
		Map<String,Object> queryParam = initDateParam();
		queryParam.put("t", initQueryPage.getOtherQueryParam());
		return queryParam;
	}
	
	/**
	 * 动态生成查询页面下拉框
	 * @param selectLabel
	 * @return
	 */
	protected String generateSelectLabel(HtmlEntity selectLabel){
		List<DictEntity> dicts = DDUtil.getDDContentsByKey(selectLabel.getDictKey());
		StringBuffer html = new StringBuffer();
		html.append(StringUtil.getMessage(headSelectHtml, new String[]{selectLabel.getLabel(),selectLabel.getName(),selectLabel.getId()}));
		html.append(StringUtil.getMessage(optionHtml, "", "全部"));
		for(DictEntity dict : dicts){
			html.append(StringUtil.getMessage(optionHtml, StringUtil.forbidNull(dict.getVal()), StringUtil.forbidNull(dict.getPrompt())));
		}
		html.append(tailSelectHtml);
		html.append(StringUtil.getMessage(setSelectValueJs, selectLabel.getId(), StringUtil.forbidNull(selectLabel.getValue())));
		
		return html.toString();
	}
	
}

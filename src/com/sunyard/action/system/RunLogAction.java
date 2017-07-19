package com.sunyard.action.system;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sunyard.action.ReportAction;
import com.sunyard.entity.system.HtmlEntity;
import com.sunyard.entity.system.RunLogEntity;
import com.sunyard.enums.DICTKEY;
import com.sunyard.service.system.RunLogService;
import com.sunyard.util.ExcelUtil;
import com.sunyard.util.StringUtil;

/**
 * 系统运行日志
 * @author mumu
 *
 */
@Controller
@Scope("prototype")
public class RunLogAction extends ReportAction<RunLogEntity>{
	private RunLogEntity runLogEntity;
	@Resource
	private RunLogService runLogService;
	private String columnStr = "run_time,system_type,system_action";
	private String titleStr = "日期,系统,系统动作";
	
	
	/**
	 * 查询功能
	 * 支持分页模糊查询
	 * @return
	 * @throws Exception 
	 */
	public String toQuery() throws Exception{
		return query(runLogService);
	}
	
	/* (non-Javadoc)
	 * @see com.sunyard.action.ReportAction#setInitQueryPageEntityProperties()
	 */
	protected void setInitQueryPageEntityProperties(){
		if(runLogEntity == null){
			runLogEntity = new RunLogEntity();
		}
		initQueryPage.setOtherQueryParam(runLogEntity);
		initQueryPage.setQueryListContents(StringUtil.toList(columnStr));
		initQueryPage.setQueryListTitles(StringUtil.toList(titleStr));
		initQueryPage.setQueryPageUrl("runLogAction_toQuery");
		initQueryPage.setQueryPageTitle("系统运行日志查询");
		initQueryPage.setQueryConditionHtml(initOtherQueryConditionHtml());
	}
	
	/**
	 * 初始化非日期查询条件Html
	 * @return
	 */
	private String initOtherQueryConditionHtml(){
		HtmlEntity select = new HtmlEntity();
		select.setDictKey(DICTKEY.K_PLATTYPE.toString());
		select.setLabel("平台类型");
		select.setName("runLogEntity.system_type");
		select.setId("system_id");
		select.setValue(runLogEntity.getSystem_type());	
		return generateSelectLabel(select);
	}
	
	/**
	 * 导出功能
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws IOException
	 */
	public void doExport() throws RowsExceededException, WriteException, IOException{
		List<RunLogEntity> list = runLogService.queryAll(generateQueryParam());
		Map<String,String> dictMap = new HashMap<String,String>();
		dictMap.put("system_type", DICTKEY.K_PLATTYPE.toString());
		ExcelUtil.excelExport(list, columnStr, titleStr, dictMap, response, "系统运行日志");
	}

	public RunLogEntity getRunLogEntity() {
		return runLogEntity;
	}

	public void setRunLogEntity(RunLogEntity runLogEntity) {
		this.runLogEntity = runLogEntity;
	}
	
}

package com.sunyard.action.system;


import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sunyard.action.ReportAction;
import com.sunyard.entity.system.OperLogEntity;
import com.sunyard.service.system.OperLogService;
import com.sunyard.util.ExcelUtil;
import com.sunyard.util.StringUtil;

/**
 * 用户操作日志
 * @author mumu
 *
 */
@Controller
@Scope("prototype")
public class OperLogAction extends ReportAction<OperLogEntity>{
	private OperLogEntity operLogEntity;
	@Resource
	private OperLogService operLogService;
	private String columnStr = "user_id,user_name,module_name,oper_desc,class_name,method_name,oper_result,user_ip,oper_date";
	private String titleStr = "用户ID,用户名称,模块名称,操作名称,类名,方法名,操作结果,用户IP,操作时间";
	
	
	/**
	 * 查询功能
	 * 支持分页模糊查询
	 * @return
	 * @throws Exception 
	 */
	public String toQuery() throws Exception{
		return query(operLogService);
	}
	
	/* (non-Javadoc)
	 * @see com.sunyard.action.ReportAction#setInitQueryPageEntityProperties()
	 */
	protected void setInitQueryPageEntityProperties(){
		if(operLogEntity == null){
			operLogEntity = new OperLogEntity();
		}
		initQueryPage.setOtherQueryParam(operLogEntity);
		initQueryPage.setQueryListContents(StringUtil.toList(columnStr));
		initQueryPage.setQueryListTitles(StringUtil.toList(titleStr));
		initQueryPage.setQueryPageUrl("operLogAction_toQuery");
		initQueryPage.setQueryPageTitle("用户操作日志查询");
		initQueryPage.setQueryConditionHtml(initOtherQueryConditionHtml());
	}
	
	/**
	 * 初始化非日期查询条件Html
	 * @return
	 */
	private String initOtherQueryConditionHtml(){
	
		StringBuffer html = new StringBuffer();
		html.append(StringUtil.getMessage(inputHtml, new String[]{"用户ID",StringUtil.forbidNull(operLogEntity.getUser_id()),"operLogEntity.user_id"}));
		html.append(StringUtil.getMessage(inputHtml, new String[]{"用户名称",StringUtil.forbidNull(operLogEntity.getUser_name()),"operLogEntity.user_name"}));
		
		return html.toString();
	}
	
	/**
	 * 导出功能
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws IOException
	 */
	public void doExport() throws RowsExceededException, WriteException, IOException{
		List<OperLogEntity> list = operLogService.queryAll(generateQueryParam());
		ExcelUtil.excelExport(list, columnStr, titleStr, null, response, "用户操作日志");
	}
	
	public OperLogEntity getOperLogEntity() {
		return operLogEntity;
	}

	public void setOperLogEntity(OperLogEntity operLogEntity) {
		this.operLogEntity = operLogEntity;
	}
}

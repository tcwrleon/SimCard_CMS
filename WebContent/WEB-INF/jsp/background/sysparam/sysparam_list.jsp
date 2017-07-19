<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>系统参数管理</title>
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
	<script type="text/javascript" src="${ctx}/javascript/common/comm.js"></script>
</head>

<body>
	<!-- 页面标题 -->
	<div class="main-title" id="Title">
	       系统参数管理	
	</div>
    	
    <!-- 查询条件表单 -->
	<form action="sysParamAction_toQuery" method="post" name="queryForm" >
		<ul id="mainAction" class="main-actions clearfix">
			<li>
				<label>参数类别：</label> 
				<select class="select" name="sysParamEntity.param_type" id="param_type">
					<option value="">全部</option>
					<c:forEach items="${K_PARAMTYPE}" var="rst">
						<option value ="${rst.val}">${rst.prompt}</option>
					</c:forEach>
				</select>
			</li>
			
			<li>
				<label>参数名称：</label> 
				<input class="text" type="text" value="${back.param_name}" name="sysParamEntity.param_name" id="param_name" />
			</li>
			
			
			<li><a class='sch-button' onclick='toQuery()'>查询</a></li>	
		</ul>	
	</form>
		
	<!-- 操作按钮显示域 -->
	<div class="toolBar">
		<span class="toolbar-icon"></span>
		系统参数管理列表
		<div class="privileges" id="myPrivileges">
	       	<!-- 在此显示页面权限按钮 -->
	       	${btnHtml}	
		</div>
	</div>
  
    <div id="scrollDiv" style="width: 100%; overflow-y: auto; height: 100%;">
    	<!-- 结果列表显示区 -->
		<table class="data-content" width="100%" border="1" cellpadding="0" cellspacing="0">      
	    	<thead>
	            <tr align="center" valign="middle">
	            	<th class="checkBox">
						<input type="checkbox" id="selectAll" onclick="checkedAll()"/>
					</th>
					<th>参数类别</th>
	            	<th>参数ID</th>
					<th>参数名称</th>
					<th>值</th>
					<th>说明</th>
	            </tr>
	        </thead>
			<tbody id="TableData">
				<c:forEach items="${page.result}" var="rst">
					<tr style="text-align: center;">
						<td><input type="checkbox" name="item" value="${rst.param_code}"/></td>
			        	<td>${rst.param_type}</td>   	
			        	<td>${rst.param_code}</td>	
			        	<td>${rst.param_name}</td>
			        	<td>${rst.param_value}</td>
			        	<td>${rst.param_desc}</td>
					</tr>
				</c:forEach>
				<c:if test="${empty page.result}">
					<tr>
						<td height="25" colspan="6" align="center">无相关信息</td>
					</tr>
				</c:if>
			</tbody>
		</table>
		
		<!-- 翻页按钮显示区 -->
		<div class="page-tag clearfix">
			<div style="float:left">
			<form action="sysParamAction_toQuery" method="post" name="listForm">
				<input type="hidden" name="pageNo"/>
				${paramCover.unCoveredForbidInputs } ${page.footerHtml}
			</form>
			</div>
			<div class="page-num">${page.toolbarHtml}</div>
		</div>
		
		<!-- 按钮表单提交区 -->
		<form method="post" name="detailForm">
			<input type="hidden" name="selectedId" />
			<input type="hidden" name="_backUrl" value="sysParamAction_toQuery"/>
				${paramCover.coveredInputs}
		</form>
    </div>
    
	<script type="text/javascript">
		var checkedLists = $("input[name='item']");
		var queryUrl = "sysParamAction_toQuery";
		var frame = '${frame}'
		
		//初始化下拉框的值
		function initSelectLabelValue(){
			$("#param_type").val('${back.param_type}');
		}
		
		$(document).ready(function() {
			initGlobalVar(checkedLists,queryUrl,frame);
			initSelectLabelValue();
			selectedCssChange();
		})
		
		function toQuery() {
			queryForm.submit();
		}
		
		function sysParamAction_toAdd(){
			showLayerPage('sysParamAction_toAdd','','系统参数新增');
		}
		
		function sysParamAction_toModi() {
			isPopupWindow = true;
			if(!singleSelection("修改","系统参数","sysParamAction_toModi")) return;
		}
		
		function sysParamAction_delete() {
			if(!multSelection("删除","系统参数","sysParamAction_delete")) return;
		}
		
	</script>
</body>
</html>

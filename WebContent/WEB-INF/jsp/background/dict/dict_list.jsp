<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>数据字典管理</title>
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
	<script type="text/javascript" src="${ctx}/javascript/common/comm.js"></script>
</head>

<body>
	<!-- 页面标题 -->
	<div class="main-title" id="Title">
	       数据字典管理	
	</div>
    	
    <!-- 查询条件表单 -->
	<form action="dictAction_toQuery" method="post" name="queryForm" >
		<ul id="mainAction" class="main-actions clearfix">
			<li>
				<label>数据字典名称：</label> 
				<input class="text" type="text" value="${back.dictMap.prompt}" name="dictEntity.dictMap.prompt" id="dictmap_prompt" />
			</li>
			
			<li>
				<label>字典说明：</label> 
				<input class="text" type="text" value="${back.prompt}" name="dictEntity.prompt" id="prompt" />
			</li>
			
			
			<li><a class='sch-button' onclick='toQuery()'>查询</a></li>	
		</ul>	
	</form>
		
	<!-- 操作按钮显示域 -->
	<div class="toolBar">
		<span class="toolbar-icon"></span>
		数据字典管理列表
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
					<th>名称</th>
	            	<th>主键</th>
					<th>值</th>
					<th>说明</th>
	            </tr>
	        </thead>
			<tbody id="TableData">
				<c:forEach items="${page.result}" var="rst">
					<tr style="text-align: center;">
						<td><input type="checkbox" name="item" value="${rst.sun_key}#${rst.val}"/></td>
			        	<td>${rst.dictMap.prompt}</td>   	
			        	<td>${rst.sun_key}</td>	
			        	<td>${rst.val}</td>
			        	<td>${rst.prompt}</td>
					</tr>
				</c:forEach>
				<c:if test="${empty page.result}">
					<tr>
						<td height="25" colspan="5" align="center">无相关信息</td>
					</tr>
				</c:if>
			</tbody>
		</table>
		
		<!-- 翻页按钮显示区 -->
		<div class="page-tag clearfix">
			<div style="float:left">
			<form action="dictAction_toQuery" method="post" name="listForm">
				<input type="hidden" name="pageNo"/>
				${paramCover.unCoveredForbidInputs } ${page.footerHtml}
			</form>
			</div>
			<div class="page-num">${page.toolbarHtml}</div>
		</div>
		
		<!-- 按钮表单提交区 -->
		<form method="post" name="detailForm">
			<input type="hidden" name="selectedId" />
			<input type="hidden" name="_backUrl" value="dictAction_toQuery"/>
				${paramCover.coveredInputs}
		</form>
    </div>
    
	<script type="text/javascript">
		var checkedLists = $("input[name='item']");
		var queryUrl = "dictAction_toQuery";
		var frame = '${frame}'
		
		//初始化下拉框的值
		function initSelectLabelValue(){
			
		}
		
		$(document).ready(function() {
			initGlobalVar(checkedLists,queryUrl,frame);
			initSelectLabelValue();
			selectedCssChange();
		})
		
		function toQuery() {
			queryForm.submit();
		}
		
		function dictAction_toAdd(){
			showLayerPage('dictAction_toAdd','','数据字典新增');
		}
		
		function dictAction_toModi() {
			isPopupWindow = true;
			if(!singleSelection("修改","数据字典","dictAction_toModi")) return;
		}
		
		function dictAction_delete() {
			if(!multSelection("删除","数据字典","dictAction_delete")) return;
		}
		
	</script>
</body>
</html>

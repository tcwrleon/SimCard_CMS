<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>权限管理</title>
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
	<script type="text/javascript" src="${ctx}/javascript/common/comm.js"></script>
</head>

<body>
	<!-- 页面标题 -->
	<div class="main-title" id="Title">
	       权限管理	
	</div>
    	
    <!-- 查询条件表单 -->
	<form action="resourceAction_toQuery" method="post" name="queryForm" >
		<ul id="mainAction" class="main-actions clearfix">
			<li>
				<label>权限编号：</label> 
				<input class="text" type="text" value="${back.privilege_id}" name="resourceEntity.privilege_id" id="privilege_id" />
			</li>
			<li>
				<label>权限名称：</label> 
				<input class="text" type="text" value="${back.privilege_name}" name="resourceEntity.privilege_name" id="privilege_name" />
			</li>
			<li>
				<label>权限类型：</label> 
				<select class="select" name="resourceEntity.type" id="type">
					<option value="">全部</option>
					<c:forEach items="${K_RESTYPE}" var="rst">
						<option value ="${rst.val}">${rst.prompt}</option>
					</c:forEach>
				</select>
			</li>
			<li>
				<label>权限状态：</label> 
				<select class="select" name="resourceEntity.valid" id="valid">
					<option value="">全部</option>
					<c:forEach items="${K_RESSTT}" var="rst">
						<option value ="${rst.val}">${rst.prompt}</option>
					</c:forEach>
				</select>
			</li>
			<li>
				<label>平台类型：</label> 
				<select class="select" name="resourceEntity.platform_type" id="platform_type">
					<option value="">全部</option>
					<c:forEach items="${K_PLATTYPE}" var="rst">
						<option value ="${rst.val}">${rst.prompt}</option>
					</c:forEach>
				</select>
			</li>
			<li><a class='sch-button' onclick='toQuery()'>查询</a></li>	
		</ul>	
	</form>
		
	<!-- 操作按钮显示域 -->
	<div class="toolBar">
		<span class="toolbar-icon"></span>
		权限管理列表
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
					<th>平台类型</th>
	            	<th>权限编号</th>
					<th>权限名称</th>
					<th>URL</th>
					<th>父权限</th>
					<th>权限类型</th>
					<th>权限状态</th>
					<th>顺序</th>
	            </tr>
	        </thead>
			<tbody id="TableData">
				<c:forEach items="${page.result}" var="rst">
					<tr style="text-align: center;">
						<td><input type="checkbox" name="item" value="${rst.privilege_id}"/></td>
			        	<td>${rst.platform_type}</td>   	
			        	<td>${rst.privilege_id}</td>	
			        	<td>${rst.privilege_name}</td>
			        	<td>${rst.url}</td>
			        	<td>${rst.parent_id}</td>
			        	<td>${rst.type}</td>
			        	<td>${rst.valid}</td>
			        	<td>${rst.sort}</td>
					</tr>
				</c:forEach>
				<c:if test="${empty page.result}">
					<tr>
						<td height="25" colspan="9" align="center">无相关信息</td>
					</tr>
				</c:if>
			</tbody>
		</table>
		
		<!-- 翻页按钮显示区 -->
		<div class="page-tag clearfix">
			<div style="float:left">
			<form action="resourceAction_toQuery" method="post" name="listForm">
				<input type="hidden" name="pageNo"/>
				${paramCover.unCoveredForbidInputs } ${page.footerHtml}
			</form>
			</div>
			<div class="page-num">${page.toolbarHtml}</div>
		</div>
		
		<!-- 按钮表单提交区 -->
		<form method="post" name="detailForm">
			<input type="hidden" name="selectedId" />
			<input type="hidden" name="_backUrl" value="resourceAction_toQuery"/>
				${paramCover.coveredInputs}
		</form>
    </div>
    
	<script type="text/javascript">
		var checkedLists = $("input[name='item']");
		var queryUrl = "resourceAction_toQuery";
		var frame = '${frame}'
		
		//初始化下拉框的值
		function initSelectLabelValue(){
			$("#type").val('${back.type}');
			$("#valid").val('${back.valid}');
			$("#platform_type").val('${back.platform_type}');
		}
		
		
		$(document).ready(function() {
			initGlobalVar(checkedLists,queryUrl,frame);
			initSelectLabelValue();
			selectedCssChange();
		})
		
		
		function toQuery() {
			queryForm.submit();
		}
		
		function resourceAction_toAdd(){
			submitDetailForm('resourceAction_toAdd','')
		}
		
		function resourceAction_toModi() {
			if(!singleSelection("修改","权限","resourceAction_toModi")) return;
		}
		
		function resourceAction_delete() {
			if(!multSelection("删除","权限","resourceAction_delete")) return;
			
		}
		
	</script>
</body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>部门列表</title>
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
	<%@ include file="../../common/depSelectTree.jsp"%>
	<script type="text/javascript" src="${ctx}/javascript/dtree/dtree_dept.js"></script>
	<script type="text/javascript" src="${ctx}/javascript/common/comm.js"></script>
</head>

<!-- <body class="easyui-layout" data-options="fit:true"> -->
<body>
	<!-- <div data-options="region:'north'" style="border:0; overflow:visible;"> -->
	
		<!--页面标题-->
		<div class="main-title" id="Title">
	       	部门管理	
	    </div>
    	
    	<!-- 查询条件表单 -->
		<form action="departmentAction_toQuery" method="post" name="queryForm" >
			<ul id="mainAction" class="main-actions clearfix">
				<li>
					<label>部门代码：</label> 
					<input id="department_id" class="text" type="text" value="${back.department_id}" 
						name="dptEntity.department_id" />
						&nbsp;<a id="menuBtn" href="#" onclick="showMenu('#department_id','#department_name','true'); return false;">选择</a>
				</li>
				<li>
					<label>部门名称：</label> 
					<input class="text" type="text" value="${back.department_name}" name="dptEntity.department_name" id="department_name" />
				</li>
				<li><a class='sch-button' onclick='toQuery()'>查询</a></li>	
			</ul>	
			<div id="menuContent" class="menuContent" style="display:none; position: absolute; z-index:10;">
				<ul id="treeDemo" class="ztree" style="margin-top:0; width:190px;"></ul>
			</div>
		</form>
		
		<!-- 操作按钮显示域 -->
		<div class="toolBar">
			<span class="toolbar-icon"></span>
			部门管理列表
			<div class="privileges" id="myPrivileges">
	       		<!-- 在此显示页面权限按钮 -->
	       		${btnHtml}
			</div>
	    </div>
    <!-- </div> -->
    
    <%-- 显示部门树 --%>
    <%-- <div data-options="region:'west',split:true" id="area_treeboxreagent_tree" style="width:200px;background:#f7f9f9; border-left:0;">
		<div class="dtree">
		<script type="text/javascript">
			d = new dTree("d");
			<% String strTree = (String)request.getAttribute("departmentTree");
				out.println(strTree); 
			%>
			document.write(d);
		</script>
		</div>
    </div> --%>
    
    <!-- <div data-options="region:'center'" id="area"> -->
    <div id="scrollDiv" style="width: 100%; overflow-y: auto; height: 100%;">
    	<!-- 结果列表显示区 -->
		<table cellspacing="0" cellpadding="0" border="1" class="data-content" style="margin-left:0; border-left:0; border-right:0;">      
	    	<thead>
	            <tr align="center" valign="middle">
	            	<th class="checkBox">
						<input type="checkbox" id="selectAll" onclick="checkedAll()"/>
					</th>
	            	<th>部门代码</th>
					<th>部门名称</th>
					<th>部门级别</th>
					<th>上级部门</th>
					<!-- <th>部门状态</th> -->
					<th>备注</th>
	            </tr>
	        </thead>
			<tbody id="TableData">
				<c:forEach items="${page.result}" var="rst">
					<tr style="text-align: center;">
						<td><input type="checkbox" name="item" value="${rst.department_id}"/></td>
			        	<td>${rst.department_id}</td>   	
			        	<td>${rst.department_name}</td>	
			        	<td>${rst.department_level}</td>
			        	<td>${rst.department_pid}</td>
			        	<%-- <td>${rst.department_stt}</td> --%>
			        	<td>${rst.department_desc}</td>
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
			<form action="departmentAction_toQuery" method="post" name="listForm">
				<input type="hidden" name="pageNo"/>
				${paramCover.unCoveredForbidInputs } ${page.footerHtml}
			</form>
			</div>
			<div class="page-num">${page.toolbarHtml}</div>
		</div>
		
		<!-- 按钮表单提交区 -->
		<form method="post" name="detailForm">
			<input type="hidden" name="selectedId" />
			<input type="hidden" name="_backUrl" value="departmentAction_toQuery"/>
				${paramCover.coveredInputs}
		</form>
    </div>
    
	<script type="text/javascript">
	var checkedLists = $("input[name='item']");
	var queryUrl = "departmentAction_toQuery";
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
	
	function departmentAction_toAdd(){
		showLayerPage('departmentAction_toAdd','','部门新增');
	}
	
	function departmentAction_toModi() {
		isPopupWindow = true;
		if(!singleSelection("修改","部门","departmentAction_toModi")) return;
	}
	
	function departmentAction_delete() {
		if(!multSelection("删除","部门","departmentAction_delete")) return;
	}
	
		
	</script>
</body>
</html>

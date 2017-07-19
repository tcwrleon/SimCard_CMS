<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>用户管理</title>
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
	<%@ include file="../../common/depSelectTree.jsp"%>
	<script type="text/javascript" src="${ctx}/javascript/common/comm.js"></script>
</head>

<body>
	<!-- 页面标题 -->
	<div class="main-title" id="Title">
	       用户管理	
	</div>
    	
    <!-- 查询条件表单 -->
	<form action="" method="post" name="queryForm" >
		<input type="hidden" name="userEntity.department_id" id="department_id"/>
		<ul id="mainAction" class="main-actions clearfix">
			<li>
				<label>用户ID：</label> 
				<input class="text" type="text" value="${back.user_id}" name="userEntity.user_id" id="user_id" />
			</li>
			<li>
				<label>用户名称：</label> 
				<input class="text" type="text" value="${back.user_name}" name="userEntity.user_name" id="user_name" />
			</li>
			<li>
				<label>所属部门：</label> 
				<input id="department_name" class="text" type="text" readonly="readonly"  name="userEntity.department_name" value="${back.department_name}"/>
				&nbsp;<a id="menuBtn" href="#" onclick="showMenu('#department_name','#department_id'); return false;">选择</a>
			</li>
			<li>
				<label>用户状态：</label> 
				<select class="select" name="userEntity.user_state" id="user_state">
					<option value="">全部</option>
					<c:forEach items="${K_USERSTT}" var="rst">
						<option value ="${rst.val}">${rst.prompt}</option>
					</c:forEach>
				</select>	
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
		用户管理列表
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
	            	<th>用户ID</th>
					<th>用户名称</th>
					<th>所属部门</th>
					<th>创建日期</th>
					<th>密码修改时间</th>
					<th>用户类型</th>
					<th>用户状态</th>
				<!--<th>电话</th>
					<th>邮箱</th>
					<th>手机</th>
					<th>地址</th>
					<th>证件类型</th>
					<th>证件号码</th> 
					<th>备注</th>-->
	            </tr>
	        </thead>
			<tbody id="TableData">
				<c:forEach items="${page.result}" var="rst">
					<tr style="text-align: center;">
						<td><input type="checkbox" name="item" value="${rst.user_id}"/></td>
			        	<td>${rst.user_id}</td>   	
			        	<td>${rst.user_name}</td>	
			        	<td>${rst.department_name}</td>
			        	<td>${rst.create_date}</td>
			        	<td>${rst.pwd_modify_date}</td>
			        	<td>${rst.user_type}</td>
			        	<td>${rst.user_state}</td>
			        	<%-- <td>${rst.user_phone}</td>
			        	<td>${rst.user_email}</td>
			        	<td>${rst.user_mobile}</td>
			        	<td>${rst.user_addr}</td>
			        	<td>${rst.cert_type}</td>
			        	<td>${rst.user_certno}</td>
			        	<td>${rst.user_desc}</td> --%>
					</tr>
				</c:forEach>
				<c:if test="${empty page.result}">
					<tr>
						<td height="25" colspan="8" align="center">无相关信息</td>
					</tr>
				</c:if>
			</tbody>
		</table>
		
		<!-- 翻页按钮显示区 -->
		<div class="page-tag clearfix">
			<div style="float:left">
			<form action="userAction_toQuery" method="post" name="listForm">
				<input type="hidden" name="pageNo"/>
				${paramCover.unCoveredForbidInputs } ${page.footerHtml}
			</form>
			</div>
			<div class="page-num">${page.toolbarHtml}</div>
		</div>
		
		<!-- 按钮表单提交区 -->
		<form method="post" name="detailForm">
			<input type="hidden" name="selectedId" />
			<input type="hidden" name="_backUrl" value="userAction_toQuery"/>
				${paramCover.coveredInputs}
		</form>
    </div>
    
	<script type="text/javascript">
		var checkedLists = $("input[name='item']");//获取查询页面所有name为item的input标签
		var queryUrl = "userAction_toQuery";
		var frame = '${frame}'
		//初始化下拉框的值
		function initSelectLabelValue(){
			$("#user_state").val('${back.user_state}');
			$("#department_id").val('${back.department_id}');
		}
		
		$(document).ready(function() {
			initGlobalVar(checkedLists,queryUrl,frame);
			initSelectLabelValue();
			selectedCssChange();
		})
		
		function toQuery() {
			queryForm.action = "userAction_toQuery";
			queryForm.submit();
		}
		
		function userAction_doExport(){
			queryForm.action = "userAction_doExport";
			queryForm.submit();
		}
		
		function userAction_toAdd(){
			//submitDetailForm('userAction_toAdd','')
			showLayerPage('userAction_toAdd','','用户新增');
		}
		
		function userAction_toModi() {
			isPopupWindow = true
			if(!singleSelection("修改","用户","userAction_toModi")) return;
		}
		
		function userAction_delete() {
			if(!multSelection("删除","用户","userAction_delete")) return;
			
		}
		
		function userAction_lock() {
			if(!multSelection("锁定","用户","userAction_lock")) return;
		}
		
		function userAction_unlock() {
			if(!multSelection("解锁","用户","userAction_unlock")) return;
		}
		
		function userAction_toModiPwd() {
			isPopupWindow = true;
			if(!singleSelection("密码修改","用户","userAction_toModiPwd")) return;
		}
		
		function userAction_setDefultPwd() {
			if(!multSelection("重置密码","用户","userAction_setDefultPwd")) return;
			
		}
		
		function userAction_toRoleAssign() {
			if(!singleSelection("角色分配","用户","userAction_toRoleAssign")) return;
		}
		
	</script>
</body>
</html>

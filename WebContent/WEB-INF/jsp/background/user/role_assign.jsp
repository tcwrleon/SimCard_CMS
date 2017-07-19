<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>分配角色</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
<style type="text/css">
.hidden-input-border {
	border-left:0px;
	border-top:0px;
	border-right:0px;
	border-bottom:1px;
}
</style>
</head>
<body style="text-align:center;">
	<div class="window-pop" style="width:710px;">
			<div class="window-box">
				<label>用户ID：</label><input class="tool-text" style="width:160px;" type="text" id="user_id" name="user_id" value="${userInfo.user_id}" readonly="readonly" />&nbsp;
				<label>用户名称：</label><input class="tool-text" style="width:160px;" type="text" id="user_name" name="user_name" value="${userInfo.user_name}" readonly="readonly" />&nbsp;
				<label>说  明：</label><input class="tool-text" style="width:160px;" type="text" id="user_desc" name="user_desc" value="${userInfo.user_desc}" readonly="readonly" />
			</div>
			<!-- <div class="window-box">
				<label>角色ID：</label><input class="tool-text" style="width:160px;" type="text" value="" id="role_id" onkeyup="filterRoleId()" />&nbsp;
				<label>角色名称 ：</label><input class="tool-text" style="width:160px;" type="text" value="" id="role_name" onkeyup="filterRoleName()" />&nbsp;
				<input type="radio" value="1" name="role" /><label>已分配</label>&nbsp;
				<input type="radio" value="0" name="role" checked="checked" /><label>可分配</label>
				<a class='sch-button' onclick='toQuery()'>查询</a>	
			</div> -->
			<table style="width: 710px; font-size: 12px;" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td height="40" align="center" width="300">已分配角色</td>
					<td></td>
					<td align="center" width="300">可分配角色</td>
				</tr>
				<tr>
					<td>
						<table class="data-content" width="40%" border="1" cellpadding="0" cellspacing="0">      
	    					<thead>
	            				<tr align="center" valign="middle">
		            				<th class="checkBox">
										<input type="checkbox" id="select_all_user_roles" onclick="checkedAllUserRoles()"/>
									</th>
		            				<th>角色ID</th>
									<th>角色名</th>
								</tr>
	       					</thead>
							<tbody id="table_data_user_roles">
							<c:forEach items="${userRoles}" var="rst">
								<tr style="text-align: center;" id="${rst.role_id}">
									<td><input type="checkbox" name="item_user_roles" id="_${rst.role_id}" value="${rst.role_id}"/></td>
				        			<td>${rst.role_id}</td>
				        			<td>${rst.role_name}</td>     	  	
								</tr>
							</c:forEach>
							<%-- <c:if test="${empty userRoles}">
								<tr>
									<td height="25" colspan="3" align="center">无相关信息</td>
								</tr>
							</c:if> --%>
							</tbody>
						</table>
					</td>
					<td align="center">
						<input class="tool-btn" type="button" value=" < " id="toUserRoles"  onclick="toUser()"/>
						<br/>
						<br/> 
						<input class="tool-btn" type="button" value=" > " id="toOtherRoles" onclick="toOther()"/>
					</td>
					<td>
						<table class="data-content" width="40%" border="1" cellpadding="0" cellspacing="0">      
	    					<thead>
	            				<tr align="center" valign="middle">
		            				<th class="checkBox">
										<input type="checkbox" id="select_all_other_roles" onclick="checkedAllOtherRoles()"/>
									</th>
		            				<th>角色ID</th>
									<th>角色名</th>
								</tr>
	       					</thead>
							<tbody id="table_data_other_roles">
							<c:forEach items="${assignableRoles}" var="rst">
								<tr style="text-align: center;" id="${rst.role_id}">
									<td><input type="checkbox" name="item_other_roles" id="_${rst.role_id}" value="${rst.role_id}"/></td>
				        			<td>${rst.role_id}</td>
				        			<td>${rst.role_name}</td>     	  	
								</tr>
							</c:forEach>
							<%-- <c:if test="${empty assignableRoles}">
								<tr>
									<td height="25" colspan="3" align="center">无相关信息</td>
								</tr>
							</c:if> --%>
							</tbody>
						</table>
					</td>
				</tr>
			</table>
			<div style="text-align:center; padding-top:40px;">
	    		<input class="tool-btn" type="button" onclick="assignRole()" value="确 定" />&nbsp;
	    		<input class="tool-btn" type="button" value="返  回" id="closeLayer" />
			</div>
		<!-- </form> -->
	</div>
	<form method="post" name="rolesForm" action ="" >
		<input type="hidden" name="rolesId" />
		${paramCover.unCoveredForbidInputs}	
	</form>
	<script>
		$(document).ready(function() {
			$("#user_id").addClass("hidden-input-border");
			$("#user_name").addClass("hidden-input-border");
			$("#user_desc").addClass("hidden-input-border");
			$("#closeLayer").click(function(){
				history.back();
			}); 
		});
		
		function checkedAllUserRoles(){
			var userRolesList = $("input[name='item_user_roles']");
			checkedAll(userRolesList,"#select_all_user_roles");
		}
		
		function checkedAllOtherRoles(){
			var otherRolesList = $("input[name='item_other_roles']");
			checkedAll(otherRolesList,"#select_all_other_roles");
		}
		
		function checkedAll(checkedLists,id){
			for(var i = 0; i < checkedLists.length; i++){
				if($(id).is(":checked")){
					checkedLists.eq(i).prop('checked',true);
					checkedLists.eq(i).parent().parent().addClass('checkedTr');
				}else{
					checkedLists.eq(i).prop('checked',false);
					checkedLists.eq(i).parent().parent().removeClass('checkedTr');
				}
			}
		}
		
		function toUser(){
			var otherRolesList = $("input[name='item_other_roles']");
			move(otherRolesList,"item_user_roles","#table_data_user_roles");
		}
		
		function toOther(){
			var userRolesList = $("input[name='item_user_roles']");
			move(userRolesList,"item_other_roles","#table_data_other_roles");
		}
		
		function move(checkedLists,name,selector){
			var html = "";
			for (var i = 0; i < checkedLists.length; i++) {
				if(checkedLists[i].checked) {
					var id = checkedLists[i].value;
					$("#_"+id).attr("name",name);//更新checkbox属性name值
					var item = '<tr style="text-align: center;" id="' + id + '">' + $("#"+id).html() + '</tr>';
					$("#"+id).remove();
					html = html + item ;
				}
			}
			$(html).appendTo(selector);
		}
		
		function assignRole(){
			var idstr = "";
			var userRolesList = $("input[name='item_user_roles']");
			for (var i = 0; i < userRolesList.length; i++) {
				userRolesList.eq(i).prop('checked',true)
				if(userRolesList[i].checked){
					idstr = idstr + userRolesList[i].value + ",";				
				}
				
			}
			if (idstr != ""){
				idstr = idstr.substr(0, idstr.length-1);
			}else{
				alert("未分配角色！");
				return;
			}
			
			rolesForm.action = "userAction_assignRole";
			rolesForm.rolesId.value = idstr;
			rolesForm.submit();
		}
		
	</script>
</body>
</html>

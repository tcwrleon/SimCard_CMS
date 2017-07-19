<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>sim卡信息管理</title>
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
</head>
<body>
	<div class="main-title" id="Title"><!--页面标题-->
		sim卡信息管理
    </div>
	<form action="simCardAction_toQuery" method="post" name="qryForm">
	<ul id="mainAction" class="main-actions clearfix">
		<li>
			<label>sim卡编号：</label>
			<input class="text" type="text" name="sim_id" id="sim_id" value='${simCardInfo.sim_id}' />
		</li>					
		<li>
			<label>运营商：</label>
			<select class="select" name="operators" id="operators" >
				<option value="">全部</option>
				<c:forEach items="${SIM_OPERATORS}" var="rst">
					<option value="${rst.val}">${rst.prompt}</option>
				</c:forEach>
			</select>
		</li>
		<li><span class='sch-button' onclick='submitQuery()'>查询</span>
		</li>
		
	</ul>
	<input type="hidden" name="_backUrl" value="simCardAction_toQuery"/>
	</form>
    <div class="toolBar">
    	<span class="toolbar-icon"></span>
		sim卡列表
		<div class="privileges" id="myPrivileges">
    	<!-- 在此显示页面权限按钮 -->
			${btnHtml} 
<!--     	<a class="button" onclick="simCardAction_toAdd()">新增</a>
    	<a class="button" onclick="simCardAction_toModi()">修改</a>
    	<a class="button" onclick="simCardAction_toDel()">删除</a> -->
    	</div>
	</div>
	
    <div id="scrollDiv" style="overflow: auto;"><span title="双击进入详情" />
		<table class="data-content" border="1" cellpadding="0" cellspacing="0">
			<tr>
				<th class="checkBox"><input type="checkbox" id="selectAll" onclick="selectAll()"/></th>
				<th>sim卡编号</th>
				<th>手机号</th>
				<th>用户名</th>
                <th>服务密码</th>
				<th>身份证</th>
				<th>运营商</th>
				<th>省份</th>
				<th>开发人员</th>
				<th>最后动作者</th>
				<th>最后动作</th>
			</tr>
			<c:forEach items="${page.result}" var="rst">
				<tr style="text-align: center;" id="${rst.sim_id}" <%--ondblclick="toDetail(this.id);"--%>>
					<td>
						<input type="checkbox" name="item" value="${rst.sim_id}"/>
					</td>
			       	<td>${rst.sim_id}</td>
			       	<td>${rst.mobile}</td>
			       	<td>${rst.user_name}</td>
                    <td>${rst.service_pwd}</td>
					<td>${rst.id_card}</td>
			       	<td>${rst.operators}</td>
			       	<td>${rst.province}</td>
			       	<td>${rst.author}</td>
			       	<td>${rst.final_at}</td>
					<td>${rst.final_action}</td>
					</tr>
				</c:forEach>
				<c:if test="${empty page.result}">
					<tr>
						<td height="25" colspan="11" align="center">无相关信息</td>
					</tr>
				</c:if>
		</table>
	</div>
	<div class="page-tag clearfix">
		<div style="float:left">
			<form action="simCardAction_toQuery" method="post" name="listForm">
				<input type="hidden" name="pageNo"/>
				${paramCover.unCoveredForbidInputs} ${page.footerHtml}
			</form>
		</div>
		<div class="page-num">${page.toolbarHtml}</div>
	</div>	
	<form method="post" name="detailForm" action="">
		<input type="hidden" name="selectedId" id="selectedId" />
		<input type="hidden" name="_backUrl" value="simCardAction_toQuery"/>
			${paramCover.coveredInputs}
	</form>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#operators").val("${simCardInfo.operators}");
		});
		var checkedLists = $("input[name='item']");
		var queryUrl = "simCardAction_toQuery";
		for(var i = 0; i < checkedLists.length; i++) {
			checkedLists.eq(i).click(function(){
				if($(this).is(":checked")){
					$(this).parent().parent().addClass("checkedTr");	
				}else{
					$(this).parent().parent().removeClass("checkedTr");
				}
			});
		};
		
		function selectAll(){
			for(var i = 0; i < checkedLists.length; i++){
				if($('#selectAll').is(":checked")){
					checkedLists.eq(i).prop('checked',true);
					checkedLists.eq(i).parent().parent().addClass('checkedTr');
				}else{
					checkedLists.eq(i).prop('checked',false);
					checkedLists.eq(i).parent().parent().removeClass('checkedTr');
				}
			}
		}
		
		function submitQuery() {
			qryForm.submit();
		}	
		
		function simCardAction_toAdd(){
			detailForm.action = "simCardAction_toAdd";
			detailForm.submit();	
		}
		
		function simCardAction_toModi() {
			var selectedId = getSelectedIdStr();
			if(selectedId == ""){
				alert("请选择要修改的sim卡！");
				return;
			}
			
			if(selectedId.indexOf(",") > -1){
				alert("每次只能选择一张sim卡修改！");
				return;
			}		
			detailForm.action = "simCardAction_toModi";
			detailForm.selectedId.value = selectedId;
			detailForm.submit();		
		}			
		
		function simCardAction_toDel() {
			var sim_id = getSelectedIdStr();
			if(sim_id == ""){
				alert("请选择要删除的sim卡信息！");
				return;
			}else {
				if(confirm("确定要删除所选sim卡信息吗？")) {
					detailForm.action = "simCardAction_delete";					
					detailForm.selectedId.value = sim_id;
					detailForm.submit();
				}
			}
		}

		function simCardAction_doExport(){
			qryForm.action = "simCardAction_doExport";
			qryForm.submit();
		}
		
		function simCardAction_toDetail() {
			var selectedId = getSelectedIdStr();
			if(selectedId == ""){
				alert("请选择要查看的sim卡信息！");
				return;
			}
			
			if(selectedId.indexOf(",") > -1){
				alert("每次只能选择一条sim卡信息进行查看！");
				return;
			}		
			detailForm.action = "simCardAction_toDetail";
			detailForm.selectedId.value = selectedId;
			detailForm.submit();		
		}
		
		function getSelectedIdStr(){
			var idstr = "";
			for (var i = 0; i < checkedLists.length; i++) {
				if(checkedLists[i].checked) {
					idstr = idstr + checkedLists[i].value + ",";
				}
			}			
			if (idstr != ""){
				idstr = idstr.substr(0, idstr.length-1);
			}
			return idstr;
		}

		function toDetail(id){
			window.location.href = "simCardAction_toDetail?selectedId="+id;
		}
	</script>
</body>
</html>

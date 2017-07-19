<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>接收短信管理</title>
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
</head>
<body>
	<div class="main-title" id="Title"><!--页面标题-->
		接收短信管理
    </div>
	<form action="smsRecAction_toQuery" method="post" name="qryForm">
	<ul id="mainAction" class="main-actions clearfix">
		<li>
			<label>接收方：</label>
			<input class="text" type="text" name="simnum" id="simnum" value='${smsRecInfo.simnum}' />
		</li>
		<li><span class='sch-button' onclick='submitQuery()'>查询</span>
		</li>
		
	</ul>
	<input type="hidden" name="_backUrl" value="smsRecAction_toQuery"/>
	</form>
    <div class="toolBar">
    	<span class="toolbar-icon"></span>
		sim卡列表
		<div class="privileges" id="myPrivileges">
    	<!-- 在此显示页面权限按钮 -->
			${btnHtml} 
<!--     	<a class="button" onclick="smsRecAction_toAdd()">新增</a>
    	<a class="button" onclick="smsRecAction_toModi()">修改</a>
    	<a class="button" onclick="smsRecAction_toDel()">删除</a> -->
    	</div>
	</div>
	
    <div id="scrollDiv" style="overflow: auto;">
		<table class="data-content" border="1" cellpadding="0" cellspacing="0">
			<tr>
				<th class="checkBox"><input type="checkbox" id="selectAll" onclick="selectAll()"/></th>
				<th>编号</th>
				<th>接收方</th>
                <th>时间</th>
				<th>内容</th>
				<th>发送方</th>

			</tr>
			<c:forEach items="${page.result}" var="rst">
				<tr style="text-align: center;" id="${rst.id}" <%--ondblclick="toDetail(this.id);"--%>>
					<td>
						<input type="checkbox" name="item" value="${rst.id}"/>
					</td>
			       	<td>${rst.id}</td>
			       	<td>${rst.simnum}</td>
                    <td>${rst.time}</td>
			       	<td>${rst.content}</td>
			       	<td>${rst.number}</td>
					</tr>
				</c:forEach>
				<c:if test="${empty page.result}">
					<tr>
						<td height="25" colspan="6" align="center">无相关信息</td>
					</tr>
				</c:if>
		</table>
	</div>
	<div class="page-tag clearfix">
		<div style="float:left">
			<form action="smsRecAction_toQuery" method="post" name="listForm">
				<input type="hidden" name="pageNo"/>
				${paramCover.unCoveredForbidInputs} ${page.footerHtml}
			</form>
		</div>
		<div class="page-num">${page.toolbarHtml}</div>
	</div>	
	<form method="post" name="detailForm" action="">
		<input type="hidden" name="selectedId" id="selectedId" />
		<input type="hidden" name="_backUrl" value="smsRecAction_toQuery"/>
			${paramCover.coveredInputs}
	</form>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		var checkedLists = $("input[name='item']");
		var queryUrl = "smsRecAction_toQuery";
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
		
		function smsRecAction_toDel() {
			var id = getSelectedIdStr();
			if(id == ""){
				alert("请选择要删除的短信信息！");
				return;
			}else {
				if(confirm("确定要删除所选短信信息吗？")) {
					detailForm.action = "smsRecAction_delete";					
					detailForm.selectedId.value = id;
					detailForm.submit();
				}
			}
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
			window.location.href = "smsRecAction_toDetail?selectedId="+id;
		}
	</script>
</body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
	<%@ include file="jslibs.jsp"%>
	<%@ include file="csslibs.jsp"%>
</head>

<body>
	<!-- 页面标题 -->
	<div class="main-title" id="Title">
	</div>
    	
    <!-- 查询条件表单 -->
	<form action="" method="post" name="queryForm" >
		<ul id="mainAction" class="main-actions clearfix">
			${initQueryPageInfo.queryConditionHtml}
			<li>
				<label>起始日期：</label>
				<input class="text" name="start_time" type="text" id="d4311" value='${dateMap.start_time}' 
				onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'d4312\')||%y-%M-%d'})" />
				<!-- onpicked:function(){d4312.focus();}, -->
			</li>
			<li>
				<label>截止日期：</label>
				<input class="text" name="end_time" type="text" id="d4312" value='${dateMap.end_time}' 
				onfocus="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',maxDate:'%y-%M-%d'})" /> 
			</li>
			<li><a class='sch-button' onclick='toQuery()'>查询</a></li>	
		</ul>	
	</form>
		
	<!-- 操作按钮显示域 -->
	<div class="toolBar">
		<span class="toolbar-icon"></span>
		${initQueryPageInfo.queryPageTitle}
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
	            	<c:forEach items="${initQueryPageInfo.queryListTitles}" var="title">
	            		<th>${title}</th>
	            	</c:forEach>
	            </tr>
	        </thead>
			<tbody id="TableData">
				<c:forEach items="${page.result}" var="rst">
					<tr style="text-align: center;">
						<c:forEach items="${initQueryPageInfo.queryListContents}" var="column">
							<td>${rst[column]}</td>
						</c:forEach>
					</tr>
				</c:forEach>
				<c:if test="${empty page.result}">
					<tr>
						<td height="25" colspan="${fn:length(initQueryPageInfo.queryListTitles)}" align="center">无相关信息</td>
					</tr>
				</c:if>
			</tbody>
		</table>
		
		<!-- 翻页按钮显示区 -->
		<div class="page-tag clearfix">
			<div style="float:left">
			<form action="" method="post" name="listForm">
				<input type="hidden" name="pageNo"/>
				${paramCover.unCoveredForbidInputs } ${page.footerHtml}
			</form>
			</div>
			<div class="page-num">${page.toolbarHtml}</div>
		</div>
    </div>
    
	<script type="text/javascript">
		//初始化下拉框的值
		function initSelectLabelValue(){
		}
		
		$(document).ready(function() {
			initSelectLabelValue();
			document.title='${initQueryPageInfo.queryPageTitle}';
			$('#Title').html('${initQueryPageInfo.queryPageTitle}');
			listForm.action="${initQueryPageInfo.queryPageUrl}";
		})
		
		function toQuery() {
			queryForm.action = "${initQueryPageInfo.queryPageUrl}";
			queryForm.submit();
		}
		
		function doExport(url){
			queryForm.action = url;
			queryForm.submit();
		}	
	</script>
</body>
</html>

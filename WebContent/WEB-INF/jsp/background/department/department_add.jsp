<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>新增部门</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
	<%@ include file="../../common/depSelectTree.jsp"%>
<style type="text/css">
span.error {
	top:5px;
	right:68px;
	line-height:25px;
}
.hidden-input-border {
	border-left:0px;
	border-top:0px;
	border-right:0px;
	border-bottom:1px;
}
</style>
</head>
<body style="text-align:center;">
	<div class="window-pop">
		<!--显示表单内容-->
		<form action="" method="post" name="form" id="form">
			<c:if test="${empty param._backUrl}">
				<input type="hidden" name="_backUrl" value="departmentAction_toQuery"/>
			</c:if>
			<input type="hidden" name="dptEntity.department_pid" id="department_pid_hidden"/>
			${paramCover.unCoveredForbidInputs}
			<ul class="pop-list">
				<li>
					<label>部门级别：</label> 
					<select class="tool-select" name="dptEntity.department_level" id="department_level">
						<c:forEach items="${K_DPTLVL}" var="rst">
							<option value ="${rst.val}">${rst.prompt}</option>
						</c:forEach>
					</select>				
				</li>
				
				<li>
					<label>上级部门：</label>
					<input id="department_pid" type="text" class="tool-text" readonly="readonly" value="" />
						&nbsp;<a id="menuBtn" href="#" onclick="showMenu('#department_pid','#department_pid_hidden'); return false;">选择</a></li>
				<li>
					<label><span>* </span>部门代码：</label> 
					<input class="tool-text" type="text" value="${obj.department_id}" name="dptEntity.department_id" id="department_id" />
				</li>
				
				<li>
					<label><span>* </span>部门名称：</label> 
					<input class="tool-text" type="text" value="${obj.department_name}" name="dptEntity.department_name" id="department_name" />
				</li>
				
				<%-- <li>
					<label>部门状态：</label> 
					<select class="tool-select" name="dptEntity.department_stt" id="department_stt">
						<c:forEach items="${K_DPTSTT}" var="rst">
							<option value ="${rst.val}">${rst.prompt}</option>
						</c:forEach>
					</select>
				</li> --%>
				
				<li>
					<label>备注：</label> 
					<input class="tool-text" type="text" value="${obj.department_desc}" name="dptEntity.department_desc" id="department_desc" />				
				</li>
			</ul>
			<div class="tool-buttons">
				<input class="tool-btn" type="submit" value="提  交"></input>&nbsp;
				<input class="tool-btn" type="button" value="重  置" onclick="resetPage()"></input>&nbsp;
				<input class="tool-btn" id="closeLayer" type="button" value="返  回"></input>
			</div>
			
			<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
				<ul id="treeDemo" class="ztree" style="margin-top:0; width:210px;"></ul>
			</div>
		</form>
	</div>
	<script>
		$(document).ready(function() {
			$("#form").validate({
				onfocusout: function(element) { 
					$(element).valid(); 
				},
				rules: {
					"dptEntity.department_id": {
						required: true,
						maxlength: 30
					},
					"dptEntity.department_name": {
						required: true,
						maxlength: 30
					},
					"dptEntity.department_desc": {
						maxlength: 50
					}
				}	
			});
			
			if("${modiFlag}" != ""){ 
				//修改入口
				initSelectLabelValue();
				$("#department_id").attr("readonly","readonly");
				$("#department_id").addClass("hidden-input-border");
				form.action = "departmentAction_modi";
			}else{
				//新增入口
				$("#department_id").removeAttr("readonly");
				form.action = "departmentAction_add";
			}
			//获取当前窗口索引
			var index = parent.layer.getFrameIndex(window.name);
			$("#closeLayer").click(function(){
				parent.layer.close(index);
			})
		})
	
		function initSelectLabelValue(){
			$("#department_level").val('${obj.department_level}');
			$("#department_pid_hidden").val('${obj.department_pid}');
			$("#department_pid").val('${obj.department_pid_name}');
			$("#department_stt").val('${obj.department_stt}');
		}
		
		function resetPage(){
			form.reset();
			if("${modiFlag}" != ""){
				initSelectLabelValue();
			}
		}
		
	</script>
	
</body>
</html>

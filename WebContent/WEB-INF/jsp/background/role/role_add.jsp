<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
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
				<input type="hidden" name="_backUrl" value="roleAction_toQuery"/>
			</c:if>
			${paramCover.unCoveredForbidInputs}
			<ul class="pop-list">
				<li>
					<label><span>* </span>角色编号：</label> 
					<input class="tool-text" type="text" value="${obj.role_id}" name="roleEntity.role_id" id="role_id" />
				</li>
				
				<li>
					<label><span>* </span>角色名称：</label> 
					<input class="tool-text" type="text" value="${obj.role_name}" name="roleEntity.role_name" id="role_name" />
				</li>
				
				<li>
					<label>平台类型：</label> 
					<select class="tool-select" name="roleEntity.platform_type" id="platform_type">
						<c:forEach items="${K_PLATTYPE}" var="rst">
							<option value ="${rst.val}">${rst.prompt}</option>
						</c:forEach>
					</select>
				</li>
				
				<li>
					<label>备注：</label> 
					<input class="tool-text" type="text" value="${obj.role_desc}" name="roleEntity.role_desc" id="role_desc" />				
				</li>
			</ul>
			<div class="tool-buttons">
				<input class="tool-btn" type="submit" value="提  交"></input>&nbsp;
				<input class="tool-btn" type="button" onclick="resetPage()" value="重  置"></input>&nbsp;
				<input class="tool-btn" id="closeLayer" type="button" value="返  回"></input>
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
					"roleEntity.role_id": {
						required: true,
						maxlength: 10
					},
					"roleEntity.role_name": {
						required: true,
						maxlength: 30
					},
					"roleEntity.role_desc": {
						maxlength: 50
					}
				}	
			});
			if("${modiFlag}" != ""){ 
				//修改入口
				initSelectLabelValue();
				$("#role_id").attr("readonly","readonly");
				$("#role_id").addClass("hidden-input-border");
				form.action = "roleAction_modi";
			}else{
				//新增入口
				$("#role_id").removeAttr("readonly");
				form.action = "roleAction_add";
			}
			//获取当前窗口索引
			var index = parent.layer.getFrameIndex(window.name);
			$("#closeLayer").click(function(){
				parent.layer.close(index);
			})
		})
	
		function initSelectLabelValue(){
			$("#platform_type").val('${obj.platform_type}');
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

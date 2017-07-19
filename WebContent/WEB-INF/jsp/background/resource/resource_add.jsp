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
/* span.error {
	top:5px;
	right:68px;
	line-height:25px;
} */
.hidden-input-border {
	border-left:0px;
	border-top:0px;
	border-right:0px;
	border-bottom:1px;
}
</style>
</head>
<body style="text-align:center;">
	<!--显示表单内容-->
	<form action="" method="post" name="form" id="form">
		${paramCover.unCoveredForbidInputs}
		<ul class="main-actions clearfix">
			<li>
				<label class="extra"><span>* </span>权限编号：</label> 
				<input class="tool-text" type="text" value="${obj.privilege_id}" name="resourceEntity.privilege_id" id="privilege_id" />
			</li>
			
			<li>
				<label class="extra"><span>* </span>权限名称：</label>
				<input class="tool-text" type="text" value="${obj.privilege_name}" name="resourceEntity.privilege_name" id="privilege_name" />
			</li>
			
			<li>
				<label class="extra">父权限编号：</label> 
				<input class="tool-text" type="text" value="${obj.parent_id}" name="resourceEntity.parent_id" id="parent_id" />
			</li>
			
			<li>
				<label class="extra">URL：</label> 
				<input class="tool-text" type="text" value="${obj.url}" name="resourceEntity.url" id="url" />
			</li>
			
			<li>
				<label class="extra">权限类型：</label> 
				<select class="tool-select" name="resourceEntity.type" id="type">
					<c:forEach items="${K_RESTYPE}" var="rst">
						<option value="${rst.val}">${rst.prompt}</option>
					</c:forEach>
				</select>
			</li>
			
			<li>
				<label class="extra">权限状态：</label> 
				<select class="tool-select" name="resourceEntity.valid" id="valid">
					<c:forEach items="${K_RESSTT}" var="rst">
						<option value="${rst.val}">${rst.prompt}</option>
					</c:forEach>
				</select>
			</li>
			
			<li>
				<label class="extra">平台类型：</label> 
				<select class="tool-select" name="resourceEntity.platform_type" id="platform_type">
					<c:forEach items="${K_PLATTYPE}" var="rst">
						<option value ="${rst.val}">${rst.prompt}</option>
					</c:forEach>
				</select>
			</li>
			
			<li>
				<label class="extra">顺序：</label> 
				<input class="tool-text" type="text" value="${obj.sort}" name="resourceEntity.sort" id="sort" />
			</li>
			
		</ul>
		<div class="tool-buttons">
			<input class="tool-btn" type="submit" value="提  交"></input>&nbsp; 
			<input class="tool-btn" type="reset" value="重  置"></input>&nbsp; 
			<input class="tool-btn" id="closeWindow" type="button" value="返  回"></input>
		</div>
	</form>
	<script>
		$(document).ready(function() {
			$("#form").validate({
				onfocusout: function(element) { 
					$(element).valid(); 
				},
				rules: {
					"resourceEntity.privilege_id": {
						required: true,
						digits: true,
						maxlength: 10
					},
					"resourceEntity.privilege_name": {
						required: true,
						maxlength: 30
					},
					"resourceEntity.parent_id": {
						digits: true,
						maxlength: 10
					},
					"resourceEntity.url": {
						maxlength: 100
					},
					"resourceEntity.sort": {
						digits: true,
						maxlength: 30
					}
				}	
			});
			if ("${modiFlag}" != "") {
				//修改入口
				initSelectLabelValue();
				$("#privilege_id").attr("readonly", "readonly");
				$("#privilege_id").addClass("hidden-input-border");
				form.action = "resourceAction_modi";
				document.title = "修改权限";
			} else {
				//新增入口
				$("#privilege_id").removeAttr("readonly");
				form.action = "resourceAction_add";
				document.title = "新增权限";
			}
			
			$("#closeWindow").click(function(){
				history.back();
			});
		})

		function initSelectLabelValue() {
			$("#type").val('${obj.type}');
			$("#valid").val('${obj.valid}');
			$("#platform_type").val('${obj.platform_type}');
		}
	</script>
</body>
</html>

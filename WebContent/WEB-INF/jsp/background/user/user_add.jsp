<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
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
	<!--显示表单内容-->
	<form action="" method="post" name="form" id="form">
		<c:if test="${empty param._backUrl}">
			<input type="hidden" name="_backUrl" value="userAction_toQuery" />
		</c:if>
		${paramCover.unCoveredForbidInputs}
		<input type="hidden" name="userEntity.department_id" id="department_id"/>
		<ul class="main-actions clearfix">
			<li>
				<label class="extra"><span>* </span>用户ID：</label> 
				<input class="tool-text" type="text" value="${obj.user_id}" name="userEntity.user_id" id="user_id" />
			</li>
			
			<li>
				<label class="extra"><span>* </span>用户名称：</label>
				<input class="tool-text" type="text" value="${obj.user_name}" name="userEntity.user_name" id="user_name" />
			</li>
			
			<li>
				<label class="extra">所属部门：</label> 
				<input id="department_name" class="tool-text" type="text" readonly="readonly" name="userEntity.department_name" />
				&nbsp;<a id="menuBtn" href="#" onclick="showMenu('#department_name','#department_id'); return false;">选择</a>
			</li>
			
			<li>
				<label class="extra">用户类型：</label> 
				<select class="tool-select" name="userEntity.user_type" id="user_type">
					<c:forEach items="${K_USERTYPE}" var="rst">
						<option value="${rst.val}">${rst.prompt}</option>
					</c:forEach>
				</select>
			</li>
			
			<li>
				<label class="extra">用户状态：</label> 
				<select class="tool-select" name="userEntity.user_state" id="user_state">
					<c:forEach items="${K_USERSTT}" var="rst">
						<option value="${rst.val}">${rst.prompt}</option>
					</c:forEach>
				</select>
			</li>
			
			<%-- <li>
				<label class="extra">电话：</label> 
				<input class="tool-text" type="text" value="${obj.user_phone}" name="userEntity.user_phone" id="user_phone" />
			</li>
			
			<li>
				<label class="extra">手机：</label> 
				<input class="tool-text" type="text" value="${obj.user_mobile}" name="userEntity.user_mobile" id="user_mobile" />
			</li>
			
			<li>
				<label class="extra">邮箱：</label> 
				<input class="tool-text" type="text" value="${obj.user_email}" name="userEntity.user_email" id="user_email" />
			</li>
			
			<li>
				<label class="extra">地址：</label> 
				<input class="tool-text" type="text" value="${obj.user_addr}" name="userEntity.user_addr" id="user_addr" />
			</li>
			
			<li>
				<label class="extra">证件类型：</label> 
				<select class="tool-select" name="userEntity.cert_type" id="cert_type">
					<option value="">不选</option>
					<c:forEach items="${K_CERTTYPE}" var="rst">
						<option value="${rst.val}">${rst.prompt}</option>
					</c:forEach>
				</select>
			</li>
			
			<li>
				<label class="extra">证件号码：</label> 
				<input class="tool-text" type="text" value="${obj.user_certno}" name="userEntity.user_certno" id="user_certno" />
			</li>--%>
			
			<li>
				<label class="extra">备注：</label> 
				<input class="tool-text" type="text" value="${obj.user_desc}" name="userEntity.user_desc" id="user_desc" />
			</li> 
			
		</ul>
		<div class="tool-buttons">
			<input class="tool-btn" type="submit" value="提  交"></input>&nbsp; 
			<input class="tool-btn" type="reset" value="重  置"></input>&nbsp; 
			<input class="tool-btn" id="closeWindow" type="button" value="返  回"></input>
		</div>
		<div id="menuContent" class="menuContent" style="display:none; position: absolute; z-index:10;">
			<ul id="treeDemo" class="ztree" style="margin-top:0; width:200px;"></ul>
		</div>
	</form>
	<script>
		$(document).ready(function() {
			$("#form").validate({
				onfocusout: function(element) { 
					$(element).valid(); 
				},
				rules: {
					"userEntity.user_id": {
						required: true,
						maxlength: 30
					},
					"userEntity.user_name": {
						required: true,
						maxlength: 30
					},
					"userEntity.user_desc": {
						maxlength: 50
					}
				}	
			});
			
			if ("${modiFlag}" != "") {
				//修改入口
				initSelectLabelValue();
				$("#user_id").attr("readonly", "readonly");
				$("#user_id").addClass("hidden-input-border");
				form.action = "userAction_modi";
				document.title = "修改用户";
			} else {
				//新增入口
				$("#user_id").removeAttr("readonly");
				form.action = "userAction_add";
				document.title = "新增用户";
			}
			
			/* $("#closeWindow").click(function(){
				history.back();
			}); */
			
			//获取当前窗口索引
			var index = parent.layer.getFrameIndex(window.name);
			$("#closeWindow").click(function(){
				parent.layer.close(index);
			})
		})

		function initSelectLabelValue() {
			$("#user_type").val('${obj.user_type}');
			$("#department_id").val('${obj.department_id}');
			$("#department_name").val('${obj.department_name}');
			$("#user_state").val('${obj.user_state}');
			//$("#cert_type").val('${obj.cert_type}');
		}
	</script>
</body>
</html>

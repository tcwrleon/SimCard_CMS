<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>密码修改</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
	<script type="text/javascript" src="${ctx}/javascript/jquery/jquery.validate.custom-rules.js"></script>
<style type="text/css">
span.error {
	top:5px;
	right:68px;
	line-height:25px;
}
</style>
</head>
<body style="text-align:center;">
	<div class="window-pop">
		<!--显示表单内容-->
		<form action="userAction_modiPwd" method="post" name="form" id="form">
			<%-- <c:if test="${empty param._backUrl}">
				<input type="hidden" name="_backUrl" value="userAction_toQuery"/>
			</c:if> --%>
			<c:if test="${!empty pwdExpired}">
				<input type="hidden" name="selectedId" value="${selectedId}"/>
				<input type="hidden" name="pwdExpired" value="${pwdExpired}"/>
				<input type="hidden" name="_backUrl" value="loginAction_goToLoginPage"/>
				<div style="text-align:center; padding:15px; color:red; font-size:16px;">您的登录密码已过期，请修改密码！</div>
			</c:if>
			 ${paramCover.unCoveredForbidInputs} 
			<ul class="pop-list">
				<li>
					<label><span>* </span>旧密码：</label> 
					<input class="tool-text" type="password" name="old_password" id="old_password" />				
				</li>
				
				<li>
					<label><span>* </span>新密码：</label> 
					<input class="tool-text" type="password" name="new_password" id="new_password" />				
				</li>
				<li>
					<label><span>* </span>新密码确认：</label> 
					<input class="tool-text" type="password" name="new_password_confirm" id="new_password_confirm" />				
				</li>
			</ul>
			<div class="tool-buttons">
				<input class="tool-btn" type="submit" value="提  交"></input>&nbsp;
				<input class="tool-btn" type="reset" value="重  置"></input>&nbsp;
				<input class="tool-btn" id="closeLayer" type="button" value="返  回"></input>
			</div>
			<p style="text-align:left; color:red;"><label><span>* </span>温馨提示：</label>密码至少包含以下四种字符中的三种：大写字母、小写字母、数字和特殊字符</p>
		</form>
	</div>
	<script>
		$(document).ready(function() {
			$("#form").validate({
				onfocusout: function(element) { 
					$(element).valid(); 
				},
				rules: {
					old_password: {
						required: true,
						rangelength:[8,10],
					},
					new_password: {
						required: true,
						rangelength:[8,10],
						pwdStrength: true,
					},
					new_password_confirm: {
						required: true,
						equalTo: "#new_password" 
					}
				}	 
			});
			
			if("${pwdExpired}" != ""){
				$(".window-pop").css({"margin-top":"100px","padding":"10px 20px 30px","border":"solid 1px #ccc"});
				$("#closeLayer").click(function(){
					history.back();
				});
			}else{
				//获取当前窗口索引(弹出框)
				var index = parent.layer.getFrameIndex(window.name);
				$("#closeLayer").click(function(){
					parent.layer.close(index);
				});
			}
		})
		
		
	</script>
</body>
</html>

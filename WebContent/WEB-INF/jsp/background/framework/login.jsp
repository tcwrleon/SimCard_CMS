<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>登录页面</title>
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
</head>
<body class="over-hid" onload="$('#username').focus()">
	<div id="loginBg" class="login-bg"><img class="login-bg-img" src="${ctx}/images/login-bg.jpg" /></div>
	<form id="homeLogin" action="<c:url value='/j_spring_security_check'/>" method="post">
		<div class="login-main">
			<div class="login-content">
				<%-- <div class="login-face"><img src="${ctx}/images/user-face.png" /></div> --%>
				<h2 class="login-title"><img src="${ctx}/images/login-title.png" /></h2>
				<div class="user-login-info">
					<div class="user-name">
						<label for="username">账　号：</label> 
						<input type="text" name="user_name" id="username" class="user-name-input" />
					</div>
					<div class="user-pwd">
						<label for="userpwd">密　码：</label> 
						<input type="password" name="user_pwd" id="userpwd" class="user-pwd-input" />
					</div>
					
					<c:if test="${not empty error}">
					<div class="loginError">
            			登录失败 :${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
            		</div>
        			</c:if>
				</div>
				<div class="user-login-button">
					<input type="submit" class="user-login" value="登　录" />
					<input type="reset" class="user-relogin" value="重　置" />
				</div>
			</div>
		</div>
	</form>
<script>
	// 在被嵌套时就刷新上级窗口
	if(window.parent != window){
		window.parent.location.reload(true);
	}
</script>
</body>
</html>

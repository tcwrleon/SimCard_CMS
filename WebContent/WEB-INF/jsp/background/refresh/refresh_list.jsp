<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>缓存刷新</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
</head>
<body style="text-align:center;">
	<div class="window-pop">
		<!--显示表单内容-->
		<form action="cacheRefreshAction_doCacheRefresh" method="post" name="form">
			<ul class="pop-list">
				<li style="text-align: center">
					<input type="checkbox" name="refresh" value="dict"/>数据字典&nbsp;&nbsp;&nbsp;
					<input type="checkbox" name="refresh" value="sysParam"/>系统参数&nbsp;&nbsp;&nbsp;
					<input type="checkbox" name="refresh" value="roleResources"/>业务权限		
				</li>
			</ul>
			<div class="tool-buttons">
				<input class="tool-btn" type="button" value="提交刷新" onclick="refreshCache();"></input>&nbsp;
			</div>
		</form>
	</div>
	<script>
		$(document).ready(function() {
			$("input[name = refresh]:checkbox").attr("checked", true);
			$(".window-pop").css({"margin-top":"100px","padding":"10px 20px 30px","border":"solid 1px #ccc"});
		})
		
		function refreshCache(){	
			 var chk_value = [];//定义一个数组    
            $('input[name="refresh"]:checked').each(function(){  
            	chk_value.push($(this).val());
            });
			if(chk_value.length == 0) {
				alert("没有选择任何一项！");
				return;
			}
			form.submit();
		}
	</script>
</body>
</html>

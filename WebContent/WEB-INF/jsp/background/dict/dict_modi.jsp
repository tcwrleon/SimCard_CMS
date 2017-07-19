<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
<style type="">
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
		<form action="" method="post" name="form" onsubmit="initMethod();" id="form">
			<c:if test="${empty param._backUrl}">
				<input type="hidden" name="_backUrl" value="dictAction_toQuery"/>
			</c:if>
			<input type="hidden" name="val_str" id="val_str"/>
			<input type="hidden" name="dict_desc_str" id="dict_desc_str"/>
			${paramCover.unCoveredForbidInputs}
			<ul class="pop-list" id="itemList">
				<li style="">
					<label>主键：</label> 
					<input name="dictMapEntity.sun_key" id="sun_key" type="text" value="${dictmap.sun_key}" class="required tool-text" readonly="readonly" />
				</li>
				
				<li>
					<label><span>* </span>名称：</label>
					<input name="dictMapEntity.prompt" type="text" value="${dictmap.prompt}" class="required tool-text" maxlength="50"/>
				</li>
				
				<c:forEach items="${dicts}" var="dict">
					<li>
						<label>值：</label> 
						<input name="val" type="text" value="${dict.val}" class="tool-text required digits" maxlength="2" readonly="readonly"/>
					</li>
				
				 	<li>
						<label><span>* </span>说明：</label> 
						<input name="dict_desc" type="text" value="${dict.prompt}" class="tool-text required" maxlength="30"/>
					</li> 
				</c:forEach>
				
			</ul>
			<div class="tool-buttons">
				<input class="tool-btn" type="submit" value="提  交"></input>&nbsp;
				<input class="tool-btn" type="reset" value="重  置"></input>&nbsp;
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
			});
			
			$("#sun_key").addClass("hidden-input-border");
			
			$("input[name='val']").each(function(index){
				$(this).addClass("hidden-input-border");
			});
			
			//获取当前窗口索引
			var index = parent.layer.getFrameIndex(window.name);
			$("#closeLayer").click(function(){
				parent.layer.close(index);
			})
		})
		
		function initMethod(){
			document.getElementById("val_str").value = getInputName("val");
			document.getElementById("dict_desc_str").value = getInputName("dict_desc");
			form.action = "dictAction_modi";
		}
		
		function initSelectLabelValue(){
		}
		
		function getInputName(name){
			var str = "";
			var els =document.getElementsByName(name);
			for (var i = 0, j = els.length; i < j; i++){
				str = str + els[i].value + ",";
			}
			
			if (str != ""){
				str = str.substr(0, str.length-1);
			}
			
			return str;
		}
	</script>
</body>
</html>

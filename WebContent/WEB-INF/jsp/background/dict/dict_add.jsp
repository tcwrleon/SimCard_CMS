<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
<style>
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
		<form action="" method="post" name="form" onsubmit="initMethod();" id="form">
			<c:if test="${empty param._backUrl}">
				<input type="hidden" name="_backUrl" value="dictAction_toQuery"/>
			</c:if>
			<input type="hidden" name="val_str" id="val_str"/>
			<input type="hidden" name="dict_desc_str" id="dict_desc_str"/>
			${paramCover.unCoveredForbidInputs}
			<ul class="pop-list">
				<li>
					<label><span>* </span>名称：</label>
					<input name="dictMapEntity.prompt" type="text" class="tool-text"/>
				</li>
				
				<li>
					<label><span>* </span>主键：</label> 
					<input name="dictMapEntity.sun_key" type="text" class="tool-text" />
				</li>
				
				<li>
					<label class="extra" style="text-align:left"><input type="button" value=" + " onclick="newOne();" /></label>
				</li>
			</ul>
			<ul class="pop-list" id="itemList">
				<li>
					<label><span>* </span>值：</label> 
					<input name="val1" type="text" class="tool-text required digits" maxlength="2"/>
				</li>
				
				 <li>
					<label><span>* </span>说明：</label> 
					<input name="dict_desc1" type="text" class="tool-text required" maxlength="30"/>
				</li> 
				
<%--				<li>
					<label><span>* </span>值：</label> 
					<input name="val2" type="text" class="tool-text required digits" maxlength="2"/>
				</li>
				
				<li>
					<label><span>* </span>说明：</label> 
					<input name="dict_desc2" type="text" class="tool-text required" maxlength="30"/>
				</li>--%>
			</ul>
			<ul class="pop-list">	
				<li style="text-align:right">
					<input type="button" value=" - " onclick="deleteOne();"/>
				</li>
				
			</ul>
			<div class="tool-buttons">
				<input class="tool-btn" type="submit" value="提  交"></input>&nbsp;
				<input class="tool-btn" type="reset" value="重  置"></input>&nbsp;
				<input class="tool-btn" id="closeLayer" type="button" value="返  回"></input>
			</div>
		</form>
	</div>
	<script>
		var init_ul_len;
		var dict_num;
		$(document).ready(function() {
			init_ul_len = $("#itemList li").length;
			dict_num = init_ul_len/2;
			$("#form").validate({
				onfocusout: function(element) { 
					$(element).valid(); 
				},
				rules: {
					"dictMapEntity.prompt": {
						required: true,
						maxlength: 50
					},
					"dictMapEntity.sun_key": {
						required: true,
						maxlength: 30
					}
				}	
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
			form.action = "dictAction_add";
		}
		
		
		function getInputName(name){
			var str = "";
			for (var i = 1; i <= dict_num; i++){
				var els =document.getElementsByName(name+i);
				str = str + els[0].value + ",";
			}
			
			if (str != ""){
				str = str.substr(0, str.length-1);
			}
			
			return str;
		}
		
		function newOne(){
			dict_num = dict_num + 1;
			var html = '<li><label><span>* </span>值：</label>' + 
					   ' <input name="val'+ dict_num + '" type="text" class="tool-text required digits" maxlength="2"/></li>' +
					   '<li><label><span>* </span>说明：</label>' + 
					   ' <input name="dict_desc'+ dict_num + '" type="text" class="tool-text required" maxlength="30"/></li>';
			$("#itemList").append(html)
		}
		
		function deleteOne(){
			var cur_ul_len = $("#itemList li").length;
			if(cur_ul_len > init_ul_len){
				dict_num = dict_num - 1
				$("#itemList li:last").remove();
				$("#itemList li:last").remove();
			}else{
				alert("已经是初始化页面，无法删除！");
			}
		}
	</script>
</body>
</html>

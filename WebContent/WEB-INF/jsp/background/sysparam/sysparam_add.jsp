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
				<input type="hidden" name="_backUrl" value="sysParamAction_toQuery"/>
			</c:if>
			${paramCover.unCoveredForbidInputs}
			<ul class="pop-list">
				<li>
					<label>参数类别：</label> 
					<select class="tool-select" name="sysParamEntity.param_type" id="param_type" onchange="showHiddenInput()">
						<c:forEach items="${K_PARAMTYPE}" var="rst">
							<option value ="${rst.val}">${rst.prompt}</option>
						</c:forEach>
					</select>
				</li>
				
				<li id="show_prod_type">
					<label>产品类型：</label> 
					<select class="tool-select" name="sysParamEntity.prod_type" id="prod_type" >
						<c:forEach items="${K_PRODTYPE}" var="rst">
							<option value ="${rst.val}">${rst.prompt}</option>
						</c:forEach>
					</select>
				</li>

				<li id="show_prod_id">
					<label><span>* </span>产品代码：</label> 
					<input class="tool-text" type="text" maxlength="30" value="${obj.prod_id}" name="sysParamEntity.prod_id" id="prod_id" />
				</li>
				
				<li>
					<label><span>* </span>参数ID：</label> 
					<input class="tool-text" type="text" maxlength="15" value="${obj.param_code}" name="sysParamEntity.param_code" id="param_code" />				
				</li>
				
				<li>
					<label><span>* </span>参数名称：</label> 
					<input class="tool-text" type="text" maxlength="30" value="${obj.param_name}" name="sysParamEntity.param_name" id="param_name" />				
				</li>
				
				<li>
					<label><span>* </span>值：</label> 
					<input class="tool-text" type="text" maxlength="15" value="${obj.param_value}" name="sysParamEntity.param_value" id="param_value" />				
				</li>
				
				<li>
					<label>说明：</label> 
					<input class="tool-text" type="text" maxlength="50" value="${obj.param_desc}" name="sysParamEntity.param_desc" id="param_desc" />				
				</li>
			</ul>
			<div class="tool-buttons">
				<input class="tool-btn" type="submit" value="提  交"></input>&nbsp;
				<input class="tool-btn" type="button" value="重  置" onclick="resetPage()"></input>&nbsp;
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
					"sysParamEntity.prod_id": {
						required: true,
						maxlength: 30
					},
					"sysParamEntity.param_code": {
						required: true,
						maxlength: 15
					},
					"sysParamEntity.param_name": {
						required: true,
						maxlength: 30
					},
					"sysParamEntity.param_value": {
						required: true,
						maxlength: 15
					},
					"sysParamEntity.param_desc": {
						maxlength: 50
					}
				}	
			});
			
			if("${modiFlag}" != ""){ 
				//修改入口
				initSelectLabelValue();
				$("#param_code").attr("readonly","readonly");
				$("#param_code").addClass("hidden-input-border");
				form.action = "sysParamAction_modi";
			}else{
				//新增入口
				$("#param_code").removeAttr("readonly");
				form.action = "sysParamAction_add";
			}
			showHiddenInput();
			//获取当前窗口索引
			var index = parent.layer.getFrameIndex(window.name);
			$("#closeLayer").click(function(){
				parent.layer.close(index);
			});
			
		})
	
		function initSelectLabelValue(){
			$("#param_type").val('${obj.param_type}');
			$("#prod_type").val('${obj.prod_type}');
		}
		
		function showHiddenInput(){
			var checkValue = $("#param_type").val();
			if(checkValue == '0'){
				$("#show_prod_type").hide();
				$("#show_prod_id").hide();
			}else if(checkValue == '1') {
				$("#show_prod_type").show();
				$("#show_prod_id").hide();
			}else if(checkValue == '2'){
				$("#show_prod_type").hide();
				$("#show_prod_id").show();
			}else{
				$("#show_prod_type").hide();
				$("#show_prod_id").hide();
			}
		}
		
		function resetPage(){
			form.reset();
			if("${modiFlag}" != ""){
				initSelectLabelValue();
			}
			showHiddenInput();
		}
	</script>
</body>
</html>

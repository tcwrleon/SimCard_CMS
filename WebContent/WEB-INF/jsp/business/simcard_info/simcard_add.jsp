<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>sim卡信息新增</title>
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
	<link type="text/css" href="${ctx}/style/base/jquery.ui.all.css" rel="stylesheet" />
 	<script src="${ctx}/javascript/jquery/jquery-ui.min.js"></script>  
	<script type="text/javascript" src="${ctx}/javascript/ui/jquery.ui.autocomplete.js"></script>
	<script type="text/javascript" src="${ctx}/javascript/city/jquery.cityselect.js"></script>
	<style>
	.main-actions li label.extra{
		width:125px;
	}
	.tool-textarea {
		width: 220px;
		height: 100px;
		line-height: 23px;
		border-style: solid;
		border-width: 1px;
		border-color: #d4d4d4;
		border-radius: 4px;
		margin: 0 auto;
		vertical-align: middle;
	}
	</style>
</head>
	<body>
	<div class="main-title" id="Title"><!--页面标题-->
        sim卡信息编辑
    </div>
	<form id="form" name="form" method="post">
		<c:if test="${empty param._backUrl}">
			<input type="hidden" name="_backUrl" value="simCardAction_toQuery"/>
		</c:if>
		${paramCover.unCoveredInputs}
		<ul class="main-actions clearfix" id="itemList">
			<li id="li_plat_no">
				<label class="extra"><span>* </span>sim卡编号：</label>
				<input name="simCardEntity.sim_id" id="sim_id" type="text" class="tool-text" value="${simCardInfo.sim_id}"/>
			</li>
			<li>
				<label class="extra"><span>* </span>手机号：</label>
				<input name="simCardEntity.mobile" id="mobile" type="text" class="tool-text" value="${simCardInfo.mobile}"/>
			</li>
			<li>
				<label class="extra">姓名：</label>
				<input name="simCardEntity.user_name" id="user_name" type="text" class="tool-text" value="${simCardInfo.user_name}"/>
			</li>
            <li>
                <label class="extra">身份证：</label>
                <input name="simCardEntity.id_card" id="id_card" type="text" class="tool-text" value="${simCardInfo.id_card}"/>
            </li>

            <li>
                <label class="extra">服务密码：</label>
                <input name="simCardEntity.service_pwd" id="service_pwd" type="text" class="tool-text" value="${simCardInfo.service_pwd}"/>
            </li>

			<li>
				<label class="extra"><span>* </span>运营商：</label>
				<select class="tool-select" name="simCardEntity.operators" id="operators" >
					<c:forEach items="${SIM_OPERATORS}" var="rst">
						<option value="${rst.val}">${rst.prompt}</option>
					</c:forEach>
				</select>
			</li>
			<li>
				<label class="extra"><span>* </span>省份：</label>
				<input name="simCardEntity.province" id="province" type="text" class="tool-text" value="${simCardInfo.province}"/>
			</li>

			<li>
				<label class="extra">城市：</label>
				<input name="simCardEntity.city" id="city" type="text" class="tool-text" value="${simCardInfo.city}"/>
			</li>

            <li>
                <label class="extra">开发人员：</label>
                <select class="tool-select" name="simCardEntity.author" id="author" >
                    <option value="">暂无</option>
                    <c:forEach items="${SIM_AUTHOR}" var="rst">
                        <option value="${rst.val}">${rst.prompt}</option>
                    </c:forEach>
                </select>
            </li>

            <li>
                <label class="extra"><span>*</span>最后动作者：</label>
                <select class="tool-select" name="simCardEntity.final_at" id="final_at" >
                    <c:forEach items="${SIM_FINAL_AT}" var="rst">
                        <option value="${rst.val}">${rst.prompt}</option>
                    </c:forEach>
                </select>
            </li>

            <li>
                <label class="extra">最后动作：</label>
                <select class="tool-select" name="simCardEntity.final_action" id="final_action" >
                    <c:forEach items="${SIM_FINAL_ACTION}" var="rst">
                        <option value="${rst.val}">${rst.prompt}</option>
                    </c:forEach>
                </select>
            </li>

            <div id="update_time">
            <li>
                <label class="extra">最后动作时间：</label>
                <input name="simCardEntity.final_update_time" id="final_update_time" type="text" class="tool-text" value="${simCardInfo.final_update_time}"/>
            </li>
            </div>

			<li style="padding-top:6px">
				<label class="extra">备注：</label>
				<textarea class="tool-textarea" cols="1" rows="1" name="simCardEntity.remark" id="remark">${simCardInfo.remark}</textarea>
			</li>
		</ul>

		<div class="tool-buttons">
			<input class="tool-btn" type="submit" value="保存" id="submit"/>&nbsp;
			<input class="tool-btn" type="reset"  value="重 置" id="reset" />&nbsp;
			<input class="tool-btn" type="button" value="返 回" id="back" onclick="history.back();" />
		</div>	
	</form>	
	<script type="text/javascript" >
		$(document).ready(function() {
			if("${flag}" == "modi"){ 
				//修改入口
 				initSelectLabelValue();
				$("#final_update_time").attr("readonly","readonly");
				form.action = "simCardAction_modi";
			}else if("${flag}" == "look"){
                initSelectLabelValue();
				$('input',$('form[name="form"]')).attr('readonly',true);//设置表单里的input控件只读
                $("#operators").attr("disabled",true);
				$("#author").attr("disabled",true);
				$("#final_at").attr("disabled",true);
				$("#final_action").attr("disabled",true);
				$("#submit").hide();
				$("#reset").hide();
			}else{
				//新增入口
				//$("#plat_state option[value='1']").remove();可用
				//$("#plat_state option[text='正常']").remove();不可用
				//$("#plat_state option[text='停用']").attr("selected", true);不可用
                $("#update_time").hide();
				form.action = "simCardAction_add";
			}

            //增加表单的验证
            $("#form").validate({
                onfocusout: function (element) {
                    $(element).valid();
                },
                rules: {
                    "simCardEntity.sim_id": {
                        required: true
                    },
                    "simCardEntity.operators": {
                        required: true
                    },
                    "simCardEntity.province": {
                        required: true
                    },
                    "simCardEntity.final_at": {
                        required: true
                    }
                }
            });


		});
		
		function initSelectLabelValue() {
			$("#operators").val("${simCardInfo.operators}");
			$("#author").val("${simCardInfo.author}");
			$("#final_at").val("${simCardInfo.final_at}");
			$("#final_action").val("${simCardInfo.final_action}");
			//setTimeout(function(){$("#plat_address option[value='${platInfo.plat_address}']").attr("selected",true);},90);//初始化
		}

		//去掉字符串头尾空格   
		function trim(str) {   
			return str.replace(/(^\s*)|(\s*$)/g, "");   
		}		   
	</script>
</body>
</html>

/**
 * 查询页面共用方法
 * @author mumu
 *
 */

var _checkedLists = "";//获取查询页面所有name为item的input标签
var isPopupWindow = false;//是否为弹出页面
var _queryUrl = "";//查询url
var _iframeName = "";//主页iframe名称
/*提示信息*/
var msgNull = "请选择要{0}的{1}";
var msgMore = "每次只能选择一个{0}进行{1}";
var msgConfirm = "确定要{0}所有选中{1}吗？";

/*扩展String方法*/
String.prototype.format = function()  
{  
	if(arguments.length == 0) 
		return this;  
	for(var s = this, i = 0; i < arguments.length; i++)  
		s = s.replace(new RegExp("\\{"+i+"\\}","g"), arguments[i]); 
	
	return s;  
};

/**
 * 初始化全局变量
 * @param list
 * @param queryUrl
 */
function initGlobalVar(list,queryUrl,iframeName){
	_checkedLists = list;
	_queryUrl = queryUrl;
	_iframeName = iframeName
}

/**
 * 选中项样式变化
 */
function selectedCssChange(){
	for(var i = 0; i < _checkedLists.length; i++) {
		_checkedLists.eq(i).click(function(){ //选中项样式变化
			if($(this).is(":checked")){
				$(this).parent().parent().addClass("checkedTr");	
			}else{
				$(this).parent().parent().removeClass("checkedTr");
			}
		});
	}
}

/**
 * checkBox全选
 */
function checkedAll(){
	for(var i = 0; i < _checkedLists.length; i++){
		if($('#selectAll').is(":checked")){
			_checkedLists.eq(i).prop('checked',true);
			_checkedLists.eq(i).parent().parent().addClass('checkedTr');
		}else{
			_checkedLists.eq(i).prop('checked',false);
			_checkedLists.eq(i).parent().parent().removeClass('checkedTr');
		}
	}
}

/**
 * 操作按钮form提交
 * @param action url
 * @param value 选中的值
 */
function submitDetailForm(action,value){
	detailForm.action = action;
	detailForm.selectedId.value = value;
	detailForm.submit();
}

/**
 * Ajax方式提交（且跳转后页面是弹出页面）
 * @param url
 * @param selectedId
 * @param title 跳转后页面的标题
 */
function showLayerPage(url,selectedId,title){
	url = url + "?selectedId=" + selectedId;
	$.layer({
        type: 2,
        title: title,
        maxmin: false,
        area : ['450px' , '500px'],
        offset : ['', ''],
        iframe: {src: url},
        close: function(index){
        	parent.frames[_iframeName].location.href=_queryUrl;
        }
    });
}

/**
 * checkbox选中项值拼接
 * @returns {String}
 */
function getSelectedIdStr(){
	var idstr = "";
	for (var i = 0; i < _checkedLists.length; i++) {
		if(_checkedLists[i].checked) {
			idstr = idstr + _checkedLists[i].value + ",";
		}
	}
	
	if (idstr != ""){
		idstr = idstr.substr(0, idstr.length-1);
	}
	return idstr;
}

/**
 * 是否选中值
 * @param value
 * @param tipMsg1
 * @param tipMsg2
 * @returns {Boolean}
 */
function selectedIsNull(value,tipMsg1,tipMsg2){
	if(value == ""){
		alert(msgNull.format(tipMsg1,tipMsg2));
		return true;
	}
	return false;
}

/**
 * 只能单选判断
 * @param value
 * @param tipMsg1
 * @param tipMsg2
 * @returns {Boolean}
 */
function selectedOne(value,tipMsg1,tipMsg2){
	if(value.indexOf(",") > -1){
		alert(msgMore.format(tipMsg2,tipMsg1));
		return true;
	}
	return false;
}

/**
 * 单选跳转
 * @param tipMsg1
 * @param tipMsg2
 * @param url
 * @returns {Boolean}
 */
function singleSelection(tipMsg1,tipMsg2,url){
	var selectedId = getSelectedIdStr();
	if(selectedIsNull(selectedId,tipMsg1,tipMsg2)) return false;
	if(selectedOne(selectedId,tipMsg1,tipMsg2)) return false;
	if(isPopupWindow){
		isPopupWindow = false;
		showLayerPage(url,selectedId,tipMsg2+tipMsg1);
	}else{
		submitDetailForm(url,selectedId);
	}
	return true;
}

/**
 * 多选
 * @param tipMsg1
 * @param tipMsg2
 * @param url
 * @returns {Boolean}
 */
function multSelection(tipMsg1,tipMsg2,url){
	var selectedId = getSelectedIdStr();
	if(selectedIsNull(selectedId,tipMsg1,tipMsg2)) return false;
	
	if(confirm(msgConfirm.format(tipMsg1,tipMsg2))) {
		submitDetailForm(url,selectedId);
	}
	return true;
}



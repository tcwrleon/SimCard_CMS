/**
 * 自定义jquery.validate rules
 */

jQuery.validator.addMethod("pwdStrength", function(value, element){       
    		return this.optional(element) || passwordStrength(value);       
		}, "密码至少包含三种字符");

function passwordStrength(sValue){
	 var modes = 0;
	 //正则表达式验证符合要求的
	 if (/\d/.test(sValue)) modes++; //数字
	 if (/[a-z]/.test(sValue)) modes++; //小写
	 if (/[A-Z]/.test(sValue)) modes++; //大写  
	 if (/\W/.test(sValue)) modes++; //特殊字符
	 if(modes >= 3){
		 return true;
	 }else{
		 return false;
	 } 
}


jQuery.validator.addMethod("ismobile", function(value, element) {  
    var length = value.length;     
    var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|18[0-9]{1})+\d{8})$/;     
    return (length == 11 && mobile.exec(value))? true:false;  
}, "请正确填写您的手机号码");  
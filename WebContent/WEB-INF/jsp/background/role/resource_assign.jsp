<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>权限分配</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="../../common/jslibs.jsp"%>
	<%@ include file="../../common/csslibs.jsp"%>
	<script type="text/javascript" src="${ctx}/javascript/ligerUI/base.js"></script>
	<script type="text/javascript" src="${ctx}/javascript/ligerUI/ligerTree.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctx}/javascript/ligerUI/ligerui-tree.css" />
</head>
<body>   
    <div style="margin:10px;width:90%; height:90%;  float:left; border:1px solid #ccc; overflow:auto;" >
    <ul id="tree1"></ul>
    </div> 
 
    <div align="center">
    <input type="button" name="btnSave" class="tool-btn" value="保 存" onclick="getfun();"/>　　
    <input id="backBt" type="button" class="tool-btn"  value="关 闭"  />
    </div>
    <form action="roleAction_resAssign" name="form">
    	<input type="hidden" name="checkedId" value=""/>
    	<c:if test="${empty param._backUrl}">
			<input type="hidden" name="_backUrl" value="roleAction_toQuery"/>
		</c:if>
		${paramCover.unCoveredForbidInputs}
    </form>
    <script type="text/javascript">
      	var tree;
        var manager=null;
        
        $(function ()
        {
            ${resources} //初始化data变量
            
            tree=$("#tree1").ligerTree({ //使数据配置为树结构
            	data:data, 
                textFieldName: 'fname', 
                attribute: ['fid', 'fname', 'pfid'],  
                idFieldName :'fid',
                parentIDFieldName :'pfid',
                checkbox: true,
                onSelect: onSelect

            });
            manager = $("#tree1").ligerGetTreeManager();
            
            var index = parent.layer.getFrameIndex(window.name);
			$("#backBt").click(function(){
				parent.layer.close(index);
			})
        });
        
        function onSelect(){};
        
        function getfun() 
        {
 			var fids="";
 			var notes = manager.getChecked();//获取已选中的权限编号
             for (var i = 0; i < notes.length; i++)
             {
             	fids +=notes[i].data.fid+",";
             	if(notes[i].data.pfid != "" && notes[i].data.pfid != "null"){
             		fids +=notes[i].data.pfid+",";//孩子被选中，父必被选中
             	};
             }
             if(fids == ""){
                 alert("还没有分配权限！！");
                 return;
             }
            //pfid: 'null'

             
             form.checkedId.value= fids;
             form.submit();  
        }
       
    </script>
</body>

</html>

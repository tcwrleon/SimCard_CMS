<script type="text/javascript" src="${ctx}/javascript/jquery/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${ctx}/javascript/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/javascript/jquery/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/javascript/jquery/jquery.metadata.js"></script>
<script type="text/javascript" src="${ctx}/javascript/jquery/messages_cn.js"></script>
<script type="text/javascript" src="${ctx}/javascript/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/javascript/layer/layer.min.js"></script>
<%-- <script type="text/javascript" src="${ctx}/javascript/dtree/dtree.js"></script> --%>
<script>
$(function(){
	function tab(obj,curNav,curContent){
		var tab = $("#"+obj),
		tabNav = tab.find(".tab-ui-nav"),
		tabCont = tab.find(".tab-ui-content-list");
		tabNav.on("click","li",function(){
			var index = $(this).index();
			$(this).siblings().removeClass(curNav).end().addClass(curNav);
			tabCont.eq(index).addClass(curContent).siblings().removeClass(curContent);
		});
	}
	tab("tab-ui","curTabNav","curTab");
});
</script>



<link rel="stylesheet" href="${ctx}/javascript/zTree_v3/css/demo.css" type="text/css">
<link rel="stylesheet" href="${ctx}/javascript/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${ctx}/javascript/zTree_v3/js/jquery.ztree.core-3.5.js"></script>

<script type="text/javascript">
		var _showId = "";
		var _showName = "";
		var _queryFlag = "";
		var setting = {
			view: {
				dblClickExpand: true,
				selectedMulti: false
			},
			
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: onClick
			}
		};
		${departmentTree};
		
		function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			nodes = zTree.getSelectedNodes();
			var v = "";
			var _pid= "";
			nodes.sort(function compare(a,b){return a.id-b.id;});
			for (var i=0, l=nodes.length; i<l; i++) {
				v += nodes[i].name + ",";
				_pid += nodes[i].id + ","
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			if (_pid.length > 0 ) _pid = _pid.substring(0, _pid.length-1);
			if(_queryFlag == "true"){
				$(_showId).val(_pid);
				$(_showName).val(v);
			}else{
				$(_showId).val(v);
				$(_showName).val(_pid);	
			}
			hideMenu();
		}

		function showMenu(showId,showName,queryFlag) {
			_showId = showId;
		    _showName = showName;
		    _queryFlag = queryFlag;
			var cityObj = $(_showId);
			var cityOffset = $(_showId).offset();
			$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

			$("body").bind("mousedown", onBodyDown);
		}
		function hideMenu() {
			$("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
		function onBodyDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}

		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		});
		
	</script>

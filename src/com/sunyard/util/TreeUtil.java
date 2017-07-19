package com.sunyard.util;

import java.util.List;

import com.sunyard.entity.system.DepartmentEntity;
import com.sunyard.entity.system.ResourceEntity;


public class TreeUtil {
	
	/**
	 * 给dTree准备数据
	 * 菜单树
	 * @param childrenPrivileges
	 * @return
	 */
	public static String showLeftTree(List<ResourceEntity> childrenPrivileges, String parent_id) throws Exception {

		String childrenList = "d.add(" + parent_id + ",-1,'管理后台');";

		// 循环所有子权限，将子权限格式改为 d.add(1,0,'Node','example.html');
		for (ResourceEntity children : childrenPrivileges) {
			String id = children.getPrivilege_id();
			String parentId = children.getParent_id();
			String name = children.getPrivilege_name();
			String url = children.getUrl();

			if (StringUtil.isEmpty(url)) {
				url = "''";
			} else {
				url = "'" + url + "?menuId=" + id + "'";
			}
			childrenList += " d.add(" + id + "," + parentId + "," + "'" + name + "'" + "," + url + "); ";
		}
		return childrenList;
	}
	
	//机构树
	public static String showDepartmentTree(List<DepartmentEntity> treeList){
		StringBuffer childrenList = new StringBuffer();
		childrenList.append("var zNodes =[");
		for (DepartmentEntity item : treeList) {
			String id = item.getDepartment_id();
			String pid = item.getDepartment_pid();
			String name = item.getDepartment_name();
			childrenList.append("{id:'");
			childrenList.append(id);
			childrenList.append("',pId:'");
			if(StringUtil.isEmpty(pid)){
				pid = "0";
			}
			childrenList.append(pid);
			childrenList.append("',open:true,name:'");
			childrenList.append(name);
			childrenList.append("'},");
		}
		childrenList.append("];");
		return childrenList.toString();
	}
}

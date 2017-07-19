package com.sunyard.entity.system;

public class RoleEntity {
	private String role_id;
	private String role_name;
	private String role_desc;
	private String platform_type;
	
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public String getRole_desc() {
		return role_desc;
	}
	public void setRole_desc(String role_desc) {
		this.role_desc = role_desc;
	}
	public String getPlatform_type() {
		return platform_type;
	}
	public void setPlatform_type(String platform_type) {
		this.platform_type = platform_type;
	}
	
	@Override
	public int hashCode() {
		return role_id.hashCode() + role_name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RoleEntity){
			RoleEntity role = (RoleEntity) obj;
			return role_id.equals(role.role_id) && role_name.equals(role.role_name);
		}
		return super.equals(obj);
	}
	@Override
	public String toString() {
		return role_id + "||" + role_name;
	}
	
}


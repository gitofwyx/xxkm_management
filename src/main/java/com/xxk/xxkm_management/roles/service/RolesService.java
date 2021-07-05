package com.xxk.xxkm_management.roles.service;

import java.util.List;

import com.xxk.xxkm_management.roles.entity.Roles;


public interface RolesService {

	boolean addUserRole(Roles role);
	
	List<String> getUserRoles();
	
	String getUserRoleId(String roleVal);
}

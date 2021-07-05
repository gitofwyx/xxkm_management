package com.kelan.riding.roles.service;

import java.util.List;

import com.kelan.riding.roles.entity.Roles;


public interface RolesService {

	boolean addUserRole(Roles role);
	
	List<String> getUserRoles();
	
	String getUserRoleId(String roleVal);
}

package com.xxkm.management.roles.service;

import java.util.List;

import com.xxkm.management.roles.entity.Roles;


public interface RolesService {

	boolean addUserRole(Roles role);
	
	List<String> getUserRoles();
	
	String getUserRoleId(String roleVal);
}

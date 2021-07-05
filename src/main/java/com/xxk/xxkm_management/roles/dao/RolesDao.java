package com.xxk.xxkm_management.roles.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.xxk.xxkm_management.roles.entity.Roles;


@Repository
public interface RolesDao {

	int addUserRole(Roles role);
	
	List<String> getUserRoles();
	
	String getUserRoleId(String roleVal);
}

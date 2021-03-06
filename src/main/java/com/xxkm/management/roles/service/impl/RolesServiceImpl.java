package com.xxkm.management.roles.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxkm.management.roles.dao.RolesDao;
import com.xxkm.management.roles.entity.Roles;
import com.xxkm.management.roles.service.RolesService;


@Service
public class RolesServiceImpl implements RolesService{
	
	@Autowired
	RolesDao dao;

	@Override
	public boolean addUserRole(Roles role) {
		return dao.addUserRole(role)==1?true:false;
	}

	@Override
	public List<String> getUserRoles() {
		return dao.getUserRoles();
		
	}

	@Override
	public String getUserRoleId(String roleVal) {
		return dao.getUserRoleId(roleVal);
	}
		
}

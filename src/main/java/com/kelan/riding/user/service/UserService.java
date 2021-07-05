package com.kelan.riding.user.service;

import com.kelan.riding.user.entity.User;

public interface UserService {
	
	User getUserById(String id);

	User getUserByAccount(String principal);

	String getRoleByAccount(String account);

	boolean addUser(User user);
	
	boolean updateUser(User user);
	
	boolean updatePassword(String id,String password);
	
	String gatAccountByPhone(String phone);
	
	boolean checkUserUnique(String account,String phone);
	
	boolean forgetPassword(String phone,String password);
}

package com.kelan.riding.user.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kelan.riding.user.entity.User;

@Repository
public interface UserDao {
	//根据id获取用户信息
	User getUserById(String id);
	
	//根据账号获取用户信息
	User getUserByAccount(String account);
	
	//根据账号获取角色信息
	String getRoleByAccount(String account);
	
	//添加用户信息
	int addUser(User user);
	
	//修改用户信息
	int updateUser(User user);
	
	//修改用户密码
	int updatePassword(String id,String password);
	
	//根据手机号获取登录名
	String gatAccountByPhone(String phone);
	
	//检查账号和手机号的唯一性
	List<String> checkUserUnique(String account,String phone);
	
	//用户忘记密码操作
	int forgetPassword(String phone,String password);
	
}

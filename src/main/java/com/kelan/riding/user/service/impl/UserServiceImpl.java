package com.kelan.riding.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kelan.riding.user.dao.UserDao;
import com.kelan.riding.user.entity.User;
import com.kelan.riding.user.service.UserService;
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao dao;
	
	//<!--根据id获取用户信息 -->
	@Override
	public User getUserById(String id) {
		return dao.getUserById(id);
	}
	
	//<!--根据账号获取用户信息 -->
	@Override
	public User getUserByAccount(String account) {
		return dao.getUserByAccount(account);
	}

	//<!--根据账号获取角色信息 -->
	@Override
	public String getRoleByAccount(String account) {
		return dao.getRoleByAccount(account);
	}

	//<!--添加用户信息 -->
	@Override
	public boolean addUser(User user) {
		return dao.addUser(user)==1?true:false;
	}

	//<!--修改用户信息 -->
	@Override
	public boolean updateUser(User user) {
		return dao.updateUser(user)==1?true:false;
	}
	
	//修改用户密码
	@Override
	public boolean updatePassword(String id, String password) {
		return dao.updatePassword(id, password)==1?true:false;
	}

	//<!--根据手机获取账号信息 -->
	@Override
	public String gatAccountByPhone(String phone) {
		return dao.gatAccountByPhone(phone);
	}
	
	//<!--检验手机号账号唯一性 -->
	@Override
	public boolean checkUserUnique(String account, String phone) {
		List<String> result=dao.checkUserUnique(account, phone);
		//查询结果为空时说明账号唯一，返回true，不为空返回false。
		if(result.size()==0){
			return true;
		}
		return false;
	}
	
	//用户忘记密码操作
	@Override
	public boolean forgetPassword(String phone, String password) {
		return dao.forgetPassword(phone, password)==1?true:false;
	}

}

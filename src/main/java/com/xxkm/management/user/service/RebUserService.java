package com.xxkm.management.user.service;

import com.xxkm.management.user.entity.RegUser;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
public interface RebUserService {

    public boolean login(String account, String password);

    public List<RegUser> listRegUser(int pageStart, int pageSize);

    public  List<Map<String, Object>> listRegUserByIds(List<String> listStr);

    public boolean addRegUser(RegUser user);

    public RegUser getRegUser(String id);

    public RegUser getUserByAccount(String account);

    //根据账号获取角色信息
    public String getRoleByAccount(String account);

    public boolean updateRegUser(RegUser user);

    public int countRegUser();

    public boolean deleteListRegUser(List<String> listStr);

    public Boolean addTest(RegUser user);

}

package com.xxkm.management.system.bar_code.dao;

import com.xxkm.management.system.bar_code.entity.Bar_code;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Repository
public interface Bar_codeDao {

    public List<Bar_code> listDevice(int pageStart, int pageSize);

    public int countDevice();

    public List<Bar_code> listDeviceById(List<String> listDevId);

    public int addDevice(Bar_code device);

    public Bar_code getDeviceById(String deviceId);

    public List<Map<String, Object>> getDeviceNumber(String deviceId);

    public List<Map<String, Object>> getDeviceSelect();

    public List<Map<String, Object>> getStoreDeviceById(List<String> listDevId);

    public List<Map<String, Object>> getDeviceIdent();   //获取编号

}

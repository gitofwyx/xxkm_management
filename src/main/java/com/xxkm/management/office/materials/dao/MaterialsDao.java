package com.xxkm.management.office.materials.dao;

import com.xxkm.management.office.materials.entity.Materials;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Repository
public interface MaterialsDao {

    public List<Materials> listDevice(int pageStart, int pageSize);

    public List<Materials> listDeviceById(List<String> listDevId);

    public int addDevice(Materials device);

    public List<Map<String, Object>> getDeviceNumber(String deviceId);

    public List<Map<String, Object>> getDeviceSelect();

    public List<Map<String, Object>> getStoreDeviceById(List<String> listDevId);

    public List<Map<String, Object>> getDeviceIdent();   //获取编号

    public int plusDeviceNumber(int dev_no, String deviceId);   //更新设备数量 （增加）

    public int minusDeviceNumber(int dev_no, String deviceId);   //更新设备数量 （减少）

}

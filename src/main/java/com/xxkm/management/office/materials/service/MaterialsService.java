package com.xxkm.management.office.materials.service;

import com.xxkm.management.office.materials.entity.Materials;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
public interface MaterialsService {

    public List<Materials> listDevice(int pageStart, int pageSize);

    public List<Materials> listDeviceById(List<String> listDevId);

    public boolean addDevice(Materials device);

    public List<Map<String, Object>> getDeviceNumber(String deviceId);

    public List<Map<String, Object>> getDeviceSelect();

    public List<Map<String, Object>> getStoreDeviceById(List<String> listDevId);

    public List<Map<String, Object>> getDeviceIdent();  //获取设备编号

    public boolean plusDeviceNumber(int dev_no, String deviceId);

    public boolean minusDeviceNumber(int dev_no, String deviceId);

}

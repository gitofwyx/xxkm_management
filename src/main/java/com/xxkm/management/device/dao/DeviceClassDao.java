package com.xxkm.management.device.dao;

import com.xxkm.management.device.entity.Device;
import com.xxkm.management.device.entity.DeviceClass;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Repository
public interface DeviceClassDao {

    public List<Device> listDeviceClass(int pageStart, int pageSize);

    public List<Map<String, Object>> listAllDeviceName();

    public List<Map<String, Object>> listDeviceOfTab(String tab);

    public List<Map<String, Object>> listMaterialOfTab(String tab);

    public int addDeviceClass(DeviceClass deviceClass);

    public DeviceClass getDeviceClassById(String id);

    public List<Map<String, Object>> getClassClassById(String id);

    public int updateDevMax(DeviceClass deviceClass);

    public int updateDev_maxMax(DeviceClass deviceClass);

    public int updateMat_maxMax(DeviceClass deviceClass);

}

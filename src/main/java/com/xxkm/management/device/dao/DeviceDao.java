package com.xxkm.management.device.dao;

import com.xxkm.management.device.entity.Device;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Repository
public interface DeviceDao {

    public List<Device> listDevice(int pageStart, int pageSize);

    public int countDevice();

    public List<Device> listDeviceById(List<String> listDevId);

    public int addDevice(Device device);

    public Device getDeviceById(String deviceId);

    public Device getDeviceByBar_code(String Bar_code);

    public List<Map<String, Object>> getDeviceNumber(String deviceId);

    public List<Map<String, Object>> getDeviceSelect();

    public List<Map<String, Object>> getStoreDeviceById(List<String> listDevId);

    public List<Map<String, Object>> getDeviceIdent();   //获取编号

}

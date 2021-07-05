package com.xxkm.management.device.service;

import com.xxkm.management.device.entity.Device;
import com.xxkm.management.stock.entity.Stock;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
public interface DeviceService {

    public List<Device> listDevice(int pageStart, int pageSize);

    public int countDevice();

    public List<Device> listDeviceById(List<String> listDevId);

    public boolean addDevice(Device device);

    public Device getDeviceById(String deviceId);

    public List<Map<String, Object>> getDeviceNumber(String deviceId);

    public List<Map<String, Object>> getDeviceSelect();

    public List<Map<String, Object>> getStoreDeviceById(List<String> listDevId);

    public List<Map<String, Object>> getDeviceIdent();  //获取设备编号

    public Stock makeStockByDevice(Stock stock);

}

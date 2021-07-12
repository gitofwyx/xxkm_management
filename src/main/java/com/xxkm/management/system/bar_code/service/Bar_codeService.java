package com.xxkm.management.system.bar_code.service;

import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.system.bar_code.entity.Bar_code;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
public interface Bar_codeService {

    public List<Bar_code> listDevice(int pageStart, int pageSize);

    public int countDevice();

    public List<Bar_code> listDeviceById(List<String> listDevId);

    public boolean addDevice(Bar_code device);

    public Bar_code getDeviceById(String deviceId);

    public List<Map<String, Object>> getDeviceNumber(String deviceId);

    public List<Map<String, Object>> getDeviceSelect();

    public List<Map<String, Object>> getStoreDeviceById(List<String> listDevId);

    public List<Map<String, Object>> getDeviceIdent();  //获取设备编号

    public Stock makeStockByDevice(Stock stock);

}

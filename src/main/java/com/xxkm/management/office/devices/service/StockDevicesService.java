package com.xxkm.management.office.devices.service;

import com.xxkm.management.office.devices.entity.Devices;
import com.xxkm.management.office.storage.entity.OfficesStorage;
import com.xxkm.management.storage.entity.Delivery;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
public interface StockDevicesService {

    public List<Devices> listDevices(int pageStart, int pageSize, String class_id, String device_id, String location_office_id);

    public int countDevices();

    public boolean addStockDevices(Devices devices,OfficesStorage storage,String stock_version);

    public boolean deliveryStockDevices(Devices devices, Delivery delivery,double stock_no) ;

    public List<Devices> getDevicesWithStatus(String deviceId, String officeId, String status);

}

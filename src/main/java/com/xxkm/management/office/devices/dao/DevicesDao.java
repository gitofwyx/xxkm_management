package com.xxkm.management.office.devices.dao;

import com.xxkm.management.office.devices.entity.Devices;
import com.xxkm.management.office.storage.entity.OfficesStorage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Repository
public interface DevicesDao {

    public List<Devices> listDevices(@Param("pageStart") int pageStart, @Param("pageSize") int pageSize, @Param("class_id") String class_id,
                                           @Param("device_id") String device_id, @Param("location_office_id") String location_office_id);

    public int countDevices();

    public List<Devices> listDevicesById(List<String> listDevId);

    public int addDevices(Devices device);

    public List<Map<String, Object>> getDevicesNumber(String deviceId);

    public List<Map<String, Object>> getDevicesSelect();

    public List<Map<String, Object>> getStoreDevicesById(List<String> listDevId);

    public List<Map<String, Object>> getDevicesIdent();   //获取编号

    public List<Devices> getDevicesWithStatus(String deviceId,String officeId,String status);

    public List<Map<String, Object>> getDevicesWithDepositoryId(String depositoryId,String status);

    public int updateDevicesForDeployment(Devices device);

    public int updateDevicesStatus(String devicesId,String location_office_id,String present_stock_id,String status,String userId,String Date);

    public int transferDevices(Devices devices);

}

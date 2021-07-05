package com.xxkm.management.office.devices.service.impl;

import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.management.office.depository.service.DepositoryService;
import com.xxkm.management.office.devices.dao.DevicesDao;
import com.xxkm.management.office.devices.dao.StockDevicesDao;
import com.xxkm.management.office.devices.entity.Devices;
import com.xxkm.management.office.devices.service.DevicesService;
import com.xxkm.management.office.devices.service.StockDevicesService;
import com.xxkm.management.office.storage.entity.OfficesStorage;
import com.xxkm.management.office.storage.service.OfficesStorageService;
import com.xxkm.management.stock.service.StockService;
import com.xxkm.management.storage.entity.Delivery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Service
public class StockDevicesServiceImpl implements StockDevicesService {

    private static Logger log = Logger.getLogger(StockDevicesServiceImpl.class);

    @Autowired
    private StockDevicesDao dao;

    @Autowired
    private OfficesStorageService storageService;

    @Autowired
    private StockService stockService;


    @Override
    public List<Devices> listDevices(int pageStart, int pageSize, String class_id, String device_id, String location_office_id) {
        return dao.listStockDevices((pageStart - 1) * pageSize, pageSize, class_id, device_id, location_office_id);
    }

    @Override
    public int countDevices() {
        return 0;
    }

    @Override
    public boolean addStockDevices(Devices devices, OfficesStorage storage,String stock_version) {

        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String devicesId = UUIdUtil.getUUID();
        boolean devicesResult = false;
        try {
            boolean Result =stockService.plusStockConfiguredTotal(devices.getPresent_stock_id(),devices.getCreateUserId(),createDate,stock_version);
            if(!Result){
                log.error("addStockDevices:plusStockConfiguredTotal错误！");
                return false;
            }
            devices.setId(devicesId);
            devices.setClass_id(storage.getClass_id());
            devices.setDevice_id(storage.getEntity_id());
            devices.setDevices_ident("NO");
            devices.setDevice_state("0");
            devices.setLocation_office_id(storage.getOffices_storage_officeId());
            devices.setInventory_office_id(storage.getOffices_storage_officeId());
            devices.setDevice_origin("1");
            devices.setDevice_deployment_status("0");
            devices.setRelated_flag("0");
            devices.setCreateDate(createDate);
            devices.setUpdateUserId(devices.getCreateUserId());
            devices.setUpdateDate(createDate);

            devices.setDeleteFlag("0");
            devicesResult = dao.addStockDevices(devices) == 1 ? true : false;
            if (!(devicesResult)) {
                log.error("depositoryResult:" + devicesResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                storage.setEntity_id(devices.getDevice_id());
                storage.setOffices_entity_id(devicesId);
                storage.setOffices_storage_genre("0");
                storage.setEntity_entry_status("0");
                result = storageService.addOfficesStorage(devices,storage);
                if("true".equals(result.get("hasError"))){
                    return false;
                }
            }
        } catch (DuplicateKeyException e) {
            result.put("hasError", true);
            result.put("error", "重复值异常，可能编号值重复");
            log.error(e);
        } catch (Exception e) {
            result.put("hasError", true);
            result.put("error", "添加出错");
            log.error(e);
        }
        return devicesResult;
    }

    //出库
    @Override
    public boolean deliveryStockDevices(Devices devices, Delivery delivery,double stock_no) {

        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        boolean devicesResult = false;
        try {
            devices.setId(delivery.getStock_entity_id());
            devices.setDevice_origin("1");
            devices.setDevice_deployment_status("1");
            devices.setRelated_flag("0");
            devices.setUpdateDate(createDate);

            devices.setDeleteFlag("0");
            devicesResult = dao.updateDeviceStatus(devices) == 1 ? true : false;
            if (!(devicesResult)) {
                log.error("depositoryResult:" + devicesResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                delivery.setOut_confirmed_ident("NO");
                delivery.setOut_confirmed_type("1");
                delivery.setOut_confirmed_date(createDate);
                delivery.setOut_confirmed_total(1);
                delivery.setOut_confirmed_by(devices.getUpdateUserId());
                delivery.setCreateUserId(devices.getUpdateUserId());
                delivery.setCreateDate(createDate);
                delivery.setUpdateUserId(devices.getUpdateUserId());
                delivery.setUpdateDate(createDate);
                result = stockService.updateSingleStockWithDelivery(delivery,stock_no);
                if("true".equals(result.get("hasError"))){
                    return false;
                }
            }
        } catch (DuplicateKeyException e) {
            result.put("hasError", true);
            result.put("error", "重复值异常，可能编号值重复");
            log.error(e);
        } catch (Exception e) {
            result.put("hasError", true);
            result.put("error", "添加出错");
            log.error(e);
        }
        return devicesResult;
    }

    //根据部署状态获取设备
    @Override
    public List<Devices> getDevicesWithStatus(String deviceId, String officeId, String status) {

        return dao.getDevicesWithStatus(deviceId, officeId, status);
    }

}

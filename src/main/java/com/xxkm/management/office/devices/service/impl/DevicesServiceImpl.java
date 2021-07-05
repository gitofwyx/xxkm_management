package com.xxkm.management.office.devices.service.impl;

import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.management.office.depository.service.DepositoryService;
import com.xxkm.management.office.devices.dao.DevicesDao;
import com.xxkm.management.office.devices.entity.Devices;
import com.xxkm.management.office.devices.service.DevicesService;
import com.xxkm.management.office.storage.entity.OfficesStorage;
import com.xxkm.management.office.storage.service.OfficesStorageService;
import com.xxkm.management.stock.service.StockService;
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
public class DevicesServiceImpl implements DevicesService {

    private static Logger log = Logger.getLogger(DevicesServiceImpl.class);

    @Autowired
    private DevicesDao dao;

    @Autowired
    private OfficesStorageService storageService;

    @Autowired
    private StockService stockService;

    @Autowired
    private DepositoryService depositoryService;


    @Override
    public List<Devices> listDevices(int pageStart, int pageSize, String class_id, String device_id, String location_office_id) {
        return dao.listDevices((pageStart - 1) * pageSize, pageSize, class_id, device_id, location_office_id);
    }

    @Override
    public int countDevices() {
        return dao.countDevices();
    }

    @Override
    public List<Devices> listDevicesById(List<String> listDevId) {
        return dao.listDevicesById(listDevId);
    }

    @Override
    public boolean addDevices(Devices devices, OfficesStorage storage) {

        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String devicesId = UUIdUtil.getUUID();
        boolean devicesResult = false;
        try {

            devices.setId(devicesId);
            devices.setClass_id(storage.getClass_id());
            devices.setDevice_id(storage.getEntity_id());
            devices.setDevices_ident("NO");
            devices.setDevice_state("0");
            devices.setLocation_office_id(storage.getOffices_storage_officeId());
            devices.setInventory_office_id(storage.getOffices_storage_officeId());
            devices.setDevice_origin("1");
            devices.setDevice_deployment_status("2");
            devices.setRelated_flag("0");
            devices.setCreateDate(createDate);
            devices.setUpdateUserId(devices.getCreateUserId());
            devices.setUpdateDate(createDate);

            devices.setDeleteFlag("0");
            devicesResult = dao.addDevices(devices) == 1 ? true : false;
            if (!(devicesResult)) {
                log.error("depositoryResult:" + devicesResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                storage.setEntity_id(devices.getDevice_id());
                storage.setOffices_entity_id(devicesId);
                storage.setOriginal_storage_officeId(devices.getInventory_office_id());
                storage.setOffices_storage_genre("2");
                storage.setEntity_entry_status("2");
                result = storageService.addOfficesStorage(devices, storage);
                if ("true".equals(result.get("hasError"))) {
                    return false;
                }
                devicesResult = depositoryService.deploymentDeviceWithSingle(storage.getStock_or_depository_id(), storage.getUpdateUserId(), createDate);
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

    //部署操作；标注时间：2021年6月17日 23:53:48
    @Override
    public boolean updateDevicesForDeployment(Devices devices, OfficesStorage storage) {

        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        boolean devicesResult = false;
        try {

            devices.setClass_id(storage.getClass_id());
            devices.setDevice_id(storage.getEntity_id());
            devices.setDevices_ident("NO");
            devices.setDevice_deployment_status("2");
            devices.setRelated_flag("0");
            devices.setUpdateUserId(devices.getCreateUserId());
            devices.setUpdateDate(createDate);

            devices.setDeleteFlag("0");
            devicesResult = dao.updateDevicesForDeployment(devices) == 1 ? true : false;
            if (!(devicesResult)) {
                log.error("depositoryResult:" + devicesResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                storage.setEntity_id(devices.getDevice_id());
                storage.setOffices_entity_id(devices.getId());
                storage.setOriginal_storage_officeId(devices.getInventory_office_id());
                storage.setOffices_storage_genre("2");
                storage.setEntity_entry_status("2");
                result = storageService.addOfficesStorage(devices, storage);
                if ("true".equals(result.get("hasError"))) {
                    return false;
                }
                devicesResult = depositoryService.deploymentDeviceWithSingle(storage.getStock_or_depository_id(), storage.getUpdateUserId(), createDate);
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

    //
    @Override
    public boolean updateDevicesStatus(String devicesId, String location_office_id, String present_stock_id, String status, String userId, String Date) {

        String createDate = DateUtil.getFullTime();
        boolean devicesResult = false;
        try {
            devicesResult = dao.updateDevicesStatus(devicesId, location_office_id, present_stock_id, status, userId, Date) == 1 ? true : false;
            if (!(devicesResult)) {
                log.error("depositoryResult:" + devicesResult);
            }
        } catch (DuplicateKeyException e) {
            log.error(e);
        } catch (Exception e) {
            log.error(e);
        }
        return devicesResult;
    }

    //转科
    @Override
    public boolean transferDevices(Devices devices, OfficesStorage officesStorage) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        boolean devicesResult = false;
        try {
            devices.setPresent_stock_id(officesStorage.getStock_or_depository_id());//获取库存的id值
            devices.setId(officesStorage.getOffices_entity_id());
            devices.setDevice_id(officesStorage.getEntity_id());
            devices.setLocation_office_id(officesStorage.getOffices_storage_officeId());
            devices.setDevice_deployment_status("1");
            devices.setUpdateDate(createDate);
            devices.setDeleteFlag("0");
            devicesResult = dao.transferDevices(devices) == 1 ? true : false;
            if (!(devicesResult)) {
                log.error("depositoryResult:" + devicesResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                officesStorage.setOffices_storage_ident("NO");
                officesStorage.setOffices_storage_type("1");//设备\耗材类别（1.设备2.配件3.耗材）
                officesStorage.setOffices_storage_genre("4");//流动类别（0：配置1.入科2.部署3.回收4.调用5.借用）
                officesStorage.setOffices_storage_date(createDate);
                officesStorage.setOffices_storage_total(1);
                officesStorage.setOffices_storage_by(devices.getUpdateUserId());
                officesStorage.setEntity_entry_status("1");//入科状态（0：配置待入科1：待入科；2：部分待入科；3：已入科；4：部分已入科）
                result = depositoryService.transferDepositoryForDelivery(devices, officesStorage);
                if ("true".equals(result.get("hasError"))) {
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

    //回收
    @Override
    public boolean recoveryDevices(Devices devices, OfficesStorage officesStorage, String stock_no, String stock_unit, String stock_proportion) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        boolean devicesResult = false;
        try {
            devices.setId(officesStorage.getOffices_entity_id());
            devices.setDevice_id(officesStorage.getEntity_id());
            devices.setLocation_office_id(officesStorage.getOffices_storage_officeId());
            devices.setDevice_deployment_status("0");
            devices.setUpdateDate(createDate);
            devicesResult = dao.transferDevices(devices) == 1 ? true : false;
            if (!(devicesResult)) {
                log.error("depositoryResult:" + devicesResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                officesStorage.setOffices_storage_ident("NO");
                officesStorage.setOffices_storage_type("1");//设备\耗材类别（1.设备2.配件3.耗材）
                officesStorage.setOffices_storage_genre("3");//流动类别（0：配置1.入科2.部署3.回收4.调用5.借用）
                officesStorage.setOffices_storage_date(createDate);
                officesStorage.setOffices_storage_total(1);
                officesStorage.setOffices_storage_by(devices.getUpdateUserId());
                officesStorage.setEntity_entry_status("0");//入科状态（0：配置待入科1：待入科；2：部分待入科；3：已入科；4：部分已入科）
                result = depositoryService.transferDepositoryForDelivery(devices, officesStorage);
                if ("true".equals(result.get("hasError"))) {
                    return false;
                }
                officesStorage.setStock_or_depository_id(devices.getPresent_stock_id());
                officesStorage.setOffices_storage_no(Float.parseFloat(stock_no));
                officesStorage.setOffices_storage_total(1);
                officesStorage.setOffices_storage_unit(stock_unit);
                officesStorage.setOffices_storage_proportion(Integer.parseInt(stock_proportion));
                officesStorage.setCreateUserId(devices.getUpdateUserId());
                officesStorage.setCreateDate(createDate);
                officesStorage.setUpdateUserId(devices.getUpdateUserId());
                officesStorage.setUpdateDate(createDate);
                if ("".equals(devices.getPresent_stock_id()) || devices.getPresent_stock_id() == null) {
                    result = stockService.addStockForRecovery(officesStorage);
                } else {
                    result = stockService.updateStockForRecovery(officesStorage);
                }
                if (!(devicesResult)) {
                    log.error("depositoryResult:" + devicesResult);
                    result.put("hasError", true);
                    result.put("error", "添加出错");
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

    @Override
    public List<Map<String, Object>> getDevicesNumber(String devicesId) {
        return dao.getDevicesNumber(devicesId);
    }

    @Override
    public List<Map<String, Object>> getDevicesSelect() {
        return dao.getDevicesSelect();
    }

    @Override
    public List<Map<String, Object>> getStoreDevicesById(List<String> listDevId) {
        return dao.getStoreDevicesById(listDevId);
    }

    @Override
    public List<Map<String, Object>> getDevicesIdent() {
        if (dao.getDevicesIdent().size() != 1) {
            log.error("getDeviceIdent:获取设备编号错误");
            return null;
        }
        return dao.getDevicesIdent();
    }

    //根据部署状态获取设备
    @Override
    public List<Devices> getDevicesWithStatus(String deviceId, String officeId, String status) {

        return dao.getDevicesWithStatus(deviceId, officeId, status);
    }

    //根据部署状态获取设备
    @Override
    public List<Map<String, Object>> getDevicesWithDepositoryId(String depositoryId, String status) {

        return dao.getDevicesWithDepositoryId(depositoryId, status);
    }

}

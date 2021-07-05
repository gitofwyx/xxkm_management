package com.xxkm.management.office.devices.controller;

import com.xxkm.core.file.BaseController;
import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.JsonUtils;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.core.util.build_ident.IdentUtil;
import com.xxkm.management.device.entity.DeviceClass;
import com.xxkm.management.device.service.DeviceClassService;
import com.xxkm.management.device.service.DeviceService;
import com.xxkm.management.office.depository.entity.Depository;
import com.xxkm.management.office.devices.entity.Devices;
import com.xxkm.management.office.devices.service.DevicesService;
import com.xxkm.management.office.storage.entity.OfficesStorage;
import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.storage.entity.Delivery;
import com.xxkm.management.storage.entity.Storage;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Controller
@RequestMapping("")
public class DevicesController extends BaseController {

    private static Logger log = Logger.getLogger(DevicesController.class);

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DevicesService devicesService;


    @ResponseBody
    @RequestMapping("/listDevices")
    public Map<String, Object> listDevices(@RequestParam(value = "pageIndex") String pageIndex,
                                           @RequestParam(value = "limit") String limit,
                                           @RequestParam(value = "search_class_id") String class_id,//型号id
                                           @RequestParam(value = "search_entity_id") String entity_id,//设备、耗材id
                                           @RequestParam(value = "location_office_id") String location_office_id) {//科室id
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            int pageNumber = Integer.parseInt(pageIndex) + 1;//因为pageindex 从0开始
            int pageSize = Integer.parseInt(limit);

            List<Devices> listDevice = devicesService.listDevices(pageNumber, pageSize, class_id, entity_id, location_office_id);
            if (listDevice == null) {
                log.error("获取分页出错");
                result.put("success", false);
                return result;
            } else if (listDevice.isEmpty()) {
                result.put("rows", resultList);
                result.put("results", 0);
            } else {
                List<String> listEntId = new ArrayList<>();
                for (Devices device : listDevice) {
                    listEntId.add(device.getDevice_id());
                }
               /* Set setDevId = new  HashSet();
                setDevId.addAll(listDevId);
                listDevId.clear();
                listDevId.addAll(setDevId);*/
                List<Map<String, Object>> entityList = new ArrayList<>();
                entityList = deviceService.getStoreDeviceById(listEntId);
                if ( entityList == null) {
                    log.error("获取分页出错");
                    result.put("hasError", true);
                    result.put("error", "获取出错");
                    return result;
                } else {
                    for (Devices device : listDevice) {
                        Map<String, Object> resultMap = new HashMap<>();
                        for (Map<String, Object> entityMap : entityList) {
                            if (device.getDevice_id().equals(entityMap.get("id"))) {
                                resultMap.put("entity_name", entityMap.get("entity_name"));
                                resultMap.put("entity_type", entityMap.get("entity_type"));
                            }
                        }
                        resultMap.putAll(JsonUtils.toMap(device));
                        resultList.add(resultMap);
                    }
                }
                result.put("rows", resultList);
                result.put("results", devicesService.countDevices());
            }
        } catch (Exception e) {
            log.error(e);
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/addDevices")
    public Map<String, Object> addDevices(Devices devices, OfficesStorage officesStorage,
                                          @RequestParam(value = "depository_id") String depository_id,
                                          @RequestParam(value = "devices_id") String devices_id) {
        Map<String, Object> result = new HashMap<>();
        String Date = DateUtil.getFullTime();
        boolean Result=false;
        try {
            if ("".equals(depository_id) || depository_id == null) {
                result.put("hasError", true);
                result.put("error", "设备更新出错！");
                return result;
            }
            String CurrentUserId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
            devices.setCreateUserId(CurrentUserId);
            officesStorage.setStock_or_depository_id(depository_id);
            officesStorage.setOffices_storage_type("1");
            if(!"".equals(devices_id) || devices_id != null){
                devices.setId(devices_id);
                Result = devicesService.updateDevicesForDeployment(devices, officesStorage);
            }else {
                Result = devicesService.addDevices(devices, officesStorage);
            }
            if (!(Result)) {
                result.put("success", false);
            } else {
                result.put("success", true);
            }
        } catch (Exception e) {
            result.put("success", false);
            log.error(e);
        }
        return result;
        //return "system/index";
    }

    //转科
    @ResponseBody
    @RequestMapping(value = "/transferDevices", method = RequestMethod.POST)
    public Map<String, Object> transferDevices(Devices devices, OfficesStorage officesStorage) {
        Map<String, Object> result = new HashMap<>();
        try {

            String CurrentUserId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
            devices.setUpdateUserId(CurrentUserId);
            if (officesStorage.getStock_or_depository_id() != null && !"".equals(officesStorage.getStock_or_depository_id())) {
                boolean Result = devicesService.transferDevices(devices, officesStorage);
                if (!(Result)) {
                    result.put("success", false);
                } else {
                    result.put("success", true);
                }
            }

        } catch (Exception e) {
            log.error(e);
            result.put("hasError", true);
            result.put("error", "更新出错");
        }
        return result;
        //return "system/index";
    }

    @ResponseBody
    @RequestMapping("/recoveryDevices")
    public Map<String, Object> recoveryDevices(Devices devices, OfficesStorage officesStorage,
                                               @RequestParam(value = "stock_no") String stock_no,
                                               @RequestParam(value = "stock_unit") String stock_unit,
                                               @RequestParam(value = "stock_proportion") String stock_proportion) {
        Map<String, Object> result = new HashMap<>();
        try {
            String CurrentUserId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
            devices.setUpdateUserId(CurrentUserId);
            if (officesStorage.getStock_or_depository_id() != null && !"".equals(officesStorage.getStock_or_depository_id())) {
                boolean Result = devicesService.recoveryDevices(devices, officesStorage,stock_no,stock_unit,stock_proportion);
                if (!(Result)) {
                    result.put("success", false);
                } else {
                    result.put("success", true);
                }
            }

        } catch (Exception e) {
            log.error(e);
            result.put("hasError", true);
            result.put("error", "更新出错");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/getDevicesName")
    public Map<String, Object> getDevicesName() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> listResult = new ArrayList<>();
        //List<Map<String, Object>> dev_count = new ArrayList<>();
        try {
            //listResult = devicesService.();
            //dev_count = deviceClassService.getCountClassById("1fa2614d-4a55-1234-a79a-5546319b9123");
            if (listResult == null) {
                log.error("获取出错");
                return null;
            }
            result.put("dev_class", listResult);
            /*result.put("dev_count", dev_count);*/
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return result;
    }

    //获取未部署的设备
    @ResponseBody
    @RequestMapping("/getDevicesDeployment")
    public Map<String, Object> getDevicesDeployment(@RequestParam(value = "deviceId") String deviceId,
                                                    @RequestParam(value = "officeId") String officeId) {
        Map<String, Object> result = new HashMap<>();
        //List<Map<String, Object>> dev_count = new ArrayList<>();
        try {
            List<Devices> listDevices=devicesService.getDevicesWithStatus(deviceId,officeId,"1");
            if (listDevices == null) {
                return null;
            }
            result.put("listDevices", listDevices);
        } catch (Exception e) {
            log.error(e);
            result.put("hasError", true);
            result.put("error", "获取出错");
        }
        return result;
    }

    //获取未部署的设备
    @ResponseBody
    @RequestMapping("/getDevicesWithDepositoryId")
    public Map<String, Object> getDevicesWithDepositoryId(@RequestParam(value = "depositoryId") String depositoryId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> listDevices = new ArrayList<>();
        try {
            listDevices=devicesService.getDevicesWithDepositoryId(depositoryId,"2");
            if (listDevices == null) {
                return null;
            }
            result.put("listDevices", listDevices);
        } catch (Exception e) {
            log.error(e);
            result.put("hasError", true);
            result.put("error", "获取出错");
        }
        return result;
    }

}

package com.xxkm.management.office.devices.controller;

import com.xxkm.core.file.BaseController;
import com.xxkm.core.util.JsonUtils;
import com.xxkm.management.device.service.DeviceService;
import com.xxkm.management.office.devices.entity.Devices;
import com.xxkm.management.office.devices.service.StockDevicesService;
import com.xxkm.management.office.storage.entity.OfficesStorage;
import com.xxkm.management.stock.service.StockService;

import com.xxkm.management.storage.entity.Delivery;
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
public class StockDevicesController extends BaseController {

    private static Logger log = Logger.getLogger(StockDevicesController.class);

    @Autowired
    private StockService stockService;

    @Autowired
    private StockDevicesService stockDevicesService;

    @Autowired
    private DeviceService deviceService;

    @ResponseBody
    @RequestMapping("/listStockDevices")
    public Map<String, Object> listStockDevices(@RequestParam(value = "pageIndex") String pageIndex,
                                           @RequestParam(value = "limit") String limit,
                                           @RequestParam(value = "search_class_id") String class_id,//型号id
                                           @RequestParam(value = "search_entity_id") String entity_id,//设备、耗材id
                                           @RequestParam(value = "location_office_id") String location_office_id) {//科室id
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            int pageNumber = Integer.parseInt(pageIndex) + 1;//因为pageindex 从0开始
            int pageSize = Integer.parseInt(limit);

            List<Devices> listStockDevice = stockDevicesService.listDevices(pageNumber, pageSize, class_id, entity_id, location_office_id);
            if (listStockDevice == null) {
                log.error("获取分页出错");
                result.put("success", false);
                return result;
            } else if (listStockDevice.isEmpty()) {
                result.put("rows", resultList);
                result.put("results", 0);
            } else {
                List<String> listEntId = new ArrayList<>();
                for (Devices device : listStockDevice) {
                    listEntId.add(device.getDevice_id());
                }
                List<Map<String, Object>> entityList = new ArrayList<>();
                entityList = deviceService.getStoreDeviceById(listEntId);
                if ( entityList == null) {
                    log.error("获取分页出错");
                    result.put("hasError", true);
                    result.put("error", "获取出错");
                    return result;
                } else {
                    for (Devices device : listStockDevice) {
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
                result.put("results", stockDevicesService.countDevices());
            }
        } catch (Exception e) {
            log.error(e);
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/addStockDevices", method = RequestMethod.POST)
    public Map<String, Object> addStockDevices(Devices devices, OfficesStorage storage,
                                               @RequestParam(value = "stock_version") String stock_version) {
        Map<String, Object> result = new HashMap<>();
        try {

            String CurrentUserId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
            devices.setCreateUserId(CurrentUserId);
            if (devices.getPresent_stock_id() != null && !"".equals(devices.getPresent_stock_id())) {
                storage.setStock_or_depository_id(devices.getPresent_stock_id());//获取库存的id值
                storage.setOffices_storage_type("1");
                boolean Result = stockDevicesService.addStockDevices(devices, storage,stock_version);
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

    //出库
    @ResponseBody
    @RequestMapping(value = "/deliveryStockDevices", method = RequestMethod.POST)
    public Map<String, Object> deliveryStockDevices(Devices devices, Delivery delivery,
                                                    @RequestParam(value = "stock_no") String stock_no) {
        Map<String, Object> result = new HashMap<>();
        try {

            String CurrentUserId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
            devices.setUpdateUserId(CurrentUserId);
            if (devices.getPresent_stock_id() != null && !"".equals(devices.getPresent_stock_id())) {
                delivery.setStock_id(devices.getPresent_stock_id());//获取库存的id值
                delivery.setOut_confirmed_type("1");
                boolean Result = stockDevicesService.deliveryStockDevices(devices, delivery,Double.parseDouble(stock_no));
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

}

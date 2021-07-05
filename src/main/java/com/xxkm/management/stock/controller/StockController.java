package com.xxkm.management.stock.controller;

import com.xxkm.core.file.BaseController;
import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.JsonUtils;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.core.util.build_ident.IdentUtil;
import com.xxkm.management.device.entity.Device;
import com.xxkm.management.device.service.DeviceService;
import com.xxkm.management.material.service.MaterialService;
import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.storage.entity.Delivery;
import com.xxkm.management.storage.entity.Storage;
import com.xxkm.management.storage.service.DeliveryService;
import com.xxkm.management.stock.service.StockService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
public class StockController extends BaseController {

    private static Logger log = Logger.getLogger(StockController.class);

    @Autowired
    private StockService stockService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MaterialService materialService;

    @ResponseBody
    @RequestMapping("/listStock")
    public Map<String, Object> listStock(@RequestParam(value = "pageIndex") String pageIndex,
                                         @RequestParam(value = "limit") String limit,
                                         @RequestParam(value = "search_class_id") String class_id,//型号id
                                         @RequestParam(value = "search_entity_id") String entity_id,//型号id
                                         @RequestParam(value = "search_office_id") String stock_office_id,//库存科室id
                                         @RequestParam(value = "search_type") String search_type) {//类别
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            int pageNumber = Integer.parseInt(pageIndex) + 1;//页数，因为pageindex 从0开始要加1
            int pageSize = Integer.parseInt(limit);         //单页记录数
            List<Stock> listStock = stockService.listStock(pageNumber, pageSize, class_id, entity_id, stock_office_id, search_type);
            if (listStock == null) {
                log.error("listStock:获取分页出错");
                result.put("hasError", true);
                result.put("error", "获取出错");
                return result;
            } else if (listStock.isEmpty()) {
                result.put("rows", resultList);
                result.put("results", 0);
            } else {
                List<String> listEntId = new ArrayList<>();
                for (Stock stock : listStock) {
                    listEntId.add(stock.getEntity_id());
                }
               /* Set setDevId = new  HashSet();
                setDevId.addAll(listDevId);
                listDevId.clear();
                listDevId.addAll(setDevId);*/
                List<Map<String, Object>> entityList = new ArrayList<>();
                if ("1".equals(search_type)) {
                    entityList = deviceService.getStoreDeviceById(listEntId);
                }else if("2".equals(search_type)|| "3".equals(search_type)){
                    entityList = materialService.getStoreMaterialById(listEntId);
                }
                else {
                    entityList = null;
                }
                if (resultList == null || entityList == null) {
                    log.error("获取分页出错");
                    result.put("hasError", true);
                    result.put("error", "获取出错");
                    return result;
                } else {
                    for (Stock stock : listStock) {
                        Map<String, Object> resultMap = new HashMap<>();
                        for (Map<String, Object> entityMap : entityList) {
                            if (stock.getEntity_id().equals(entityMap.get("id"))) {
                                resultMap.put("entity_name", entityMap.get("entity_name"));
                                resultMap.put("entity_type", entityMap.get("entity_type"));
                            }
                        }
                        resultMap.putAll(JsonUtils.toMap(stock));
                        resultList.add(resultMap);
                    }
                }
                result.put("rows", resultList);
                result.put("results", stockService.countStock(search_type));
            }
        } catch (Exception e) {
            log.error(e);
            result.put("hasError", true);
            result.put("error", "获取出错");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/addStock", method = RequestMethod.POST)
    public Map<String, Object> addStock(Stock stock, Storage storage,
                                        @RequestParam(value = "stock_record_id") String stock_record_id) {
        Map<String, Object> result = new HashMap<>();
        try {

            String CurrentUserId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
            stock.setUpdateUserId(CurrentUserId);
            if (stock_record_id != null && !"".equals(stock_record_id)) {
                stock.setId(stock_record_id);//获取库存的id值
                result = stockService.updateStockWithStorage(stock, storage);
            } else {
                result = stockService.addStockWithStorage(stock, storage);
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
    @RequestMapping("/updateStock")
    public Map<String, Object> updateStock(Stock stock, Delivery delivery,
                                           @RequestParam(value = "stock_record_id") String stock_record_id) {
        Map<String, Object> result = new HashMap<>();
        try {
            String CurrentUserId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
            stock.setUpdateUserId(CurrentUserId);
            if (stock_record_id != null && !"".equals(stock_record_id)) {
                stock.setId(stock_record_id);//获取库存的id值
                result = stockService.updateStockWithDelivery(stock, delivery);
            } else {
                result.put("hasError", true);
                result.put("error", "updateStock:获取stock_record_id出错");
                log.error("updateStock:" + result.get("error"));
            }
        } catch (Exception e) {
            result.put("hasError", true);
            result.put("error", "设备更新出错！"+e.getCause().getLocalizedMessage());
            log.error(e);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/updateStockForMaterial")
    public Map<String, Object> updateStockForMaterial(Stock stock, Delivery delivery,
                                           @RequestParam(value = "stock_record_id") String stock_record_id) {
        Map<String, Object> result = new HashMap<>();
        try {
            if("1".equals(stock.getStock_type())||"".equals(stock.getStock_type())){
                result.put("hasError", true);
                result.put("error", "出错！设备类型获取不能为"+stock.getStock_type());
                return result;
            }
            Device device=deviceService.getDeviceById(delivery.getEntity_id());
            if(null!=device||"1".equals(stock.getStock_type())){
                result.put("hasError", true);
                result.put("error", "出错！设备设为配置出库！");
                return result;
            }
            String CurrentUserId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
            stock.setUpdateUserId(CurrentUserId);
            if (stock_record_id != null && !"".equals(stock_record_id)) {
                stock.setId(stock_record_id);//获取库存的id值
                result = stockService.updateStockWithDelivery(stock, delivery);
            } else {
                result.put("hasError", true);
                result.put("error", "updateStock:获取stock_record_id出错");
                log.error("updateStock:" + result.get("error"));
            }
        } catch (Exception e) {
            result.put("hasError", true);
            result.put("error", "设备更新出错！"+e.getCause().getLocalizedMessage());
            log.error(e);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/listStockByEntityId", method = RequestMethod.POST)
    public Map<String, Object> listStockByEntityId(@RequestParam(value = "entity_id") String entity_id,
                                                   @RequestParam(value = "office_id") String office_id) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Stock> listStock = stockService.listStockByEntityId(entity_id, office_id);
            if (listStock == null) {
                log.error("获取出错");
                return null;
            }
            result.put("entityData", listStock);
            /*result.put("dev_count", dev_count);*/
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getStockByStockId", method = RequestMethod.POST)
    public Map<String, Object> listStockByStockId(@RequestParam(value = "stockId") String stockId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Stock stock = stockService.getStockById(stockId);
            if (stock == null) {
                log.error("获取出错");
                return null;
            }
            result.put("Object", stock);
            result.put("success", true);
            /*result.put("dev_count", dev_count);*/
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getStocksByEntityId", method = RequestMethod.POST)
    public Map<String, Object> getStocksByEntityId(@RequestParam(value = "entity_id") String entity_id) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Stock> listStock = stockService.getStocksByEntityId(entity_id);
            if (listStock == null) {
                log.error("获取出错");
                return null;
            }
            result.put("entityData", listStock);
            /*result.put("dev_count", dev_count);*/
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return result;
    }

}

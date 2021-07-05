package com.xxkm.management.office.depository.controller;

import com.xxkm.core.file.BaseController;
import com.xxkm.core.util.JsonUtils;
import com.xxkm.management.device.service.DeviceService;
import com.xxkm.management.office.depository.entity.Depository;
import com.xxkm.management.office.depository.service.DepositoryService;
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
public class DepositoryController extends BaseController {

    private static Logger log = Logger.getLogger(DepositoryController.class);

    @Autowired
    private DepositoryService depositoryService;

    @Autowired
    private DeviceService deviceService;

    @ResponseBody
    @RequestMapping("/listDepository")
    public Map<String, Object> listDepository(@RequestParam(value = "pageIndex") String pageIndex,
                                              @RequestParam(value = "limit") String limit,
                                              @RequestParam(value = "search_class_id") String class_id,//型号id
                                              @RequestParam(value = "search_entity_id") String entity_id,//设备、耗材id
                                              @RequestParam(value = "search_office_id") String depository_officeId,//库存科室id
                                              @RequestParam(value = "search_type") int search_type) {//类别
        Map<String, Object> result = new HashMap<>();
        try {
            int pageNumber = Integer.parseInt(pageIndex) + 1;//页数，因为pageindex 从0开始要加1
            int pageSize = Integer.parseInt(limit);         //单页记录数
            List<Depository> listDepository = depositoryService.listDepository(pageNumber, pageSize, class_id, entity_id, depository_officeId, search_type);
            if (listDepository == null) {
                log.error("listStock:获取分页出错");
                result.put("hasError", true);
                result.put("error", "获取出错");
                return result;
            } else if (listDepository.isEmpty()) {
                result.put("rows", listDepository);
                result.put("results", 0);
            } else {
                List<String> listDevId = new ArrayList<>();

               /* Set setDevId = new  HashSet();
                setDevId.addAll(listDevId);
                listDevId.clear();
                listDevId.addAll(setDevId);*/
                result.put("rows", listDepository);
                result.put("results", depositoryService.countDepository(Integer.toString(search_type)));
            }
        } catch (Exception e) {
            log.error(e);
            result.put("hasError", true);
            result.put("error", "获取出错");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/selectDepositoryForOfficeEntitys", method = RequestMethod.POST)
    public Map<String, Object> selectDepositoryForOfficeEntitys(@RequestParam(value = "entity_id") String entity_id,//库存科室id
                                                             @RequestParam(value = "depository_officeId") String depository_officeId) {//类别
        Map<String, Object> result = new HashMap<>();
        try {
            List<Depository> listDepository = depositoryService.selectDepository(entity_id, depository_officeId);
            if (listDepository == null) {
                return null;
            }
            result.put("listDepository", listDepository);
        } catch (Exception e) {
            log.error(e);
            result.put("hasError", true);
            result.put("error", "获取出错");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getDepositoryById", method = RequestMethod.POST)
    public Map<String, Object> getDepositoryById(@RequestParam(value = "depository_id") String depository_id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Depository depository = depositoryService.getDepositoryById(depository_id);
            if (depository == null) {
                return null;
            }
            result.put("depository", depository);
            result.put("success", true);
        } catch (Exception e) {
            result.put("hasError", true);
            result.put("error", "设备获取出错！" + e.getCause().getLocalizedMessage());
            log.error(e);
        }
        return result;
        //return "system/index";
    }

    @ResponseBody
    @RequestMapping(value = "/getDepositoryByEntId", method = RequestMethod.POST)
    public Map<String, Object> getDepositoryByEntId(@RequestParam(value = "entity_record_id") String entity_record_id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Depository depository = depositoryService.getDepositoryByEntId(entity_record_id);
            if (depository == null) {
                return null;
            }
            result.put("Object", depository);
            result.put("success", true);
        } catch (Exception e) {
            result.put("hasError", true);
            result.put("error", "设备获取出错！" + e.getCause().getLocalizedMessage());
            log.error(e);
        }
        return result;
        //return "system/index";
    }

    @ResponseBody
    @RequestMapping(value = "/addDepository", method = RequestMethod.POST)
    public Map<String, Object> addDepository(Depository depository, OfficesStorage storage,
                                             @RequestParam(value = "class_record_id") String class_record_id,
                                             @RequestParam(value = "entity_record_id") String entity_record_id,
                                             @RequestParam(value = "depository_id") String depository_id) {
        Map<String, Object> result = new HashMap<>();
        try {

            String CurrentUserId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
            depository.setId(depository_id);
            depository.setUpdateUserId(CurrentUserId);
            depository.setClass_id(class_record_id);
            depository.setEntity_id(entity_record_id);
            storage.setOffices_storage_type(depository.getDepository_type());
            if (!"".equals(depository_id) && depository_id != null) {
                depository.setId(depository_id);
                result = depositoryService.updateDepositoryWithStorage(depository, storage);
            } else {
                result = depositoryService.addDepositoryWithStorage(depository, storage);
            }

        } catch (Exception e) {
            result.put("hasError", true);
            result.put("error", "设备更新出错！" + e.getCause().getLocalizedMessage());
            log.error(e);
        }
        return result;
        //return "system/index";
    }

    @ResponseBody
    @RequestMapping("/recoveryDepository")
    public Map<String, Object> recoveryDepository(Depository depository, Stock stock, Storage storage,
                                                  @RequestParam(value = "stock_record_id") String stock_record_id) {
        Map<String, Object> result = new HashMap<>();
        try {
            String CurrentUserId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
            stock.setUpdateUserId(CurrentUserId);
            stock.setId(stock_record_id);
            stock.setClass_id(depository.getClass_id());
            stock.setEntity_id(depository.getEntity_id());
            stock.setStock_type(storage.getIn_confirmed_type());
            depository.setUpdateUserId(CurrentUserId);
            depositoryService.recoveryDepository(depository, stock, storage);
        } catch (Exception e) {

        }
        return result;
    }

   /* @ResponseBody
    @RequestMapping(value = "/listDepositoryByEntityId", method = RequestMethod.POST)
    public Map<String, Object> listDepositoryByEntityId(@RequestParam(value = "entity_id") String entity_id,
                                                        @RequestParam(value = "stock_office") String office_id) {
        int id = 0;
        Map<String, Object> result = new HashMap<>();
        try {
            List<Depository> listDepository = depositoryService.listDepositoryByEntityId(entity_id, office_id);
            if (listDepository == null) {
                log.error("获取出错");
                return null;
            }
            result.put("entityData", listDepository);
            *//*result.put("dev_count", dev_count);*//*
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return result;
    }*/

}

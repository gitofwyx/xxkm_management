package com.xxkm.management.system.bar_code.controller;

import com.xxkm.core.file.BaseController;
import com.xxkm.core.util.DateUtil;
import com.xxkm.management.device.entity.Device;
import com.xxkm.management.device.service.DeviceService;
import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.storage.entity.Delivery;
import com.xxkm.management.storage.service.DeliveryReportService;
import com.xxkm.management.system.bar_code.entity.Bar_code;
import com.xxkm.management.system.bar_code.service.Bar_codeService;
import com.xxkm.management.system.bar_code.service.StartPrintCheckOutService;
import com.xxkm.management.system.bar_code.service.impl.StartPrintCheckOut;
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
import java.util.function.Supplier;

/**
 * Created by Administrator on 2017/3/15.
 */
@Controller
@RequestMapping("")
public class Bar_codeController extends BaseController {

    private static Logger log = Logger.getLogger(Bar_codeController.class);

    private Supplier<Bar_code> bar_codeStorySupplier = Bar_code::new;

    @Autowired
    private Bar_codeService bar_codeService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeliveryReportService deliveryReportService;

    @Autowired
    private StartPrintCheckOutService startPrintCheckOutService;
    
    @ResponseBody
    @RequestMapping("/bar_code")
    public Map<String, Object> listBar_code(@RequestParam(value = "pageIndex") String pageIndex,
                                          @RequestParam(value = "limit") String limit,
                                          @RequestParam(value = "dev_name") String dev_name,
                                          @RequestParam(value = "dev_type") String dev_type,
                                          @RequestParam(value = "startDate") String startDate) {
        Map<String, Object> result = new HashMap<>();
        try {
            int pageNumber = Integer.parseInt(pageIndex) + 1;//??????pageindex ???0??????
            int pageSize = Integer.parseInt(limit);

            List<Bar_code> listDevice = bar_codeService.listDevice(pageNumber, pageSize);
            if (listDevice == null) {
                log.error("??????????????????");
                result.put("success", false);
                return result;
            } else {
                result.put("rows", listDevice);
                result.put("results", bar_codeService.countDevice());
            }
        } catch (Exception e) {
            log.error(e);
            result.put("success", false);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/updateBar_code")
    public Map<String, Object> updateBar_code(
                                           @RequestParam(value = "bar_code_ident") String bar_code_ident,
                                           @RequestParam(value = "entity_id") String entity_id) {
        Map<String, Object> result = new HashMap<>();
        try {
            String CurrentUserId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");

            if (entity_id != null && !"".equals(entity_id)) {
                Bar_code bar_code = bar_codeStorySupplier.get();
                bar_code.setBar_code_ident(bar_code_ident);
                bar_code.setStock_entity_id(bar_code_ident);
                result = bar_codeService.updateBar_codeForDevice(bar_code);
            } else {
                result.put("hasError", true);
                result.put("error", "updateStock:??????stock_record_id??????");
                log.error("updateStock:" + result.get("error"));
            }
        } catch (Exception e) {
            result.put("hasError", true);
            result.put("error", "?????????????????????"+e.getCause().getLocalizedMessage());
            log.error(e);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getDeviceByBar_code", method = RequestMethod.GET)
    public Map<String, Object> getDeviceByBar_code(
            @RequestParam(value = "bar_code_ident") String bar_code_ident) {
        Map<String, Object> result = new HashMap<>();
        try {
            String CurrentUserId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");

            if (bar_code_ident != null && !"".equals(bar_code_ident)) {
                Device device = deviceService.getDeviceByBar_code(bar_code_ident);
                if(device==null||"".equals(device.getId())){
                    result.put("status", 101);
                    result.put("errorMSG", "updateStock:??????stock_record_id??????");
                    return result;
                }
                result.put("data",device);
            } else {
                result.put("status", 101);
                result.put("errorMSG", "updateStock:??????stock_record_id??????");
                log.error("updateStock:" + result.get("error"));
            }
        } catch (Exception e) {
            result.put("status", 101);
            result.put("errorMSG", "updateStock:??????stock_record_id??????");
            log.error(e);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/TestPrintJ")
    public Map<String, Object> testPrintJ() {
        Map<String, Object> result = new HashMap<>();
        try {
            String createDate = DateUtil.getFullTime();
            String startDate= DateUtil.getMonthStartDate(createDate);
            String endDate=createDate;
            ArrayList<Map<String, Object>> listDelivery=(ArrayList)deliveryReportService.getDeliveryReportSingleParam(startDate,endDate);
            startPrintCheckOutService.printText2Action(listDelivery);
        } catch (Exception e) {
            result.put("hasError", true);
            result.put("error", "?????????????????????"+e.getCause().getLocalizedMessage());
            log.error(e);
        }

        return result;
    }

}

package com.xxkm.management.storage.controller;

import com.xxkm.core.file.BaseController;
import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.JsonUtils;
import com.xxkm.management.stock.service.StockService;
import com.xxkm.management.storage.entity.Delivery;
import com.xxkm.management.storage.entity.Storage;
import com.xxkm.management.storage.service.DeliveryService;
import com.xxkm.management.user.service.RebUserService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Controller
@RequestMapping("")
public class DeliveryController extends BaseController {

    private static Logger log = Logger.getLogger(DeliveryController.class);

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private RebUserService rebUserService;

    @Autowired
    private StockService stockService;


    @ResponseBody
    @RequestMapping("/listDelivery")
    public Map<String, Object> listDelivery(@RequestParam(value = "pageIndex") String pageIndex,
                                            @RequestParam(value = "limit") String limit,
                                            @RequestParam(value = "account") String account,
                                            @RequestParam(value = "name") String name,
                                            @RequestParam(value = "out_confirmed_date") String out_confirmed_date) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<String> userIdList = new ArrayList<>();
        List<Map<String, Object>> userMapList = new ArrayList<>();
        try {
            int pageNumber = Integer.parseInt(pageIndex) + 1;//???????????????pageindex ???0????????????1
            int pageSize = Integer.parseInt(limit);         //???????????????
            List<Delivery> listDelivery = deliveryService.listDelivery(pageNumber, pageSize);
            if (listDelivery == null) {
                log.error("listDelivery:??????????????????");
                result.put("error", false);
                return result;
            } else {

            }
            result.put("rows", resultList);
            result.put("results", deliveryService.countDelivery());
            result.put("userMapList", userMapList);

        } catch (Exception e) {
            log.error(e);
            result.put("hasError", true);
            result.put("error", "????????????");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/deliveryReport")
    public Map<String, Object> deliveryReport(HttpServletRequest request,
                                              HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String startDate=DateUtil.getMonthStartDate(createDate);
        String endDate=createDate;
        try {
            Map<String, String[]> pramMap = request.getParameterMap();
            Map<String, Object> objectMap = new HashMap<>();
            for (String key : pramMap.keySet()) {

                String[] pramChar = pramMap.get(key);
                StringBuffer pramBuffer = new StringBuffer();
                for (int i = 0; i < pramChar.length; i++) {
                    pramBuffer.append(pramChar[i]);
                }
                String pram = pramBuffer.toString();
                objectMap.put(key, pram);
            }
            if(!"".equals(objectMap.get("date"))&&objectMap.get("date")!=null){
                String Day=DateUtil.getFullDay();
                startDate=DateUtil.getPreMonthStartDate(Day)+" 00:00:00";
                endDate=DateUtil.getPreMonthEndDate(Day)+" 23:59:59";
            }
            if(!"".equals(objectMap.get("startdate"))&&objectMap.get("startdate")!=null){
                startDate=(String) objectMap.get("startdate");
            }
            if(!"".equals(objectMap.get("enddate"))&&objectMap.get("enddate")!=null){
                endDate=(String) objectMap.get("enddate");
            }

            resultMap = deliveryService.deliveryReport(startDate,endDate);
            if (resultMap == null) {
                log.error("listDelivery:??????????????????");
                return resultMap;
            }

        } catch (Exception e) {
            log.error(e);
            resultMap.put("hasError", true);
            resultMap.put("error", "????????????");
        }
        return resultMap;
    }

    @ResponseBody
    @RequestMapping("/listDeliveryByStock")
    public Map<String, Object> listDeliveryByStock(@RequestParam(value = "stock_id") String stock_id,
                                                   @RequestParam(value = "pageIndex") String pageIndex,
                                                   @RequestParam(value = "limit") String limit,
                                                   @RequestParam(value = "dev_ident") String dev_ident,
                                                   @RequestParam(value = "out_confirmed_date") String out_confirmed_date) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<String> userIdList = new ArrayList<>();
        List<Map<String, Object>> userMapList = new ArrayList<>();
        try {
            int pageNumber = Integer.parseInt(pageIndex) + 1;//???????????????pageindex ???0????????????1
            int pageSize = Integer.parseInt(limit);         //???????????????
            List<Delivery> listDelivery = deliveryService.listDeliveryByStock(pageNumber, pageSize, null, null, stock_id,null);
            if (listDelivery == null) {
                log.error("listDeliveryByStock:??????????????????");
                result.put("error", false);
                return result;
            } else {
                for (Delivery delivery : listDelivery) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.putAll(JsonUtils.toMap(delivery));
                    resultList.add(resultMap);
                    userIdList.add(delivery.getOut_confirmed_by());
                }
                if (!userIdList.isEmpty()) {
                    userMapList = rebUserService.listRegUserByIds(userIdList);
                }
            }
            result.put("rows", resultList);
            result.put("results", deliveryService.countDelivery());
            result.put("userMapList", userMapList);

        } catch (Exception e) {
            log.error(e);
            result.put("hasError", true);
            result.put("error", "????????????");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/listDeliveryByOffice")
    public Map<String, Object> listDeliveryByOffice(@RequestParam(value = "office_id") String office_id) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            List<Delivery> listDelivery = deliveryService.listDeliveryByOffice(1, 9, office_id);
            if (listDelivery == null) {
                log.error("listDeliveryByOffice:??????????????????");
                result.put("error", false);
                return result;
            } else {
                for (Delivery delivery : listDelivery) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.putAll(JsonUtils.toMap(delivery));
                    resultList.add(resultMap);
                }
            }
            result.put("entityData", resultList);
            result.put("results", 9);

        } catch (Exception e) {
            log.error(e);
            result.put("hasError", true);
            result.put("error", "????????????");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("/listDeliveryUNIONStorageByOffice")
    public Map<String, Object> listDeliveryUNIONStorageByOffice(@RequestParam(value = "office_id") String office_id) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            List<Delivery> listDelivery = deliveryService.listDeliveryUNIONStorageByOffice(1, 9, office_id);
            if (listDelivery == null) {
                log.error("listDeliveryByOffice:??????????????????");
                result.put("error", false);
                return result;
            } else {
                for (Delivery delivery : listDelivery) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.putAll(JsonUtils.toMap(delivery));
                    resultList.add(resultMap);
                }
            }
            result.put("entityData", resultList);
            result.put("results", 9);

        } catch (Exception e) {
            log.error(e);
            result.put("hasError", true);
            result.put("error", "????????????");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/backwardDelivery", method = RequestMethod.POST)
    public Map<String, Object> backwardDelivery(Storage storage,Delivery delivery,
                                                @RequestParam(value = "delivery_id") String delivery_id,
                                                @RequestParam(value = "stock_no") String stock_no) {
        Map<String, Object> result = new HashMap<>();
        try {

            String CurrentUserId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
            delivery.setUpdateUserId(CurrentUserId);
            if ( !"".equals(delivery_id)&&delivery_id != null ) {
                delivery.setId(delivery_id);
                result = stockService.updateStockForBackward( storage,delivery,stock_no);

            } else {
                result.put("hasError", true);
                result.put("error", "backwardDelivery:????????????-delivery_id??????");
            }

        } catch (Exception e) {
            log.error(e);
            result.put("hasError", true);
            result.put("error", "????????????");
        }
        return result;
        //return "system/index";
    }
}

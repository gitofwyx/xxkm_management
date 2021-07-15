package com.xxkm.management.storage.service.impl;

import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.core.util.build_ident.IdentUtil;
import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.storage.dao.DeliveryDao;
import com.xxkm.management.storage.entity.Delivery;
import com.xxkm.management.storage.service.DeliveryReportService;
import com.xxkm.management.storage.service.DeliveryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Service
public class DeliveryServiceImpl implements DeliveryService {


    private static Logger log = Logger.getLogger(DeliveryServiceImpl.class);

    @Autowired
    private DeliveryDao dao;

    @Autowired
    private DeliveryReportService deliveryReportService;

    @Override
    public List<Delivery> listDelivery(int pageStart, int pageSize) {
        return dao.listDelivery((pageStart - 1) * pageSize, pageSize);
    }

    @Override
    public int countDelivery() {
        return dao.countDelivery();
    }

    @Override
    public List<Delivery> listDeliveryByStock(int pageStart, int pageSize, String class_id, String entity_id, String stock_id, String officeId) {
        return dao.listDeliveryByStock((pageStart - 1) * pageSize, pageSize, class_id, entity_id, stock_id, officeId);
    }

    @Override
    public List<Delivery> listDeliveryByOffice(int pageStart, int pageSize, String stock_id) {
        return dao.listDeliveryByOffice((pageStart - 1) * pageSize, pageSize, stock_id);
    }

    @Override
    public List<Delivery> listDeliveryUNIONStorageByOffice(int pageStart, int pageSize, String stock_id) {
        return dao.listDeliveryUNIONStorageByOffice((pageStart - 1) * pageSize, pageSize, stock_id);
    }

    @Override
    public Map<String, Object> addDelivery(Stock stock, Delivery delivery, String status) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String deliveryId = UUIdUtil.getUUID();
        try {
            if ("".equals(delivery.getEntity_id()) || delivery.getEntity_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错！无法获取设备ID");
                return result;
            }
            //库存编号生成
            String out_confirmed_ident = IdentUtil.getIdentNo((int) stock.getStock_no(), createDate);
            if ("".equals(out_confirmed_ident) || out_confirmed_ident == null) {
                result.put("hasError", true);
                result.put("error", "添加出错,无法生成入库编号！");
                return result;
            }
            //
            delivery.setId(deliveryId);
            delivery.setStock_id(stock.getId());
            delivery.setOut_confirmed_ident(out_confirmed_ident);
            delivery.setOut_confirmed_type(stock.getStock_type());
            delivery.setOut_confirmed_genre("1");
            delivery.setOut_confirmed_by(stock.getUpdateUserId());
            delivery.setOut_confirmed_unit(stock.getStock_unit());
            delivery.setOut_confirmed_proportion(stock.getStock_proportion());
            delivery.setEntity_entry_status(status);
            delivery.setCreateDate(createDate);
            delivery.setCreateUserId(stock.getUpdateUserId());
            delivery.setUpdateDate(createDate);
            delivery.setUpdateUserId(stock.getUpdateUserId());
            delivery.setDeleteFlag("0");

            boolean storageResult = dao.addDelivery(delivery) == 1 ? true : false;
            ;
            if (!(storageResult)) {
                log.error("addstorage:" + storageResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                //log.info(">>>>保存成功");
                result.put("success", true);
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
        return result;
    }

    @Override
    public Map<String, Object> addDelivery(Delivery delivery, String status) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String deliveryId = UUIdUtil.getUUID();
        try {
            if ("".equals(delivery.getEntity_id()) || delivery.getEntity_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错！无法获取设备ID");
                return result;
            }

            //c库
            delivery.setId(deliveryId);
            delivery.setOut_confirmed_genre("1");
            delivery.setEntity_entry_status(status);
            delivery.setDeleteFlag("0");

            boolean storageResult = dao.addDelivery(delivery) == 1 ? true : false;
            ;
            if (!(storageResult)) {
                log.error("addstorage:" + storageResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                //log.info(">>>>保存成功");
                result.put("success", true);
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
        return result;
    }


    @Override
    public boolean updateDeliveryStatus(String id) {
        return dao.updateDeliveryStatus(id) == 1 ? true : false;
    }

    @Override
    public Map<String, Object> deliveryReport(String startDate,String endDate) {

        Map<String, Object> resultMap = new HashMap<>();
        try{
            List<Map<String, Object>> listDelivery=deliveryReportService.getDeliveryReportSingleParam(startDate,endDate);
            resultMap.put("data", listDelivery);
        }catch (Exception e) {
            resultMap.put("hasError", true);
            resultMap.put("error", "查询出错");
            log.error(e);
        }
        return resultMap;
    }
}

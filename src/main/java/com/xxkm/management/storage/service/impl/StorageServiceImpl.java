package com.xxkm.management.storage.service.impl;

import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.core.util.build_ident.IdentUtil;
import com.xxkm.management.device.entity.Device;
import com.xxkm.management.office.devices.entity.Devices;
import com.xxkm.management.office.storage.entity.OfficesStorage;
import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.storage.entity.Delivery;
import com.xxkm.management.storage.service.StorageService;
import com.xxkm.management.storage.dao.StorageDao;
import com.xxkm.management.storage.entity.Storage;
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
public class StorageServiceImpl implements StorageService {

    private static Logger log = Logger.getLogger(StorageServiceImpl.class);

    @Autowired
    private StorageDao dao;


    @Override
    public List<Storage> listStorage(int pageStart, int pageSize) {
        return dao.listStorage((pageStart - 1) * pageSize, pageSize);
    }

    @Override
    public int countStorage() {
        return dao.countStorage();
    }

    @Override
    public List<Storage> listStorageByStock(int pageStart, int pageSize, String class_id, String entity_id, String stock_id, String officeId) {
        return dao.listStorageByStock((pageStart - 1) * pageSize, pageSize, class_id, entity_id, stock_id, officeId);
    }

    //入库记录
    @Override
    public Map<String, Object> addStorage(Stock stock, Storage storage) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String storageId = UUIdUtil.getUUID();
        try {
            if ("".equals(storage.getEntity_id()) || storage.getEntity_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }
            String in_confirmed_ident = IdentUtil.getIdentNo((int)storage.getIn_confirmed_no(), createDate);
            if ("".equals(in_confirmed_ident) || in_confirmed_ident == null) {
                result.put("hasError", true);
                result.put("error", "添加出错,无法生成入库编号！");
                return result;
            }
            //入库
            storage.setId(storageId);
            storage.setStock_id(stock.getId());
            storage.setIn_confirmed_ident(in_confirmed_ident);
            storage.setIn_confirmed_type(stock.getStock_type());
            storage.setIn_confirmed_by(stock.getUpdateUserId());
            storage.setOut_flag("0");
            storage.setCreateDate(createDate);
            storage.setCreateUserId(stock.getUpdateUserId());
            storage.setUpdateDate(createDate);
            storage.setUpdateUserId(stock.getUpdateUserId());
            storage.setDeleteFlag("0");

            boolean storageResult = dao.addStorage(storage) == 1 ? true : false;
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

    //入库记录
    @Override
    public Map<String, Object> addStorage(Delivery delivery, Storage storage) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String storageId = UUIdUtil.getUUID();
        try {
            if ("".equals(storage.getEntity_id()) || storage.getEntity_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }
            String in_confirmed_ident = IdentUtil.getIdentNo((int)storage.getIn_confirmed_no(), createDate);
            if ("".equals(in_confirmed_ident) || in_confirmed_ident == null) {
                result.put("hasError", true);
                result.put("error", "添加出错,无法生成入库编号！");
                return result;
            }
            //入库
            storage.setId(storageId);
            storage.setStock_id(delivery.getStock_id());
            storage.setIn_confirmed_ident(in_confirmed_ident);
            storage.setIn_confirmed_type(delivery.getOut_confirmed_type());
            storage.setIn_confirmed_by(delivery.getUpdateUserId());
            storage.setOut_flag("0");
            storage.setCreateDate(createDate);
            storage.setCreateUserId(delivery.getUpdateUserId());
            storage.setUpdateDate(createDate);
            storage.setUpdateUserId(delivery.getUpdateUserId());
            storage.setDeleteFlag("0");

            boolean storageResult = dao.addStorage(storage) == 1 ? true : false;
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

    //入库记录
    @Override
    public Map<String, Object> addStorageForOfficesStorage(OfficesStorage storage) {
        Map<String, Object> result = new HashMap<>();
        String storageId = UUIdUtil.getUUID();
        try {
            if ("".equals(storage.getEntity_id()) || storage.getEntity_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }
            String in_confirmed_ident = IdentUtil.getIdentNo((int)storage.getOffices_storage_no(), storage.getCreateDate());
            if ("".equals(in_confirmed_ident) || in_confirmed_ident == null) {
                result.put("hasError", true);
                result.put("error", "添加出错,无法生成入库编号！");
                return result;
            }
            //入库
            storage.setId(storageId);
            storage.setOffices_storage_ident(in_confirmed_ident);
            storage.setEntity_entry_status("0");
            storage.setDeleteFlag("0");

            boolean storageResult = dao.addStorageForOfficesStorage(storage) == 1 ? true : false;
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
    public boolean deleteListRegUser(List<String> listStr) {
        return false;
    }

    @Override
    public List<String> getStorageIdByIdent(String ident) {
        return dao.getStorageIdByIdent(ident);
    }
}

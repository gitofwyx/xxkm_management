package com.xxkm.management.stock.service.impl;

import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.management.device.dao.DeviceDao;
import com.xxkm.management.device.entity.Device;
import com.xxkm.management.device.service.DeviceService;
import com.xxkm.management.material.service.MaterialService;
import com.xxkm.management.office.devices.entity.Devices;
import com.xxkm.management.office.storage.entity.OfficesStorage;
import com.xxkm.management.stock.dao.StockDao;
import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.storage.entity.Delivery;
import com.xxkm.management.storage.entity.Storage;
import com.xxkm.management.stock.service.StockService;
import com.xxkm.management.storage.service.DeliveryService;
import com.xxkm.management.storage.service.StorageService;
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
public class StockServiceImpl implements StockService {

    private static Logger log = Logger.getLogger(StockService.class);

    @Autowired
    private StockDao dao;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private DeliveryService deliveryService;

    @Override
    public List<Stock> listStock(int pageStart, int pageSize, String class_id, String entity_id, String stock_office_id, String search_type) {
        return dao.listStock((pageStart - 1) * pageSize, pageSize, class_id, entity_id, stock_office_id, search_type);
    }

    @Override
    public int countStock(String search_type) {
        return dao.countStock(search_type);
    }

    @Override
    public List<Stock> listStockByEntityId(String entity_id, String office_id) {
        return dao.listStockByEntityId(entity_id, office_id);
    }

    @Override
    public List<Stock> getStocksByEntityId(String entity_id) {
        return dao.getStocksByEntityId(entity_id);
    }

    //新增库存
    // 2019年8月12日 13:44:05更新
    @Override
    public Map<String, Object> addStockWithStorage(Stock stock, Storage storage) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String stockId = UUIdUtil.getUUID();
        try {
            if ("".equals(stock.getEntity_id()) || stock.getEntity_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }
            if ("1".equals(stock.getStock_type())) {
                stock = deviceService.makeStockByDevice(stock);
            } else if ("2".equals(stock.getStock_type()) || ("3".equals(stock.getStock_type()))) {
                stock = materialService.makeStockByMaterial(stock);
            } else {
                stock = null;
            }
            if (stock != null) {
                stock.setId(stockId);
                stock.setStock_no(storage.getIn_confirmed_no());
                stock.setStock_total(storage.getIn_confirmed_total());
                stock.setStock_flag("1");
                stock.setCreateDate(createDate);
                stock.setCreateUserId(stock.getUpdateUserId());
                stock.setUpdateDate(createDate);
                stock.setUpdateUserId(stock.getUpdateUserId());
                stock.setDeleteFlag("0");
            } else {
                log.error("addDepositoryWithStorage:无法获许设备或耗材ID");
                result.put("hasError", true);
                result.put("error", "添加出错：无法获许设备或耗材ID");
                return result;
            }
            boolean stockResult = dao.addStock(stock) == 1 ? true : false;
            if (!(stockResult)) {
                log.error("stockResult:" + stockResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                storage.setEntity_id(stock.getEntity_id());
                storage.setClass_id(stock.getClass_id());
                result = storageService.addStorage(stock, storage);
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

    //入库操作
    // 2019年8月19日 13:44:05更新
    @Override
    public Map<String, Object> updateStockWithStorage(Stock stock, Storage storage) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        try {
            if ("".equals(stock.getEntity_id()) || stock.getEntity_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错！无法获取设备ID");
                return result;
            }
            stock.setStock_total(storage.getIn_confirmed_total());
            stock.setUpdateDate(createDate);
            stock.setUpdateUserId(stock.getUpdateUserId());

            boolean stockResult = dao.plusStockNo(stock) == 1 ? true : false;
            if (!(stockResult)) {
                log.error("stockResult:" + stockResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                //入库记录
                storage.setClass_id(stock.getClass_id());
                storage.setEntity_id(stock.getEntity_id());
                result = storageService.addStorage(stock, storage);
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

    //出库操作
    // 2019年8月19日 13:43:42更新
    @Override
    public Map<String, Object> updateStockWithDelivery(Stock stock, Delivery delivery) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        try {
            if ("".equals(stock.getEntity_id()) || stock.getEntity_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错！无法获取设备ID");
                return result;
            }
            stock.setStock_total(delivery.getOut_confirmed_total());
            stock.setUpdateDate(createDate);
            stock.setUpdateUserId(stock.getUpdateUserId());

            boolean stockResult = dao.reduceStockNo(stock) == 1 ? true : false;
            if (!(stockResult)) {
                log.error("stockResult:" + stockResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                //出库更新
                delivery.setClass_id(stock.getClass_id());
                delivery.setEntity_id(stock.getEntity_id());
                result = deliveryService.addDelivery(stock, delivery, "1");
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

    //配置出库操作
    // 2019年8月19日 13:43:42更新
    @Override
    public Map<String, Object> updateSingleStockWithDelivery(Delivery delivery, double stock_no) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        try {
            if ("".equals(delivery.getStock_id()) || delivery.getStock_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错！无法获取设备ID");
                return result;
            }
            boolean stockResult = dao.reduceSingleStockNo(delivery.getStock_id(), stock_no, delivery.getCreateUserId(), createDate) == 1 ? true : false;
            if (!(stockResult)) {
                log.error("stockResult:" + stockResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                //出库更新
                result = deliveryService.addDelivery(delivery, "0");
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

    //更新操作
    @Override
    public Map<String, Object> updateStock(Stock stock) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String stockId = UUIdUtil.getUUID();
        try {
            if ("".equals(stock.getEntity_id()) || stock.getEntity_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }
            boolean stockResult = dao.updateStock(stock, stock.getEntity_id()) == 1 ? true : false;
            if (!(stockResult)) {
                log.error("stockResult:" + stockResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
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

    //回收（新增库存）
    // 2019年8月12日 13:44:05更新
    @Override
    public Map<String, Object> addStockForRecovery(OfficesStorage officesStorage) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String stockId = UUIdUtil.getUUID();
        try {
            if ("".equals(officesStorage.getEntity_id()) || officesStorage.getEntity_id() == null) {
                log.info("recoveryStockWithStorage:出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }
            officesStorage.setId(stockId);
            officesStorage.setOffices_storage_ident("NO");
            officesStorage.setOffices_storage_total(1);
            officesStorage.setEntity_entry_status("1");
            officesStorage.setCreateDate(createDate);
            officesStorage.setUpdateDate(createDate);
            officesStorage.setDeleteFlag("0");

            boolean stockResult = dao.addStockForOfficesStorage(officesStorage) == 1 ? true : false;
            if (!(stockResult)) {
                log.error("stockResult:" + stockResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {

                result = storageService.addStorageForOfficesStorage(officesStorage);
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

    //回收操作
    // 2019年8月19日 13:44:05更新
    @Override
    public Map<String, Object> updateStockForRecovery(OfficesStorage officesStorage) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        try {
            if ("".equals(officesStorage.getEntity_id()) || officesStorage.getEntity_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错！无法获取设备ID");
                return result;
            }

            boolean stockResult = dao.updateStockForOfficesStorage(officesStorage) == 1 ? true : false;
            if (!(stockResult)) {
                log.error("stockResult:" + stockResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                //入库记录
                result = storageService.addStorageForOfficesStorage(officesStorage);
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

    //入库操作
    // 2019年8月19日 13:44:05更新
    @Override
    public Map<String, Object> updateStockForBackward(Storage storage,Delivery delivery,String stock_no) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        try {
            if ("".equals(delivery.getStock_id()) || delivery.getStock_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错！无法获取库存ID");
                return result;
            }
            delivery.setOut_confirmed_no_2(Double.valueOf(stock_no));
            delivery.setUpdateDate(createDate);
            delivery.setUpdateUserId(delivery.getUpdateUserId());

            boolean stockResult = dao.plusStockNoForDelivery(delivery) == 1 ? true : false;
            if (!(stockResult)) {
                log.error("stockResult:" + stockResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                //入库记录
                storage.setClass_id(delivery.getClass_id());
                storage.setEntity_id(delivery.getEntity_id());
                result = storageService.addStorage(delivery, storage);
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
    public boolean plusStockConfiguredTotal(String stockId, String userId, String date, String stock_version) {
        return dao.plusStockConfiguredTotal(stockId, userId, date, stock_version) == 1 ? true : false;
    }

    @Override
    public boolean deleteListRegUser(List<String> listStr) {
        return false;
    }

    @Override
    public List<String> getStorageIdByIdent(String ident) {
        return dao.getStockIdByIdent(ident);
    }

    @Override
    public Stock getStockById(String id) {
        return dao.getStockById(id);
    }
}

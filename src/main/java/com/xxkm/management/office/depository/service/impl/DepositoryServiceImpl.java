package com.xxkm.management.office.depository.service.impl;

import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.management.office.depository.dao.DepositoryDao;
import com.xxkm.management.office.depository.entity.Depository;
import com.xxkm.management.office.depository.service.DepositoryService;
import com.xxkm.management.office.devices.entity.Devices;
import com.xxkm.management.office.devices.service.DevicesService;
import com.xxkm.management.office.storage.entity.OfficesStorage;
import com.xxkm.management.office.storage.service.OfficesStorageService;
import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.stock.service.StockService;
import com.xxkm.management.storage.entity.Delivery;
import com.xxkm.management.storage.entity.Storage;
import com.xxkm.management.storage.service.DeliveryService;
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
public class DepositoryServiceImpl implements DepositoryService {

    private static Logger log = Logger.getLogger(DepositoryService.class);

    @Autowired
    private DepositoryDao dao;

    @Autowired
    private OfficesStorageService storageService;

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private StockService stockService;

    @Autowired
    private DevicesService devicesService;

    @Override
    public List<Depository> listDepository(int pageStart, int pageSize, String class_id, String entity_id, String depository_officeId, int search_type) {
        return dao.listDepository((pageStart - 1) * pageSize,pageSize,class_id,entity_id,depository_officeId,search_type);
    }

    @Override
    public List<Depository> selectDepository(String entity_id, String depository_officeId) {
        List<Depository> res = dao.selectDepositoryWithOfficeEnt(entity_id,depository_officeId);
        if(res.size() == 0) {
            return null;
        }
        return res;
    }

    @Override
    public int countDepository(String search_type) {
        return dao.countDepository(search_type);
    }

    @Override
    public Depository getDepositoryByEntId(String entity_id) {
        List<Depository> res = dao.getDepositoryByEntId(entity_id);
        if(res.size() == 0) {
            return null;
        }
        return res.get(0);
    }

    //新增库存
    // 2019年8月12日 13:44:05更新
    @Override
    public Map<String, Object> addDepositoryWithStorage(Depository depository, OfficesStorage storage) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String depositoryId = UUIdUtil.getUUID();
        try {
            /*if ("".equals(depository.getEntity_id()) || depository.getEntity_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }*/
            boolean Result =deliveryService.updateDeliveryStatus(depository.getDelivery_id());
            if(!Result){
                log.error("addDepositoryWithStorage:deliveryService:allEntryDepository错误！");
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }
            if("0".equals(storage.getEntity_entry_status())){
                boolean devicesResult=devicesService.updateDevicesStatus(storage.getOffices_entity_id(),
                        depository.getDepository_officeId(),
                        depositoryId,
                        "2",
                        depository.getUpdateUserId(),createDate);
                if (!(devicesResult)) {
                    result.put("hasError", true);
                    result.put("error", "添加出错");
                    return result;
                }
            }
            depository.setId(depositoryId);
            depository.setDepository_ident("NO");
            depository.setDepository_no(storage.getOffices_storage_total());
            depository.setDepository_idle_no(storage.getOffices_storage_total());
            depository.setDepository_total(storage.getOffices_storage_total());
            depository.setDepository_idle_total(storage.getOffices_storage_total());
            depository.setDepository_flag("1");
            depository.setCreateDate(createDate);
            depository.setCreateUserId(depository.getUpdateUserId());
            depository.setUpdateDate(createDate);
            //depository.setUpdateUserId(depository.getUpdateUserId());
            depository.setDeleteFlag("0");
            boolean depositoryResult = dao.addDepository(depository) == 1 ? true : false;
            if (!(depositoryResult)) {
                log.error("depositoryResult:" + depositoryResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                storage.setClass_id(depository.getClass_id());
                storage.setEntity_id(depository.getEntity_id());
                storage.setOffices_storage_total(depository.getDepository_total());
                result = storageService.addOfficesStorage(depository, storage,"3");//“1”代表入科标记
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
    public Map<String, Object> updateDepositoryWithStorage(Depository depository, OfficesStorage storage) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        try {
           /* if ("".equals(depository.getEntity_id()) || depository.getEntity_id() == null) {
                log.info("出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错！无法获取设备ID");
                return result;
            }*/
            if (!"".equals(depository.getDelivery_id())&&depository.getDelivery_id()!=null) {
                //不是原科登记更新出库记录的状态
                boolean Result =deliveryService.updateDeliveryStatus(depository.getDelivery_id());
                if(!Result){
                    log.error("updateDepositoryWithStorage:deliveryService:allEntryDepository错误！");
                    result.put("hasError", true);
                    result.put("error", "添加出错");
                    return result;
                }
            }
            if("0".equals(storage.getEntity_entry_status())){
                boolean devicesResult=devicesService.updateDevicesStatus(storage.getOffices_entity_id(),
                        depository.getDepository_officeId(),
                        depository.getId(),
                        "2",
                        depository.getUpdateUserId(),createDate);
                if (!(devicesResult)) {
                    result.put("hasError", true);
                    result.put("error", "添加出错");
                    return result;
                }
            }
            depository.setDepository_total(storage.getOffices_storage_total());
            depository.setDepository_idle_total(storage.getOffices_storage_total());
            depository.setUpdateDate(createDate);

            boolean stockResult = dao.plusDepositoryNo(depository) == 1 ? true : false;
            if (!(stockResult)) {
                log.error("stockResult:" + stockResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
            } else {
                //入库记录
                storage.setClass_id(depository.getClass_id());
                storage.setEntity_id(depository.getEntity_id());
                result = storageService.addOfficesStorage(depository, storage,"3");//“3”代表入科标记
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
    @Override
    public Map<String, Object> recoveryDepository(Depository depository, Stock stock, Storage storage) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String stockId = UUIdUtil.getUUID();
        try {
            if ("".equals(depository.getEntity_id()) || depository.getEntity_id() == null) {
                log.info("recoveryDepository:出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }
            depository.setDepository_total(storage.getIn_confirmed_total());
            depository.setDepository_idle_total(storage.getIn_confirmed_total());
            depository.setUpdateDate(createDate);
            boolean depositoryResult = dao.recoveryDepository(depository) == 1 ? true : false;
            if (!(depositoryResult)) {
                log.error("depositoryResult:" + depositoryResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }
            if ("".equals(stock.getId()) || stock.getId() == null) {
                result = stockService.addStockWithStorage(stock, storage);
            } else {
                result = stockService.updateStockWithStorage(stock, storage);
            }
            if(result.get("hasError")==true){
                throw new Exception("error");
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

    //转移库存(入库)
    @Override
    public Map<String, Object> transferDepositoryForStorage(Devices devices, OfficesStorage storage) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String stockId = UUIdUtil.getUUID();
        try {
            if ("".equals(devices.getDevice_id()) || devices.getDevice_id() == null) {
                log.info("transferDepositoryForStorage:出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }

            boolean depositoryResult = dao.transferDepositoryForDelivery(storage) == 1 ? true : false;
            if (!(depositoryResult)) {
                log.error("depositoryResult:" + depositoryResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }
            //入库记录
            storage.setEntity_id(devices.getDevice_id());
            result = storageService.addOfficesStorage(devices, storage);//“1”代表入科标记
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

    //转移库存(出库)
    //Delivery为出库的意思，跟方法形参没有关系
    @Override
    public Map<String, Object> transferDepositoryForDelivery(Devices devices, OfficesStorage storage) {
        Map<String, Object> result = new HashMap<>();
        String createDate = DateUtil.getFullTime();
        String stockId = UUIdUtil.getUUID();
        try {
            if ("".equals(devices.getDevice_id()) || devices.getDevice_id() == null) {
                log.info("transferDepositoryForDelivery:出错！无法获取设备ID");
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }

            boolean depositoryResult = dao.transferDepositoryForDelivery(storage) == 1 ? true : false;
            if (!(depositoryResult)) {
                log.error("depositoryResult:" + depositoryResult);
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            }
            //入库记录
            storage.setEntity_id(devices.getDevice_id());
            result = storageService.addOfficesStorage(devices, storage);//“1”代表入科标记
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

    //部署操作（库存减一）；标注时间：2021年6月17日 23:53:48
    @Override
    public boolean deploymentDeviceWithSingle(String depository_id, String updateUserId, String updateDate) {
        return dao.deploymentDeviceWithSingle(depository_id,updateUserId,updateDate)==1 ? true : false;
    }


    @Override
    public List<String> getDepositoryIdByIdent(String ident) {
        return dao.getDepositoryIdByIdent(ident);
    }

    @Override
    public Depository getDepositoryById(String id) {
        return dao.getDepositoryById(id);
    }
}

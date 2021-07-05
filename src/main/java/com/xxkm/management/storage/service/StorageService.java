package com.xxkm.management.storage.service;

import com.xxkm.management.device.entity.Device;
import com.xxkm.management.office.devices.entity.Devices;
import com.xxkm.management.office.storage.entity.OfficesStorage;
import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.storage.entity.Delivery;
import com.xxkm.management.storage.entity.Storage;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
public interface StorageService {

    public List<Storage> listStorage(int pageStart, int pageSize);

    public int countStorage();

    public  List<Storage> listStorageByStock(int pageStart, int pageSize, String class_id, String entity_id, String stock_id, String officeId);

    public Map<String, Object> addStorage(Stock stock, Storage storage);

    public Map<String, Object> addStorage(Delivery delivery, Storage storage);

    public Map<String, Object> addStorageForOfficesStorage(OfficesStorage storage);

    public boolean deleteListRegUser(List<String> listStr);

    public List<String> getStorageIdByIdent(String ident);

}

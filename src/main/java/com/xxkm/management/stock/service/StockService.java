package com.xxkm.management.stock.service;

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
public interface StockService {

    public List<Stock> listStock(int pageStart, int pageSize,String class_id,String entity_id,String stock_office_id,String search_type);

    public int countStock(String search_type);

    public List<Stock> listStockByEntityId(String entity_id,String office_id);

    public List<Stock> getStocksByEntityId(String entity_id);

    public Map<String, Object> addStockWithStorage(Stock stock,Storage storage);

    public Map<String, Object> updateStockWithStorage(Stock stock,Storage storage);

    public Map<String, Object> updateStockWithDelivery(Stock stock, Delivery delivery);

    public Map<String, Object> updateSingleStockWithDelivery(Delivery delivery,double stock_no) ;

    public Map<String, Object> updateStock(Stock stock);

    public Map<String, Object> addStockForRecovery(OfficesStorage officesStorage);

    public Map<String, Object> updateStockForRecovery(OfficesStorage officesStorage);

    public Map<String, Object> updateStockForBackward(Storage storage,Delivery delivery,String stock_no);

    public boolean plusStockConfiguredTotal(String stockId,String userId,String date,String stock_version);

    public boolean deleteListRegUser(List<String> listStr);

    public List<String> getStorageIdByIdent(String ident);

    public Stock getStockById(String id);

}

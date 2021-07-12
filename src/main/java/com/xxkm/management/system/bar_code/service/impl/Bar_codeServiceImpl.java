package com.xxkm.management.system.bar_code.service.impl;

import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.system.bar_code.dao.Bar_codeDao;
import com.xxkm.management.system.bar_code.entity.Bar_code;
import com.xxkm.management.system.bar_code.service.Bar_codeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Service
public class Bar_codeServiceImpl implements Bar_codeService {

    private static Logger log = Logger.getLogger(Bar_codeServiceImpl.class);

    @Autowired
    private Bar_codeDao dao;


    @Override
    public List<Bar_code> listDevice(int pageStart, int pageSize) {
        return dao.listDevice((pageStart-1)*pageSize, pageSize);
    }

    @Override
    public int countDevice() {
        return dao.countDevice();
    }

    @Override
    public List<Bar_code> listDeviceById(List<String> listDevId) {
        return dao.listDeviceById(listDevId);
    }

    @Override
    public boolean addDevice(Bar_code device) {
        return dao.addDevice(device)==1?true:false;
    }

    @Override
    public Bar_code getDeviceById(String deviceId) {
        return dao.getDeviceById(deviceId);
    }

    @Override
    public List<Map<String, Object>> getDeviceNumber(String deviceId) {
        return dao.getDeviceNumber(deviceId);
    }

    @Override
    public  List<Map<String, Object>> getDeviceSelect() {
        return dao.getDeviceSelect();
    }

    @Override
    public List<Map<String, Object>> getStoreDeviceById(List<String> listDevId) {
        return dao.getStoreDeviceById(listDevId);
    }

    @Override
    public List<Map<String, Object>> getDeviceIdent() {
        if(dao.getDeviceIdent().size()!=1){
            log.error("getDeviceIdent:获取设备编号错误");
            return null;
        }
        return dao.getDeviceIdent();
    }

    @Override
    public Stock makeStockByDevice(Stock stock) {
        return null;
    }

}

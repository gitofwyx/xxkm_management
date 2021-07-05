package com.xxkm.management.material.service.impl;

import com.xxkm.management.material.dao.MaterialDao;
import com.xxkm.management.material.entity.Material;
import com.xxkm.management.material.service.MaterialService;
import com.xxkm.management.stock.entity.Stock;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Service
public class MaterialServiceImpl implements MaterialService {

    private static Logger log = Logger.getLogger(MaterialServiceImpl.class);

    @Autowired
    private MaterialDao dao;


    @Override
    public List<Material> listMaterial(int pageStart, int pageSize) {
        return dao.listMaterial((pageStart-1)*pageSize, pageSize);
    }

    @Override
    public int countMaterial() {
        return dao.countMaterial();
    }

    @Override
    public List<Material> listMaterialById(List<String> listMatId) {
        return dao.listMaterialById(listMatId);
    }

    @Override
    public boolean addMaterial(Material material) {
        return dao.addMaterial(material)==1?true:false;
    }

    @Override
    public Material getMaterialById(String id){
        return dao.getMaterialById(id);
    }

    @Override
    public List<Map<String, Object>> getMaterialNumber(String materialId) {
        return null;
    }

    @Override
    public  List<Map<String, Object>> getMaterialSelect(String tab) {
        return dao.getMaterialSelect(tab);
    }

    @Override
    public List<Map<String, Object>> getStoreMaterialById(List<String> listMatId) {
        return dao.getStoreMaterialById(listMatId);
    }

    @Override
    public List<Map<String, Object>> getMaterialByIdent(List<String> listDevId) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getDeviceIdent() {
        return null;
    }


    @Override
    public boolean plusMaterialNumber(int dev_no,String deviceId) {
        return dao.plusMaterialNumber(dev_no,deviceId)==1?true:false;
    }

    @Override
    public boolean minusMaterialNumber(int dev_no, String deviceId) {
        return dao.minusMaterialNumber(dev_no,deviceId)==1?true:false;
    }

    public Stock makeStockByMaterial(Stock stock){

        if ("".equals(stock.getEntity_id()) || stock.getEntity_id() == null) {
            return null;
        }
        Material material=dao.getMaterialById(stock.getEntity_id());
        if (material != null) {
            stock.setStock_ident(material.getMat_ident());
            stock.setClass_id(material.getDev_class_id());
            stock.setEntity_id(material.getId());
            stock.setCreateUserId(stock.getUpdateUserId());
            stock.setUpdateUserId(stock.getUpdateUserId());
        }
        return stock;
    }

}

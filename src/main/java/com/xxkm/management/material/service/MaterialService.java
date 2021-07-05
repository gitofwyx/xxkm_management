package com.xxkm.management.material.service;

import com.xxkm.management.material.entity.Material;
import com.xxkm.management.stock.entity.Stock;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
public interface MaterialService {

    public List<Material> listMaterial(int pageStart, int pageSize);

    public int countMaterial();

    public List<Material> listMaterialById(List<String> listMatId);

    public boolean addMaterial(Material material);

    public Material getMaterialById(String id);

    public List<Map<String, Object>> getMaterialNumber(String materialId);

    public List<Map<String, Object>> getMaterialSelect(String tab);

    public List<Map<String, Object>> getStoreMaterialById(List<String> listMatId);

    public List<Map<String, Object>> getMaterialByIdent(List<String> listMatId);

    public List<Map<String, Object>> getDeviceIdent();  //获取设备编号

    public boolean plusMaterialNumber(int dev_no, String materialId);

    public boolean minusMaterialNumber(int dev_no, String materialId);

    public Stock makeStockByMaterial(Stock stock);

}

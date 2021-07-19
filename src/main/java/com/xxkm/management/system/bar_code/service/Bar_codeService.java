package com.xxkm.management.system.bar_code.service;

import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.system.bar_code.entity.Bar_code;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
public interface Bar_codeService {

    public List<Bar_code> listDevice(int pageStart, int pageSize);

    public int countDevice();

    public Map<String, Object> updateBar_codeForDevice(Bar_code bar_code) ;

}

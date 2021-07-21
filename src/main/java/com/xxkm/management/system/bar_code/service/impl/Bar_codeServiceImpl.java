package com.xxkm.management.system.bar_code.service.impl;

import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.management.device.entity.Device;
import com.xxkm.management.device.service.DeviceService;
import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.storage.entity.Storage;
import com.xxkm.management.system.bar_code.dao.Bar_codeDao;
import com.xxkm.management.system.bar_code.entity.Bar_code;
import com.xxkm.management.system.bar_code.service.Bar_codeService;
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
public class Bar_codeServiceImpl implements Bar_codeService {

    private static Logger log = Logger.getLogger(Bar_codeServiceImpl.class);

    @Autowired
    private Bar_codeDao dao;

    @Autowired
    private DeviceService deviceService;


    @Override
    public List<Bar_code> listDevice(int pageStart, int pageSize) {
        return dao.listDevice((pageStart - 1) * pageSize, pageSize);
    }

    @Override
    public int countDevice() {
        return dao.countDevice();
    }

    //入库操作
    // 2019年8月19日 13:44:05更新
    @Override
    public Map<String, Object> updateBar_codeForDevice(Bar_code bar_code) {
        Map<String, Object> result = new HashMap<>();
        String bar_codeId = UUIdUtil.getUUID();
        String createDate = DateUtil.getFullTime();
        try {
            Device device = deviceService.getDeviceById(bar_code.getStock_entity_id());
            if ("".equals(device.getId())) {
                log.error("updateBar_codeForDevice:deviceService");
                result.put("hasError", true);
                result.put("error", "添加出错");
                return result;
            } else {
                //入库记录
                bar_code.setId(bar_codeId);
                bar_code.setBar_code_date(createDate);
                bar_code.setCreateUserId("NO");
                bar_code.setCreateDate(createDate);
                bar_code.setUpdateUserId("NO");
                bar_code.setUpdateDate(createDate);
                Boolean bar_codeResult = dao.addBar_code(bar_code) == 1 ? true : false;
                if (!(bar_codeResult)) {
                    log.error("stockResult:" + bar_codeResult);
                    result.put("hasError", true);
                    result.put("error", "添加出错");
                    return result;
                }
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



}

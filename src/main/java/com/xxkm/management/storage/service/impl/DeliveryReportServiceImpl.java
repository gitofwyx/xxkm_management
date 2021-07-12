package com.xxkm.management.storage.service.impl;

import com.xxkm.core.util.DateUtil;
import com.xxkm.core.util.UUIdUtil;
import com.xxkm.core.util.build_ident.IdentUtil;
import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.storage.dao.DeliveryDao;
import com.xxkm.management.storage.entity.Delivery;
import com.xxkm.management.storage.service.DeliveryReportService;
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
public class DeliveryReportServiceImpl implements DeliveryReportService {


    private static Logger log = Logger.getLogger(DeliveryServiceImpl.class);

    @Autowired
    private DeliveryDao dao;

    @Override
    public List<Delivery> getDeliveryReportSingleParam(String startDate, String endDate) {
        return null;
    }
}

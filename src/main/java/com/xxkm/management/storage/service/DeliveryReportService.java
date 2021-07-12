package com.xxkm.management.storage.service;

import com.xxkm.management.stock.entity.Stock;
import com.xxkm.management.storage.entity.Delivery;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
public interface DeliveryReportService {

    public List<Delivery> getDeliveryReportSingleParam(String startDate, String endDate);


}

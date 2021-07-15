package com.xxkm.management.storage.dao;

import com.xxkm.management.storage.entity.Delivery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Repository
public interface DeliveryReportDao {

    public List<Map<String, Object>> getDeliveryReportSingleParam(String startDate, String endDate);


}

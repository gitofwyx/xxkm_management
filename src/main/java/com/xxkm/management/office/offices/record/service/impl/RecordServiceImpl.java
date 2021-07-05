package com.xxkm.management.office.offices.record.service.impl;

import com.xxkm.management.office.offices.record.dao.RecordDao;
import com.xxkm.management.office.offices.record.entity.Record;
import com.xxkm.management.office.offices.record.service.RecordService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/15.
 */
@Service
public class RecordServiceImpl implements RecordService {

    private static Logger log = Logger.getLogger(RecordServiceImpl.class);

    @Autowired
    private RecordDao dao;


    @Override
    public List<Record> listRecord(int pageStart, int pageSize) {
        return dao.listRecord((pageStart-1)*pageSize, pageSize);
    }

    @Override
    public boolean addRecord(Record record) {
        return dao.addRecord(record)==1?true:false;
    }

    @Override
    public Map<String, Object> getRecordByOffices(String rec_office_id,String date) {
        return dao.getRecordByOffices(rec_office_id,date);
    }

    @Override
    public boolean plusRegCount(Record record) {
        return dao.plusRegCount(record)==1?true:false;
    }

    @Override
    public boolean plusOpeCount(Record record) {
        return dao.plusOpeCount(record)==1?true:false;
    }

    @Override
    public int getUnderlingCount(String belong_to_id) {
        return dao.getUnderlingCount(belong_to_id);
    }

}

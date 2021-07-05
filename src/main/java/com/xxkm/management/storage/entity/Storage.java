package com.xxkm.management.storage.entity;

import com.xxkm.core.entity.BaseInfoEntity;

/**
 * Created by Administrator on 2017/3/15.
 */

public class Storage extends BaseInfoEntity {
    private String id;
    private String class_id;           //种类id
    private String entity_id;           //设备id
    private String stock_id;           //设备id
    private String stock_entity_id;           //设备id
    private String in_confirmed_ident; //库存编号
    private String in_confirmed_type;  //'设备\耗材种类
    private String in_confirmed_by;    //确认人
    private String recovery_officeId;    //回收科室id
    private String in_confirmed_officeId;    //入库科室id
    private String in_confirmed_date;  //入库时间
    private double in_confirmed_no;      //入库数量（按库存单位计）
    private double in_confirmed_total;  //入库总量
    private String in_confirmed_origin;  //入库类型，1：回收；2：新增
    private double  fact_dev_no;         //实际库存
    private String out_flag;          //出库状态-> "0": "未出库", "1": "已出库"
    private String keyWord;           //关键字

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }

    public String getStock_entity_id() {
        return stock_entity_id;
    }

    public void setStock_entity_id(String stock_entity_id) {
        this.stock_entity_id = stock_entity_id;
    }

    public String getIn_confirmed_ident() {
        return in_confirmed_ident;
    }

    public void setIn_confirmed_ident(String in_confirmed_ident) {
        this.in_confirmed_ident = in_confirmed_ident;
    }

    public String getIn_confirmed_type() {
        return in_confirmed_type;
    }

    public void setIn_confirmed_type(String in_confirmed_type) {
        this.in_confirmed_type = in_confirmed_type;
    }

    public String getIn_confirmed_by() {
        return in_confirmed_by;
    }

    public void setIn_confirmed_by(String in_confirmed_by) {
        this.in_confirmed_by = in_confirmed_by;
    }

    public String getRecovery_officeId() {
        return recovery_officeId;
    }

    public void setRecovery_officeId(String recovery_officeId) {
        this.recovery_officeId = recovery_officeId;
    }

    public String getIn_confirmed_officeId() {
        return in_confirmed_officeId;
    }

    public void setIn_confirmed_officeId(String in_confirmed_officeId) {
        this.in_confirmed_officeId = in_confirmed_officeId;
    }

    public String getIn_confirmed_date() {
        return in_confirmed_date;
    }

    public void setIn_confirmed_date(String in_confirmed_date) {
        this.in_confirmed_date = in_confirmed_date;
    }

    public double getIn_confirmed_no() {
        return in_confirmed_no;
    }

    public void setIn_confirmed_no(double in_confirmed_no) {
        this.in_confirmed_no = in_confirmed_no;
    }

    public double getIn_confirmed_total() {
        return in_confirmed_total;
    }

    public void setIn_confirmed_total(double in_confirmed_total) {
        this.in_confirmed_total = in_confirmed_total;
    }

    public String getIn_confirmed_origin() {
        return in_confirmed_origin;
    }

    public void setIn_confirmed_origin(String in_confirmed_origin) {
        this.in_confirmed_origin = in_confirmed_origin;
    }

    public double getFact_dev_no() {
        return fact_dev_no;
    }

    public void setFact_dev_no(double fact_dev_no) {
        this.fact_dev_no = fact_dev_no;
    }

    public String getOut_flag() {
        return out_flag;
    }

    public void setOut_flag(String out_flag) {
        this.out_flag = out_flag;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}

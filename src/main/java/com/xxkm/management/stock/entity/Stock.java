package com.xxkm.management.stock.entity;

import com.xxkm.core.entity.BaseInfoEntity;

/**
 * Created by Administrator on 2017/3/15.
 */

public class Stock extends BaseInfoEntity {
    private String id;
    private String stock_ident;           //库存编号
    private String class_id;             //种类ID
    private String entity_id;             //设备\耗材ID
    private String stock_type;            //库存类别
    private String stock_office_id;            //库存科室编号
    private double stock_no;              //库存数量（按库存单位计）
    private String stock_unit;           //库存单位
    private int stock_proportion;   //个体/单位比例
    private double stock_total;       //库存总量
    private double stock_configured_total;       //已配置置库存总量（已设备总量单位计）
    private String stock_total_unit;  //总量单位
    private String stock_flag;          //库存状态
    private String remark;          //备注
    private String keyWord;           //关键字

    private int stock_version;           //版本号

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getStock_ident() {
        return stock_ident;
    }

    public void setStock_ident(String stock_ident) {
        this.stock_ident = stock_ident;
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

    public String getStock_type() {
        return stock_type;
    }

    public void setStock_type(String stock_type) {
        this.stock_type = stock_type;
    }

    public String getStock_office_id() {
        return stock_office_id;
    }

    public void setStock_office_id(String stock_office_id) {
        this.stock_office_id = stock_office_id;
    }

    public double getStock_no() {
        return stock_no;
    }

    public void setStock_no(double stock_no) {
        this.stock_no = stock_no;
    }

    public String getStock_unit() {
        return stock_unit;
    }

    public void setStock_unit(String stock_unit) {
        this.stock_unit = stock_unit;
    }

    public int getStock_proportion() {
        return stock_proportion;
    }

    public void setStock_proportion(int stock_proportion) {
        this.stock_proportion = stock_proportion;
    }

    public double getStock_total() {
        return stock_total;
    }

    public void setStock_total(double stock_total) {
        this.stock_total = stock_total;
    }

    public double getStock_configured_total() {
        return stock_configured_total;
    }

    public void setStock_configured_total(double stock_configured_total) {
        this.stock_configured_total = stock_configured_total;
    }

    public String getStock_total_unit() {
        return stock_total_unit;
    }

    public void setStock_total_unit(String stock_total_unit) {
        this.stock_total_unit = stock_total_unit;
    }

    public String getStock_flag() {
        return stock_flag;
    }

    public void setStock_flag(String stock_flag) {
        this.stock_flag = stock_flag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public int getStock_version() {
        return stock_version;
    }

    public void setStock_version(int stock_version) {
        this.stock_version = stock_version;
    }
}

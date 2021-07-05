package com.xxkm.management.storage.entity;

import com.xxkm.core.entity.BaseInfoEntity;

/**
 * Created by Administrator on 2017/3/15.
 */

public class Delivery extends BaseInfoEntity {
    private String id;
    private String class_id;           //设备id
    private String entity_id;           //设备id
    private String entity_name;           //设备\耗材名
    private String entity_type;           //设备\耗材型号
    private String stock_id;              //库存ID
    private String stock_entity_id;//库存设备ID
    private String out_confirmed_ident; //出库编号
    private String out_confirmed_type;//设备\耗材种类
    private String out_confirmed_genre;//出库类别（0：配置1.入科2.部署3.回收4.调用5.借用）
    private String out_confirmed_by;//确认人
    private String out_confirmed_officeId;//出库科室
    private String out_confirmed_date;    //出库时间
    private double out_confirmed_no;  //出库数量
    private double out_confirmed_no_2;  //出库数量2
    private String out_confirmed_unit;  //出库单位
    private int out_confirmed_proportion;  //个体/单位比例
    private double out_confirmed_total;      //出库总量
    private String out_confirmed_total_unit;  //总量单位
    private String entity_entry_status;      //入科状态（1：待入科；2：部分待入科；3：已入科；4：部分已入科）
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

    public String getEntity_name() {
        return entity_name;
    }

    public void setEntity_name(String entity_name) {
        this.entity_name = entity_name;
    }

    public String getEntity_type() {
        return entity_type;
    }

    public void setEntity_type(String entity_type) {
        this.entity_type = entity_type;
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

    public String getOut_confirmed_ident() {
        return out_confirmed_ident;
    }

    public void setOut_confirmed_ident(String out_confirmed_ident) {
        this.out_confirmed_ident = out_confirmed_ident;
    }

    public String getOut_confirmed_type() {
        return out_confirmed_type;
    }

    public void setOut_confirmed_type(String out_confirmed_type) {
        this.out_confirmed_type = out_confirmed_type;
    }

    public String getOut_confirmed_genre() {
        return out_confirmed_genre;
    }

    public void setOut_confirmed_genre(String out_confirmed_genre) {
        this.out_confirmed_genre = out_confirmed_genre;
    }

    public String getOut_confirmed_by() {
        return out_confirmed_by;
    }

    public void setOut_confirmed_by(String out_confirmed_by) {
        this.out_confirmed_by = out_confirmed_by;
    }

    public String getOut_confirmed_officeId() {
        return out_confirmed_officeId;
    }

    public void setOut_confirmed_officeId(String out_confirmed_officeId) {
        this.out_confirmed_officeId = out_confirmed_officeId;
    }

    public String getOut_confirmed_date() {
        return out_confirmed_date;
    }

    public void setOut_confirmed_date(String out_confirmed_date) {
        this.out_confirmed_date = out_confirmed_date;
    }

    public double getOut_confirmed_no() {
        return out_confirmed_no;
    }

    public void setOut_confirmed_no(double out_confirmed_no) {
        this.out_confirmed_no = out_confirmed_no;
    }

    public double getOut_confirmed_no_2() {
        return out_confirmed_no_2;
    }

    public void setOut_confirmed_no_2(double out_confirmed_no_2) {
        this.out_confirmed_no_2 = out_confirmed_no_2;
    }

    public String getOut_confirmed_unit() {
        return out_confirmed_unit;
    }

    public void setOut_confirmed_unit(String out_confirmed_unit) {
        this.out_confirmed_unit = out_confirmed_unit;
    }

    public int getOut_confirmed_proportion() {
        return out_confirmed_proportion;
    }

    public void setOut_confirmed_proportion(int out_confirmed_proportion) {
        this.out_confirmed_proportion = out_confirmed_proportion;
    }

    public double getOut_confirmed_total() {
        return out_confirmed_total;
    }

    public void setOut_confirmed_total(double out_confirmed_total) {
        this.out_confirmed_total = out_confirmed_total;
    }

    public String getOut_confirmed_total_unit() {
        return out_confirmed_total_unit;
    }

    public void setOut_confirmed_total_unit(String out_confirmed_total_unit) {
        this.out_confirmed_total_unit = out_confirmed_total_unit;
    }

    public String getEntity_entry_status() {
        return entity_entry_status;
    }

    public void setEntity_entry_status(String entity_entry_status) {
        this.entity_entry_status = entity_entry_status;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}

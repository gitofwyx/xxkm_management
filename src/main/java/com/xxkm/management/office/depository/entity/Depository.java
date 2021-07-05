package com.xxkm.management.office.depository.entity;

import com.xxkm.core.entity.BaseInfoEntity;

/**
 * Created by Administrator on 2017/3/15.
 */

public class Depository extends BaseInfoEntity {

    private String id;
    private String depository_ident; //科室库存编号
    private String delivery_id;         //交付表id
    private String class_id;           //设备id
    private String entity_id;           //设备id
    private String entity_name;           //设备\耗材名
    private String entity_type;           //设备\耗材型号
    private String depository_type;//科室库存类别
    private String depository_by;//入科确认人
    private String depository_officeId;//库存科室
    private double depository_no;  //科室库存数量（按库存单位计）
    private double depository_idle_no;  //闲置库存数量
    private double depository_used_total;  //已使用库存数量
    private String depository_unit;  //科室库存单位
    private int depository_proportion;  //个体/单位比例
    private double depository_total;      //科室库存总量
    private double depository_idle_total;      //闲置库存总量
    private String depository_total_unit;      //科室总量单位
    private String depository_flag;      //科室库存状态
    private String keyWord;           //关键字

    private int depository_version;           //版本号

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getDepository_ident() {
        return depository_ident;
    }

    public void setDepository_ident(String depository_ident) {
        this.depository_ident = depository_ident;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
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

    public String getDepository_type() {
        return depository_type;
    }

    public void setDepository_type(String depository_type) {
        this.depository_type = depository_type;
    }

    public String getDepository_by() {
        return depository_by;
    }

    public void setDepository_by(String depository_by) {
        this.depository_by = depository_by;
    }

    public String getDepository_officeId() {
        return depository_officeId;
    }

    public void setDepository_officeId(String depository_officeId) {
        this.depository_officeId = depository_officeId;
    }

    public double getDepository_no() {
        return depository_no;
    }

    public void setDepository_no(double depository_no) {
        this.depository_no = depository_no;
    }

    public double getDepository_idle_no() {
        return depository_idle_no;
    }

    public void setDepository_idle_no(double depository_idle_no) {
        this.depository_idle_no = depository_idle_no;
    }

    public double getDepository_used_total() {
        return depository_used_total;
    }

    public void setDepository_used_total(double depository_used_total) {
        this.depository_used_total = depository_used_total;
    }

    public String getDepository_unit() {
        return depository_unit;
    }

    public void setDepository_unit(String depository_unit) {
        this.depository_unit = depository_unit;
    }

    public int getDepository_proportion() {
        return depository_proportion;
    }

    public void setDepository_proportion(int depository_proportion) {
        this.depository_proportion = depository_proportion;
    }

    public double getDepository_total() {
        return depository_total;
    }

    public void setDepository_total(double depository_total) {
        this.depository_total = depository_total;
    }

    public double getDepository_idle_total() {
        return depository_idle_total;
    }

    public void setDepository_idle_total(double depository_idle_total) {
        this.depository_idle_total = depository_idle_total;
    }

    public String getDepository_total_unit() {
        return depository_total_unit;
    }

    public void setDepository_total_unit(String depository_total_unit) {
        this.depository_total_unit = depository_total_unit;
    }

    public String getDepository_flag() {
        return depository_flag;
    }

    public void setDepository_flag(String depository_flag) {
        this.depository_flag = depository_flag;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public int getDepository_version() {
        return depository_version;
    }

    public void setDepository_version(int depository_version) {
        this.depository_version = depository_version;
    }
}

package com.xxkm.management.office.devices.entity;

import com.xxkm.core.entity.BaseInfoEntity;

/**
 * Created by Administrator on 2017/3/15.
 */

public class Devices extends BaseInfoEntity {

    private String id;                            //
    private String class_id;                     //种类ID
    private String  devices_ident;               //设备编号
    private String device_id;                   //设备id
    private String office_unique_name;                   //科室专有名称
    private String  device_state;            //设备状态
    private String inventory_office_id;                   //原始出库科室id
    private String location_office_id;                   //现在科室id
    private String  mediaOfLANId;              //通信介质id
    private String  device_origin;             //设备来源（1入科2原科）
    private String  present_stock_id;             //设备来源（1入科2原科）
    private String in_storage_id;             //入库ID
    private String delivery_id;               //出库ID
    private String device_entry_date;        //入科时间
    private String device_deployment_status;  //部署状态
    private String device_deployment_date;  //部署时间
    private String related_flag;             //关联状态
    private String related_id;               //关联设备id
    private String device_location;         //设备具体位置
    private String  device_flag;            //设备标识
    private String devices_auditing_mark;  //设备审核标志
    private String reject_date;             //报废时间
    private String remark;                   //备注
    private String keyWord;                 //关键字

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

    public String getDevices_ident() {
        return devices_ident;
    }

    public void setDevices_ident(String devices_ident) {
        this.devices_ident = devices_ident;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getOffice_unique_name() {
        return office_unique_name;
    }

    public void setOffice_unique_name(String office_unique_name) {
        this.office_unique_name = office_unique_name;
    }

    public String getDevice_state() {
        return device_state;
    }

    public void setDevice_state(String device_state) {
        this.device_state = device_state;
    }

    public String getInventory_office_id() {
        return inventory_office_id;
    }

    public void setInventory_office_id(String inventory_office_id) {
        this.inventory_office_id = inventory_office_id;
    }

    public String getLocation_office_id() {
        return location_office_id;
    }

    public void setLocation_office_id(String location_office_id) {
        this.location_office_id = location_office_id;
    }

    public String getMediaOfLANId() {
        return mediaOfLANId;
    }

    public void setMediaOfLANId(String mediaOfLANId) {
        this.mediaOfLANId = mediaOfLANId;
    }

    public String getDevice_origin() {
        return device_origin;
    }

    public void setDevice_origin(String device_origin) {
        this.device_origin = device_origin;
    }

    public String getPresent_stock_id() {
        return present_stock_id;
    }

    public void setPresent_stock_id(String present_stock_id) {
        this.present_stock_id = present_stock_id;
    }

    public String getIn_storage_id() {
        return in_storage_id;
    }

    public void setIn_storage_id(String in_storage_id) {
        this.in_storage_id = in_storage_id;
    }

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    public String getDevice_entry_date() {
        return device_entry_date;
    }

    public void setDevice_entry_date(String device_entry_date) {
        this.device_entry_date = device_entry_date;
    }

    public String getDevice_deployment_status() {
        return device_deployment_status;
    }

    public void setDevice_deployment_status(String device_deployment_status) {
        this.device_deployment_status = device_deployment_status;
    }

    public String getDevice_deployment_date() {
        return device_deployment_date;
    }

    public void setDevice_deployment_date(String device_deployment_date) {
        this.device_deployment_date = device_deployment_date;
    }

    public String getRelated_flag() {
        return related_flag;
    }

    public void setRelated_flag(String related_flag) {
        this.related_flag = related_flag;
    }

    public String getRelated_id() {
        return related_id;
    }

    public void setRelated_id(String related_id) {
        this.related_id = related_id;
    }

    public String getDevice_location() {
        return device_location;
    }

    public void setDevice_location(String device_location) {
        this.device_location = device_location;
    }

    public String getDevice_flag() {
        return device_flag;
    }

    public void setDevice_flag(String device_flag) {
        this.device_flag = device_flag;
    }

    public String getDevices_auditing_mark() {
        return devices_auditing_mark;
    }

    public void setDevices_auditing_mark(String devices_auditing_mark) {
        this.devices_auditing_mark = devices_auditing_mark;
    }

    public String getReject_date() {
        return reject_date;
    }

    public void setReject_date(String reject_date) {
        this.reject_date = reject_date;
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
}

package com.xxkm.management.device.entity;

import com.xxkm.core.entity.BaseInfoEntity;

/**
 * Created by Administrator on 2017/3/15.
 */

public class DeviceClass extends BaseInfoEntity {
    private String id;
    private String ent_class;     //设备种类
    private int class_ident;     //设备种类编号
    private String class_tab;     //大类标识
    private int dev_max;      //设备最大值
    private int mat_max;      //种类最大值
    private int class_version;      //版本号

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getEnt_class() {
        return ent_class;
    }

    public void setEnt_class(String ent_class) {
        this.ent_class = ent_class;
    }

    public int getClass_ident() {
        return class_ident;
    }

    public void setClass_ident(int class_ident) {
        this.class_ident = class_ident;
    }

    public String getClass_tab() {
        return class_tab;
    }

    public void setClass_tab(String class_tab) {
        this.class_tab = class_tab;
    }

    public int getDev_max() {
        return dev_max;
    }

    public void setDev_max(int dev_max) {
        this.dev_max = dev_max;
    }

    public int getMat_max() {
        return mat_max;
    }

    public void setMat_max(int mat_max) {
        this.mat_max = mat_max;
    }

    public int getClass_version() {
        return class_version;
    }

    public void setClass_version(int class_version) {
        this.class_version = class_version;
    }
}

package com.xxkm.management.office.materials.entity;

import com.xxkm.core.entity.BaseInfoEntity;

/**
 * Created by Administrator on 2017/3/15.
 */

public class Materials extends BaseInfoEntity {

    private String id;
    private String material_id;
    private String  material_ident;
    private String office_id;
    private String material_name;
    private String mediaOfLANId;
    private String related_flag;
    private String related_id;
    private String material_flag;
    private String remark;
    private String keyWord;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(String material_id) {
        this.material_id = material_id;
    }

    public String getMaterial_ident() {
        return material_ident;
    }

    public void setMaterial_ident(String material_ident) {
        this.material_ident = material_ident;
    }

    public String getOffice_id() {
        return office_id;
    }

    public void setOffice_id(String office_id) {
        this.office_id = office_id;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public String getMediaOfLANId() {
        return mediaOfLANId;
    }

    public void setMediaOfLANId(String mediaOfLANId) {
        this.mediaOfLANId = mediaOfLANId;
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

    public String getMaterial_flag() {
        return material_flag;
    }

    public void setMaterial_flag(String material_flag) {
        this.material_flag = material_flag;
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

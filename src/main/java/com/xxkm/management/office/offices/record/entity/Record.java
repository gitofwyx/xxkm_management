package com.xxkm.management.office.offices.record.entity;

import com.xxkm.core.entity.BaseInfoEntity;

/**
 * Created by Administrator on 2017/3/15.
 */

public class Record extends BaseInfoEntity {
    private String id;
    private String rec_office_id;    //记录科室ID
    private int reg_count;       //登记次数
    private int ope_count;       //维护次数
    private String rec_cycle;       //登记周期
    private String rec_starting_date;       //起始时间

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getRec_office_id() {
        return rec_office_id;
    }

    public void setRec_office_id(String rec_office_id) {
        this.rec_office_id = rec_office_id;
    }

    public int getReg_count() {
        return reg_count;
    }

    public void setReg_count(int reg_count) {
        this.reg_count = reg_count;
    }

    public int getOpe_count() {
        return ope_count;
    }

    public void setOpe_count(int ope_count) {
        this.ope_count = ope_count;
    }

    public String getRec_cycle() {
        return rec_cycle;
    }

    public void setRec_cycle(String rec_cycle) {
        this.rec_cycle = rec_cycle;
    }

    public String getRec_starting_date() {
        return rec_starting_date;
    }

    public void setRec_starting_date(String rec_starting_date) {
        this.rec_starting_date = rec_starting_date;
    }
}

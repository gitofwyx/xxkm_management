package com.xxkm.management.office.offices.entity;

import com.xxkm.core.entity.BaseInfoEntity;

/**
 * Created by Administrator on 2017/3/15.
 */

public class Offices extends BaseInfoEntity {
    private String id;
    private int office_ident;
    private String belong_to_id;     //所属科室id
    private String belong_to_office;     //所属科室
    private String office_name;      //科室名
    private String office_property;      //科室性质
    private String office_function;      //科室性质
    private String leading_official;      //科室负责人
    private String keyWord;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public int getOffice_ident() {
        return office_ident;
    }

    public void setOffice_ident(int office_ident) {
        this.office_ident = office_ident;
    }

    public String getBelong_to_id() {
        return belong_to_id;
    }

    public void setBelong_to_id(String belong_to_id) {
        this.belong_to_id = belong_to_id;
    }

    public String getBelong_to_office() {
        return belong_to_office;
    }

    public void setBelong_to_office(String belong_to_office) {
        this.belong_to_office = belong_to_office;
    }

    public String getOffice_name() {
        return office_name;
    }

    public void setOffice_name(String office_name) {
        this.office_name = office_name;
    }

    public String getOffice_property() {
        return office_property;
    }

    public void setOffice_property(String office_property) {
        this.office_property = office_property;
    }

    public String getOffice_function() {
        return office_function;
    }

    public void setOffice_function(String office_function) {
        this.office_function = office_function;
    }

    public String getLeading_official() {
        return leading_official;
    }

    public void setLeading_official(String leading_official) {
        this.leading_official = leading_official;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}

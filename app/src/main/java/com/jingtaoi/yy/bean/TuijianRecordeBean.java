package com.jingtaoi.yy.bean;

import com.jingtaoi.yy.model.ApplyRecord;

import java.util.List;

public class TuijianRecordeBean {
    /**
     * msg : 获取成功
     * code : 0
     * data : [{"mtime":"16","rid":"14326798","ytime":"2019-04-16"}]
     * sys : 1556423443122
     */

    private String msg;
    private int code;
    private String sys;
    private List<ApplyRecord> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public List<ApplyRecord> getData() {
        return data;
    }

    public void setData(List<ApplyRecord> data) {
        this.data = data;
    }
}

package com.siyecaoi.yy.model;

import com.siyecaoi.yy.bean.AllmsgBean;

public class AllMsgModel {

    /**
     * 120 全国消息
     */
    private int code;
    private AllmsgBean.DataEntity data;
    private int state;// 4全国消息

    public AllMsgModel(int code, AllmsgBean.DataEntity data) {
        this.code = code;
        this.data = data;
    }

    public AllMsgModel(int code, AllmsgBean.DataEntity data, int state) {
        this.code = code;
        this.data = data;
        this.state = state;
    }

    public AllmsgBean.DataEntity getData() {
        return data;
    }

    public void setData(AllmsgBean.DataEntity data) {
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }




}

package com.jingtaoi.yy.bean;

import java.util.List;

public class ExchangeHisBean {

    private String msg;
    private int code;
    private List<ExchangeEntity> data;
    private String sys;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(List<ExchangeEntity> data) {
        this.data = data;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public List<ExchangeEntity> getData() {
        return data;
    }

    public String getSys() {
        return sys;
    }

    public class ExchangeEntity {
            public int hdGold;
            public String createTime;
            public int dhDiamond;
        }
}

package com.jingtaoi.yy.bean;

public class AlipayBean {

    private String msg;
    private int code;
    private DataEntity data;
    private long sys;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setSys(long sys) {
        this.sys = sys;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public DataEntity getData() {
        return data;
    }

    public long getSys() {
        return sys;
    }

    public class DataEntity {

        private String orderInfo;

        public void setOrderInfo(String orderInfo) {
            this.orderInfo = orderInfo;
        }

        public String getOrderInfo() {
            return orderInfo;
        }
    }
}

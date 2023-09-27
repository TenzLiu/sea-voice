package com.siyecaoi.yy.bean;

public class TopupOrderBean {

    private String msg;
    private int code;
    private DataEntity data;
    private String sys;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(DataEntity data) {
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

    public DataEntity getData() {
        return data;
    }

    public String getSys() {
        return sys;
    }

    public class DataEntity {
        /**
         * Order : O18112010390001
         * pay : 399
         * id : 2
         */
        private String Order;
        private int pay;
        private int id;

        public void setOrder(String Order) {
            this.Order = Order;
        }

        public void setPay(int pay) {
            this.pay = pay;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrder() {
            return Order;
        }

        public int getPay() {
            return pay;
        }

        public int getId() {
            return id;
        }
    }
}

package com.jingtaoi.yy.bean;

public class InviteShowBean {


    /**
     * msg : 获取成功
     * code : 0
     * data : {"money":0,"txMoney":6,"manNum":0,"fcMoney":0}
     * sys : 1557193544335
     */
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
         * money : 0
         * txMoney : 6
         * manNum : 0
         * fcMoney : 0
         *
         * money	number	我的账号余额
         * txMoney	number	提现需要达到好的钱
         * manNum	number	今日的人数
         * fcMoney	number	分成的金额
         */
        private double money;
        private double txMoney;
        private int manNum;
        private double fcMoney;

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public double getTxMoney() {
            return txMoney;
        }

        public void setTxMoney(double txMoney) {
            this.txMoney = txMoney;
        }

        public int getManNum() {
            return manNum;
        }

        public void setManNum(int manNum) {
            this.manNum = manNum;
        }

        public double getFcMoney() {
            return fcMoney;
        }

        public void setFcMoney(double fcMoney) {
            this.fcMoney = fcMoney;
        }
    }
}

package com.jingtaoi.yy.bean;

public class LotteryMyincomeBean {

    private String msg;
    private int code;
    private DataEntity data;
    private String sys;


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

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public class DataEntity {
//        * 今日收益 profitDay
//  * 收益余额 profitBalance
//  * 今日流水 billFlowDay
//  * 本周流水 billFlowWeek
//  * 本周收益 profitWeek

        private   String profitDay;
        private   String profitBalance;
        private   String billFlowDay;
        private   String billFlowWeek;
        private   String profitWeek;

        public String getProfitDay() {
            return profitDay;
        }

        public void setProfitDay(String profitDay) {
            this.profitDay = profitDay;
        }

        public String getProfitBalance() {
            return profitBalance;
        }

        public void setProfitBalance(String profitBalance) {
            this.profitBalance = profitBalance;
        }

        public String getBillFlowDay() {
            return billFlowDay;
        }

        public void setBillFlowDay(String billFlowDay) {
            this.billFlowDay = billFlowDay;
        }

        public String getBillFlowWeek() {
            return billFlowWeek;
        }

        public void setBillFlowWeek(String billFlowWeek) {
            this.billFlowWeek = billFlowWeek;
        }

        public String getProfitWeek() {
            return profitWeek;
        }

        public void setProfitWeek(String profitWeek) {
            this.profitWeek = profitWeek;
        }
    }
}

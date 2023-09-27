package com.siyecaoi.yy.bean;

import java.util.List;

public class DrawHisBean {

    private String msg;
    private int code;
    private List<DataEntity> data;
    private String sys;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(List<DataEntity> data) {
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

    public List<DataEntity> getData() {
        return data;
    }

    public String getSys() {
        return sys;
    }

    public class DataEntity {
        private String createTime;
        private List<WithdrawEntity> withdraw;

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setWithdraw(List<WithdrawEntity> withdraw) {
            this.withdraw = withdraw;
        }

        public String getCreateTime() {
            return createTime;
        }

        public List<WithdrawEntity> getWithdraw() {
            return withdraw;
        }

        public class WithdrawEntity {
            /**
             * money : 1
             * createTime : 2019-04-10 11:29:08
             * phone :
             * state : 1
             * status : 1
             * <p>
             * createTime	string	提交提现时间
             * money	number	提现金额
             * phone	string	提现电话
             * state	number	1 钻石，2 是分成
             * status	number	1 是待审核， 2已提现， 3已拒绝
             */
            private double money;
            private String createTime;
            private String phone;
            private int state;
            private int status;

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setState(int state) {
                this.state = state;
            }

            public void setStatus(int status) {
                this.status = status;
            }


            public String getCreateTime() {
                return createTime;
            }

            public String getPhone() {
                return phone;
            }

            public int getState() {
                return state;
            }

            public int getStatus() {
                return status;
            }
        }
    }
}

package com.siyecaoi.yy.bean;

import java.util.List;

public class DrawBean {

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
         * trueName	string	真实姓名
         * - - Withdraw	object	无
         * id	number	数据id
         * x	number	提现的钱
         * y	number	需要消耗的钻石
         * state	number	判断是否填写支付宝信息 1是没有，2是有
         * payAccount	string	支付宝
         * ynum	number	钻石数
         * gold	number	浪花数`
         */
        private String trueName;
        private List<WithdrawEntity> Withdraw;
        private int state;
        private String payAccount;
        private String ynum;//double类型，存为string类型
        private int gold;

        public String getYnum() {
            return ynum;
        }

        public void setYnum(String ynum) {
            this.ynum = ynum;
        }

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }

        public void setWithdraw(List<WithdrawEntity> Withdraw) {
            this.Withdraw = Withdraw;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setPayAccount(String payAccount) {
            this.payAccount = payAccount;
        }

        public String getTrueName() {
            return trueName;
        }

        public List<WithdrawEntity> getWithdraw() {
            return Withdraw;
        }

        public int getState() {
            return state;
        }

        public String getPayAccount() {
            return payAccount;
        }

        public class WithdrawEntity {
            /**
             * x : 1
             * y : 2
             * id : 1
             */
            private double x;
            private int y;
            private int id;

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }

            public int getY() {
                return y;
            }

            public void setY(int y) {
                this.y = y;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}

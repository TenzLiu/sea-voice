package com.siyecaoi.yy.bean;

import java.util.List;

public class TopupListBean {

    private String msg;
    private int code;
    private DataBean data;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public static class DataBean {

        /**
         * 	1 首次支付，2 不是首次支付
         */
        private int state;
        private List<SetRechargeBean> SetRecharge;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public List<SetRechargeBean> getSetRecharge() {
            return SetRecharge;
        }

        public void setSetRecharge(List<SetRechargeBean> SetRecharge) {
            this.SetRecharge = SetRecharge;
        }

        public static class SetRechargeBean {
            /**
             * id : 1
             * x : 10
             * y : 10
             */

            /**
             * id	number	id
             * x	number	获取浪花，id=1是首次支付的奖励浪花
             * y	number	支付的钱
             */
            private int id;
            private int x;
            private double y;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getX() {
                return x;
            }

            public void setX(int x) {
                this.x = x;
            }

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }
        }
    }
}

package com.siyecaoi.yy.bean;

import java.util.List;

public class TopupHisBean {

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
        private List<RechargeEntity> recharge;

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setRecharge(List<RechargeEntity> recharge) {
            this.recharge = recharge;
        }

        public String getCreateTime() {
            return createTime;
        }

        public List<RechargeEntity> getRecharge() {
            return recharge;
        }

        public class RechargeEntity {
            /**
             * gold : 220
             * createTime : 2019-04-10 15:00:43
             * mark : 充值20.0元
             *
             * createTime	string	时间
             * gold	number	充值浪花
             * mark	string	内容
             */
            private int gold;
            private String createTime;
            private String mark;

            public void setGold(int gold) {
                this.gold = gold;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public int getGold() {
                return gold;
            }

            public String getCreateTime() {
                return createTime;
            }

            public String getMark() {
                return mark;
            }
        }
    }
}

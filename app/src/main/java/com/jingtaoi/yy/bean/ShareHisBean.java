package com.jingtaoi.yy.bean;

import java.util.List;

public class ShareHisBean {

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
        /**
         * createTime : 2019-03-06 00:00:00
         * invite : [{"money":3,"createTime":"2019-03-06 18:43:52","name":"邀请奖励"}]
         */
        private String createTime;
        private List<InviteEntity> invite;

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setInvite(List<InviteEntity> invite) {
            this.invite = invite;
        }

        public String getCreateTime() {
            return createTime;
        }

        public List<InviteEntity> getInvite() {
            return invite;
        }

        public class InviteEntity {
            /**
             * money : 3
             * createTime : 2019-03-06 18:43:52
             * name : 邀请奖励
             */
            private double money;
            private String createTime;
            private String name;

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}

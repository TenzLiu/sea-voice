package com.jingtaoi.yy.bean;

import java.util.List;

public class InviteAwardBean {

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
         * DivideList : [{"money":2.52,"createTime":"2019-05-09 18:44:20","name":"小小米充值10.0"}]
         * sumMoney : 0
         * today : 2.52
         * sumMoney	number	所得的钱
         * today	number	今天获得的钱
         */
        private List<DivideListEntity> DivideList;
        private double sumMoney;
        private double today;

        public void setDivideList(List<DivideListEntity> DivideList) {
            this.DivideList = DivideList;
        }


        public void setToday(double today) {
            this.today = today;
        }

        public List<DivideListEntity> getDivideList() {
            return DivideList;
        }

        public double getSumMoney() {
            return sumMoney;
        }

        public void setSumMoney(double sumMoney) {
            this.sumMoney = sumMoney;
        }

        public double getToday() {
            return today;
        }

        public class DivideListEntity {
            /**
             * money : 2.52
             * createTime : 2019-05-09 18:44:20
             * name : 小小米充值10.0
             * <p>
             * * createTime	string	时间
             * * money	number	钱
             * * name	string	名称
             */
            private double money;
            private String createTime;
            private String name;

            public void setMoney(double money) {
                this.money = money;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getMoney() {
                return money;
            }

            public String getCreateTime() {
                return createTime;
            }

            public String getName() {
                return name;
            }
        }
    }
}

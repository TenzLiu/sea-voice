package com.jingtaoi.yy.bean;

import java.util.List;

public class InviteNumberBean {

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
         * InviteList : [{"money":2.42,"createTime":"2019-05-06 11:05:33","name":"你邀请\u201c曦筽宋\u201d给你贡献了红包"},{"money":2.84,"createTime":"2019-04-29 17:47:29","name":"你邀请\u201c123456\u201d给你贡献了红包"}]
         * sumMoney : 0
         * today : 0
         * sum : 0
         *
         * sumMoney	number	所得的钱
         * today	number	今天邀请的人
         * sum	number	总邀请人
         */
        private List<InviteListEntity> InviteList;
        private double sumMoney;
        private int today;
        private int sum;

        public void setInviteList(List<InviteListEntity> InviteList) {
            this.InviteList = InviteList;
        }

        public void setSumMoney(int sumMoney) {
            this.sumMoney = sumMoney;
        }

        public void setToday(int today) {
            this.today = today;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }

        public List<InviteListEntity> getInviteList() {
            return InviteList;
        }

        public double getSumMoney() {
            return sumMoney;
        }

        public void setSumMoney(double sumMoney) {
            this.sumMoney = sumMoney;
        }

        public int getToday() {
            return today;
        }

        public int getSum() {
            return sum;
        }

        public class InviteListEntity {
            /**
             * money : 2.42
             * createTime : 2019-05-06 11:05:33
             * name : 你邀请“曦筽宋”给你贡献了红包
             *
             *          * createTime	string	时间
             *          * money	number	钱
             *          * name	string	名称
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

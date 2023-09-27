package com.siyecaoi.yy.bean;

import java.util.List;

/**
 * @author: xha
 * @date: 2020/10/7 18:16
 * @Description:
 */
public class SendRecordBean {
    private String msg;
    private int code;
    private String sys;
    private List<DataBean> data;

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

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         "createTime":"2020-10-07 17:55:20",
         "giveMoney":0,
         "id":41,
         "isDelete":0,
         "isLiang":1,
         "nickname":"哈哈",
         "rechargeMoney":0.0,
         "restitution":42,
         "state":3,
         "sumMoney":500,
         "uid":39,
         "usercoding":"17823459"
         */

        private String createTime;
        private int giveMoney;
        private int id;
        private int isDelete;
        private int isLiang;//1不显示 靓  字       2就显示
        private String nickname;
        private double rechargeMoney;
        private int restitution;
        private double sumMoney;
        private int uid;
        private int state;
        private String usercoding;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getGiveMoney() {
            return giveMoney;
        }

        public void setGiveMoney(int giveMoney) {
            this.giveMoney = giveMoney;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public int getIsLiang() {
            return isLiang;
        }

        public void setIsLiang(int isLiang) {
            this.isLiang = isLiang;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public double getRechargeMoney() {
            return rechargeMoney;
        }

        public void setRechargeMoney(double rechargeMoney) {
            this.rechargeMoney = rechargeMoney;
        }

        public int getRestitution() {
            return restitution;
        }

        public void setRestitution(int restitution) {
            this.restitution = restitution;
        }

        public double getSumMoney() {
            return sumMoney;
        }

        public void setSumMoney(double sumMoney) {
            this.sumMoney = sumMoney;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getUsercoding() {
            return usercoding;
        }

        public void setUsercoding(String usercoding) {
            this.usercoding = usercoding;
        }
    }

}

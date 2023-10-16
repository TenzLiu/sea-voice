package com.jingtaoi.yy.bean;

import java.util.List;

public class ReceivePacketBean {
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
         * createTime : 2019-03-12 17:12:48
         * gold : 2
         * id : 17
         * img : http://thirdqq.qlogo.cn/g?b=oidb&k=1tb7tEdrLaPniasRgbz4yyw&s=100
         * name : 家有逗比小花喵！123456
         * redId : 18
         * rid : 14326798
         * state : 1
         * status : 1
         * uid : 170
         */

        /**
         * createTime	string	无
         * gold	number	浪花
         * id	number	这条数据id
         * redId	number	红包的id
         * rid	string	房间id
         * state	number	是否被定时器 1否，2是
         * status	number	是否被领取红包 1 未，2 是
         * uid	number	被送用户id
         * img	string	发红包用户
         * name	string	发红包头像
         */
        private String createTime;
        private int gold;
        private int id;
        private String img;
        private String name;
        private int redId;
        private String rid;
        private int state;
        private int status;
        private int uid;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRedId() {
            return redId;
        }

        public void setRedId(int redId) {
            this.redId = redId;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
